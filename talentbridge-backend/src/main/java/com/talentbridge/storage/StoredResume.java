package com.talentbridge.storage;

/**
 * Metadata returned after a resume has been stored successfully.
 *
 * originalFileName is safe display metadata.
 * storedFileName is the generated internal filename.
 */
public record StoredResume(
        String originalFileName,
        String storedFileName) {
}