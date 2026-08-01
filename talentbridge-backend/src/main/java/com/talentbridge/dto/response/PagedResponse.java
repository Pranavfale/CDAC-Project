package com.talentbridge.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Consistent API response for paginated results.
 *
 * @param content current page content
 * @param page zero-based current page number
 * @param size requested page size
 * @param totalElements total matching records
 * @param totalPages total number of pages
 * @param first whether this is the first page
 * @param last whether this is the last page
 * @param <T> response content type
 */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    /**
     * Converts a Spring Data Page into the external API response.
     */
    public static <T> PagedResponse<T> from(
            Page<T> pageResult) {

        return new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast());
    }
}