package com.talentbridge.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import com.talentbridge.exception.InvalidResumeFileException;
import com.talentbridge.exception.ResumeNotFoundException;

/**
 * Unit tests for LocalResumeStorageService.
 *
 * Each test uses a temporary directory created by JUnit. The tests do not
 * access the application's real uploads/resumes directory.
 */
class LocalResumeStorageServiceTest {

    private static final long MAX_RESUME_SIZE_BYTES =
            5L * 1024L * 1024L;

    private static final String PDF_CONTENT_TYPE =
            "application/pdf";

    private static final String DOC_CONTENT_TYPE =
            "application/msword";

    private static final String DOCX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument."
                    + "wordprocessingml.document";

    private static final String UUID_FILE_PATTERN =
            "^[0-9a-fA-F]{8}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{4}-"
                    + "[0-9a-fA-F]{12}"
                    + "\\.(pdf|doc|docx)$";

    @TempDir
    private Path temporaryStorageDirectory;

    private LocalResumeStorageService resumeStorageService;

    @BeforeEach
    void setUp() {

        resumeStorageService =
                new LocalResumeStorageService(
                        temporaryStorageDirectory.toString());
    }

    @Test
    @DisplayName(
        "store saves a valid PDF using a generated UUID filename"
    )
    void storeSavesValidPdfUsingGeneratedFilename()
            throws IOException {

        byte[] pdfContent =
                createValidPdfContent();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate Resume.PDF",
                        PDF_CONTENT_TYPE,
                        pdfContent);

        StoredResume storedResume =
                resumeStorageService.store(file);

        assertNotNull(storedResume);

        assertEquals(
                "Candidate Resume.PDF",
                storedResume.originalFileName());

        assertTrue(
                storedResume.storedFileName()
                        .matches(UUID_FILE_PATTERN));

        assertTrue(
                storedResume.storedFileName()
                        .endsWith(".pdf"));

        Path storedPath =
                temporaryStorageDirectory.resolve(
                        storedResume.storedFileName());

        assertTrue(Files.exists(storedPath));
        assertTrue(Files.isRegularFile(storedPath));

        assertArrayEquals(
                pdfContent,
                Files.readAllBytes(storedPath));

        assertEquals(
                1L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "store saves a valid legacy DOC file"
    )
    void storeSavesValidDocFile()
            throws IOException {

        byte[] docContent =
                createValidDocContent();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.doc",
                        DOC_CONTENT_TYPE,
                        docContent);

        StoredResume storedResume =
                resumeStorageService.store(file);

        assertNotNull(storedResume);

        assertEquals(
                "Candidate_Resume.doc",
                storedResume.originalFileName());

        assertTrue(
                storedResume.storedFileName()
                        .matches(UUID_FILE_PATTERN));

        assertTrue(
                storedResume.storedFileName()
                        .endsWith(".doc"));

        Path storedPath =
                temporaryStorageDirectory.resolve(
                        storedResume.storedFileName());

        assertTrue(Files.exists(storedPath));

        assertArrayEquals(
                docContent,
                Files.readAllBytes(storedPath));
    }

    @Test
    @DisplayName(
        "store saves a valid DOCX file"
    )
    void storeSavesValidDocxFile()
            throws IOException {

        byte[] docxContent =
                createValidDocxContent();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.docx",
                        DOCX_CONTENT_TYPE,
                        docxContent);

        StoredResume storedResume =
                resumeStorageService.store(file);

        assertNotNull(storedResume);

        assertEquals(
                "Candidate_Resume.docx",
                storedResume.originalFileName());

        assertTrue(
                storedResume.storedFileName()
                        .matches(UUID_FILE_PATTERN));

        assertTrue(
                storedResume.storedFileName()
                        .endsWith(".docx"));

        Path storedPath =
                temporaryStorageDirectory.resolve(
                        storedResume.storedFileName());

        assertTrue(Files.exists(storedPath));

        assertArrayEquals(
                docxContent,
                Files.readAllBytes(storedPath));
    }

    @Test
    @DisplayName(
        "store rejects an empty resume"
    )
    void storeRejectsEmptyResume() {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        new byte[0]);

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Resume file is required",
                exception.getMessage());

        assertFalse(
                Files.exists(
                        temporaryStorageDirectory.resolve(
                                "Candidate_Resume.pdf")));
    }

    @Test
    @DisplayName(
        "store rejects a resume larger than 5 MB"
    )
    void storeRejectsOversizedResume()
            throws IOException {

        byte[] oversizedContent =
                new byte[
                        (int) MAX_RESUME_SIZE_BYTES + 1];

        byte[] pdfSignature =
                "%PDF-".getBytes(
                        StandardCharsets.US_ASCII);

        System.arraycopy(
                pdfSignature,
                0,
                oversizedContent,
                0,
                pdfSignature.length);

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Large_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        oversizedContent);

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Resume file must not exceed 5 MB",
                exception.getMessage());

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "store rejects an unsupported file extension"
    )
    void storeRejectsUnsupportedExtension()
            throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.txt",
                        "text/plain",
                        "resume content".getBytes(
                                StandardCharsets.UTF_8));

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Only PDF, DOC and DOCX resumes are allowed",
                exception.getMessage());

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "store rejects a MIME type that does not match the extension"
    )
    void storeRejectsMimeTypeMismatch()
            throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.pdf",
                        DOC_CONTENT_TYPE,
                        createValidPdfContent());

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Resume content type does not match the file extension",
                exception.getMessage());

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "store rejects a corrupted file signature"
    )
    void storeRejectsCorruptedFileSignature()
            throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        "This is not a real PDF file"
                                .getBytes(
                                        StandardCharsets.UTF_8));

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Resume file content is invalid or corrupted",
                exception.getMessage());

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "store rejects path traversal in the original filename"
    )
    void storeRejectsUnsafeOriginalFilename()
            throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "../Candidate_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        createValidPdfContent());

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.store(file));

        assertEquals(
                "Resume filename contains invalid characters",
                exception.getMessage());

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "load returns the stored resource and safe response metadata"
    )
    void loadReturnsStoredResumeAndMetadata()
            throws IOException {

        byte[] pdfContent =
                createValidPdfContent();

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        pdfContent);

        StoredResume storedResume =
                resumeStorageService.store(file);

        StoredResumeFile loadedResume =
                resumeStorageService.load(
                        storedResume.storedFileName());

        assertNotNull(loadedResume);
        assertNotNull(loadedResume.resource());

        assertTrue(
                loadedResume.resource().exists());

        assertTrue(
                loadedResume.resource().isReadable());

        assertEquals(
                PDF_CONTENT_TYPE,
                loadedResume.contentType());

        assertEquals(
                (long) pdfContent.length,
                loadedResume.contentLength());

        try (var inputStream =
                loadedResume.resource()
                        .getInputStream()) {

            assertArrayEquals(
                    pdfContent,
                    inputStream.readAllBytes());
        }
    }

    @Test
    @DisplayName(
        "load rejects a filename containing path traversal"
    )
    void loadRejectsPathTraversalFilename() {

        String unsafeStoredFileName =
                "../11111111-1111-1111-1111-111111111111.pdf";

        InvalidResumeFileException exception =
                assertThrows(
                        InvalidResumeFileException.class,
                        () ->
                                resumeStorageService.load(
                                        unsafeStoredFileName));

        assertEquals(
                "Invalid stored resume filename",
                exception.getMessage());
    }

    @Test
    @DisplayName(
        "load returns not found when a valid generated filename is missing"
    )
    void loadRejectsMissingStoredFile() {

        String missingStoredFileName =
                "11111111-1111-1111-1111-111111111111.pdf";

        ResumeNotFoundException exception =
                assertThrows(
                        ResumeNotFoundException.class,
                        () ->
                                resumeStorageService.load(
                                        missingStoredFileName));

        assertEquals(
                "Resume file not found",
                exception.getMessage());
    }

    @Test
    @DisplayName(
        "deleteIfExists removes an existing stored resume"
    )
    void deleteIfExistsRemovesStoredResume()
            throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "Candidate_Resume.pdf",
                        PDF_CONTENT_TYPE,
                        createValidPdfContent());

        StoredResume storedResume =
                resumeStorageService.store(file);

        Path storedPath =
                temporaryStorageDirectory.resolve(
                        storedResume.storedFileName());

        assertTrue(Files.exists(storedPath));

        resumeStorageService.deleteIfExists(
                storedResume.storedFileName());

        assertFalse(Files.exists(storedPath));

        assertEquals(
                0L,
                countStoredFiles());
    }

    @Test
    @DisplayName(
        "deleteIfExists safely ignores null and blank filenames"
    )
    void deleteIfExistsIgnoresNullAndBlankNames() {

        assertDoesNotThrow(
                () ->
                        resumeStorageService
                                .deleteIfExists(null));

        assertDoesNotThrow(
                () ->
                        resumeStorageService
                                .deleteIfExists(""));

        assertDoesNotThrow(
                () ->
                        resumeStorageService
                                .deleteIfExists("   "));
    }

    /**
     * Creates a minimal byte sequence with a valid PDF signature.
     */
    private byte[] createValidPdfContent() {

        return (
                "%PDF-1.7\n"
                        + "TalentBridge Candidate Resume\n"
                        + "%%EOF")
                .getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Creates a minimal byte sequence with the legacy Microsoft DOC
     * compound-document signature.
     */
    private byte[] createValidDocContent() {

        return new byte[] {
            (byte) 0xD0,
            (byte) 0xCF,
            (byte) 0x11,
            (byte) 0xE0,
            (byte) 0xA1,
            (byte) 0xB1,
            (byte) 0x1A,
            (byte) 0xE1,
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x03,
            (byte) 0x04
        };
    }

    /**
     * Creates a minimal byte sequence with a ZIP signature used by DOCX.
     */
    private byte[] createValidDocxContent() {

        return new byte[] {
            (byte) 0x50,
            (byte) 0x4B,
            (byte) 0x03,
            (byte) 0x04,
            (byte) 0x14,
            (byte) 0x00,
            (byte) 0x06,
            (byte) 0x00,
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x03,
            (byte) 0x04
        };
    }

    /**
     * Counts regular files in the temporary storage directory.
     */
    private long countStoredFiles()
            throws IOException {

        try (Stream<Path> paths =
                Files.list(temporaryStorageDirectory)) {

            return paths
                    .filter(Files::isRegularFile)
                    .count();
        }
    }
}