package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmitSummaryRequest(
        @NotBlank(message = "Summary text cannot be blank") String text,
        @NotNull(message = "Book ID is required") Long bookId,
        @NotNull(message = "Student ID is required") Long studentId
) {}