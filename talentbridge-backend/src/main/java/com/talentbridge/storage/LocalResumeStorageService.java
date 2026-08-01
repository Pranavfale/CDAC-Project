package com.talentbridge.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talentbridge.exception.InvalidResumeFileException;
import com.talentbridge.exception.ResumeStorageException;

/**
 * Stores validated candidate resumes on the local filesystem.
 *
 * User-supplied filenames are retained only as display metadata.
 * Every stored file receives a generated UUID filename.
 */
@Service
public class LocalResumeStorageService
        implements ResumeStorageService {

    private static final long MAX_RESUME_SIZE_BYTES =
            5L * 1024L * 1024L;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("pdf", "doc", "docx");

    private static final Map<String, Set<String>> ALLOWED_MIME_TYPES =
            Map.of(
                    "pdf",
                    Set.of("application/pdf"),

                    "doc",
                    Set.of("application/msword"),

                    "docx",
                    Set.of(
                            "application/vnd.openxmlformats-officedocument."
                                    + "wordprocessingml.document")
            );

    private static final Pattern STORED_FILE_NAME_PATTERN =
            Pattern.compile(
                    "^[0-9a-fA-F]{8}-"
                            + "[0-9a-fA-F]{4}-"
                            + "[0-9a-fA-F]{4}-"
                            + "[0-9a-fA-F]{4}-"
                            + "[0-9a-fA-F]{12}"
                            + "\\.(pdf|doc|docx)$");

    private static final byte[] PDF_SIGNATURE =
            "%PDF-".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] DOC_SIGNATURE = {
        (byte) 0xD0,
        (byte) 0xCF,
        (byte) 0x11,
        (byte) 0xE0,
        (byte) 0xA1,
        (byte) 0xB1,
        (byte) 0x1A,
        (byte) 0xE1
    };

    private final Path storageRoot;

    public LocalResumeStorageService(
            @Value(
                    "${talentbridge.resume.storage-path:"
                            + "uploads/resumes}"
            )
            String storagePath) {

        this.storageRoot = Paths
                .get(storagePath)
                .toAbsolutePath()
                .normalize();
    }

    /**
     * Validates and stores one resume.
     */
    @Override
    public StoredResume store(MultipartFile file) {

        Path targetPath = null;

        try {
            ValidatedResume validatedResume =
                    validateResume(file);

            Files.createDirectories(storageRoot);

            String storedFileName =
                    UUID.randomUUID()
                            + "."
                            + validatedResume.extension();

            targetPath = resolveStoredPath(storedFileName);

            try (InputStream inputStream =
                    file.getInputStream()) {

                /*
                 * REPLACE_EXISTING is intentionally not used.
                 * Although a UUID collision is extremely unlikely,
                 * an existing file must never be overwritten silently.
                 */
                Files.copy(inputStream, targetPath);
            }

            return new StoredResume(
                    validatedResume.originalFileName(),
                    storedFileName);

        } catch (InvalidResumeFileException exception) {
            throw exception;

        } catch (IOException exception) {
            deletePartiallyWrittenFile(targetPath);

            throw new ResumeStorageException(
                    "Could not store the resume file",
                    exception);
        }
    }

    /**
     * Deletes a stored resume when it exists.
     */
    @Override
    public void deleteIfExists(String storedFileName) {

        if (storedFileName == null
                || storedFileName.isBlank()) {

            return;
        }

        try {
            Path storedPath =
                    resolveStoredPath(storedFileName);

            Files.deleteIfExists(storedPath);

        } catch (InvalidResumeFileException exception) {
            throw exception;

        } catch (IOException exception) {
            throw new ResumeStorageException(
                    "Could not delete the previous resume file",
                    exception);
        }
    }

    /**
     * Validates the file's size, filename, extension, MIME type and basic
     * file signature.
     */
    private ValidatedResume validateResume(
            MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new InvalidResumeFileException(
                    "Resume file is required");
        }

        if (file.getSize() > MAX_RESUME_SIZE_BYTES) {
            throw new InvalidResumeFileException(
                    "Resume file must not exceed 5 MB");
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new InvalidResumeFileException(
                    "Resume filename is required");
        }

        String cleanedFileName =
                validateOriginalFileName(originalFileName);

        String extension =
                extractExtension(cleanedFileName);

        validateMimeType(
                extension,
                file.getContentType());

        validateFileSignature(
                extension,
                file);

        return new ValidatedResume(
                cleanedFileName,
                extension);
    }

    /**
     * Rejects paths, control characters and oversized filenames.
     */
    private String validateOriginalFileName(
            String originalFileName) {

        String cleanedFileName =
                originalFileName.trim();

        if (cleanedFileName.isBlank()
                || cleanedFileName.contains("..")
                || cleanedFileName.contains("/")
                || cleanedFileName.contains("\\")
                || containsControlCharacter(cleanedFileName)) {

            throw new InvalidResumeFileException(
                    "Resume filename contains invalid characters");
        }

        if (cleanedFileName.length() > 255) {
            throw new InvalidResumeFileException(
                    "Resume filename is too long");
        }

        return cleanedFileName;
    }

    /**
     * Extracts and validates the file extension.
     */
    private String extractExtension(String fileName) {

        int lastDotIndex =
                fileName.lastIndexOf('.');

        if (lastDotIndex <= 0
                || lastDotIndex == fileName.length() - 1) {

            throw new InvalidResumeFileException(
                    "Resume must have a valid file extension");
        }

        String extension =
                fileName
                        .substring(lastDotIndex + 1)
                        .toLowerCase(Locale.ROOT);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidResumeFileException(
                    "Only PDF, DOC and DOCX resumes are allowed");
        }

        return extension;
    }

    /**
     * Ensures the request MIME type matches the file extension.
     */
    private void validateMimeType(
            String extension,
            String contentType) {

        if (contentType == null
                || contentType.isBlank()) {

            throw new InvalidResumeFileException(
                    "Resume content type is missing");
        }

        String normalizedContentType =
                contentType
                        .split(";", 2)[0]
                        .trim()
                        .toLowerCase(Locale.ROOT);

        Set<String> allowedMimeTypes =
                ALLOWED_MIME_TYPES.get(extension);

        if (allowedMimeTypes == null
                || !allowedMimeTypes.contains(
                        normalizedContentType)) {

            throw new InvalidResumeFileException(
                    "Resume content type does not match "
                            + "the file extension");
        }
    }

    /**
     * Performs a basic document-signature check.
     */
    private void validateFileSignature(
            String extension,
            MultipartFile file) throws IOException {

        byte[] header;

        try (InputStream inputStream =
                file.getInputStream()) {

            header = inputStream.readNBytes(8);
        }

        boolean validSignature =
                switch (extension) {
                    case "pdf" ->
                        startsWith(
                                header,
                                PDF_SIGNATURE);

                    case "doc" ->
                        startsWith(
                                header,
                                DOC_SIGNATURE);

                    case "docx" ->
                        hasZipSignature(header);

                    default -> false;
                };

        if (!validSignature) {
            throw new InvalidResumeFileException(
                    "Resume file content is invalid or corrupted");
        }
    }

    /**
     * Resolves a generated filename underneath the configured root.
     */
    private Path resolveStoredPath(
            String storedFileName) {

        if (storedFileName == null
                || storedFileName.isBlank()
                || !STORED_FILE_NAME_PATTERN
                        .matcher(storedFileName)
                        .matches()) {

            throw new InvalidResumeFileException(
                    "Invalid stored resume filename");
        }

        Path resolvedPath =
                storageRoot
                        .resolve(storedFileName)
                        .normalize();

        if (!resolvedPath.startsWith(storageRoot)) {
            throw new InvalidResumeFileException(
                    "Invalid resume storage path");
        }

        return resolvedPath;
    }

    private void deletePartiallyWrittenFile(Path targetPath) {

        if (targetPath == null) {
            return;
        }

        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException ignored) {
            /*
             * The original storage exception is more important.
             * A failed cleanup must not replace it.
             */
        }
    }

    private boolean containsControlCharacter(String value) {

        return value
                .chars()
                .anyMatch(Character::isISOControl);
    }

    private boolean startsWith(
            byte[] actual,
            byte[] expected) {

        if (actual.length < expected.length) {
            return false;
        }

        for (int index = 0;
                index < expected.length;
                index++) {

            if (actual[index] != expected[index]) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCX files use a ZIP container beginning with a PK signature.
     */
    private boolean hasZipSignature(byte[] header) {

        if (header.length < 4) {
            return false;
        }

        boolean startsWithPk =
                header[0] == (byte) 0x50
                        && header[1] == (byte) 0x4B;

        boolean validZipMarker =
                (header[2] == (byte) 0x03
                        && header[3] == (byte) 0x04)
                || (header[2] == (byte) 0x05
                        && header[3] == (byte) 0x06)
                || (header[2] == (byte) 0x07
                        && header[3] == (byte) 0x08);

        return startsWithPk && validZipMarker;
    }

    /**
     * Internal result created after validation succeeds.
     */
    private record ValidatedResume(
            String originalFileName,
            String extension) {
    }
}