package com.talentbridge.storage;

import org.springframework.core.io.Resource;

/**
 * Represents a securely loaded resume file.
 *
 * It contains the file resource and safe HTTP response metadata, but it does
 * not expose the absolute filesystem path.
 */
public record StoredResumeFile(
        Resource resource,
        String contentType,
        long contentLength) {
}