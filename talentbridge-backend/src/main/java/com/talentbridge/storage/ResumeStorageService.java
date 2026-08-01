package com.talentbridge.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Defines secure resume-storage operations.
 */
public interface ResumeStorageService {

    /**
     * Validates and stores one resume file.
     *
     * @param file uploaded resume
     * @return original display filename and generated stored filename
     */
    StoredResume store(MultipartFile file);

    /**
     * Securely loads a stored resume using its generated internal filename.
     *
     * @param storedFileName generated stored filename
     * @return loaded resume resource and response metadata
     */
    StoredResumeFile load(String storedFileName);

    /**
     * Deletes a previously stored resume when it exists.
     *
     * A null or blank filename is ignored.
     *
     * @param storedFileName internally generated filename
     */
    void deleteIfExists(String storedFileName);
}