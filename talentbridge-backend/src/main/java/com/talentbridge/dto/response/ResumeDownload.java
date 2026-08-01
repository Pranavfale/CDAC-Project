package com.talentbridge.dto.response;

import org.springframework.core.io.Resource;

/**
 * Carries a candidate resume from the service to the controller.
 *
 * This object is used internally for producing a file-download response.
 */
public record ResumeDownload(
        Resource resource,
        String fileName,
        String contentType,
        long contentLength) {
}