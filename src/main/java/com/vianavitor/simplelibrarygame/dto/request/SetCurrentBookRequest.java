package com.vianavitor.simplelibrarygame.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SetCurrentBookRequest(
        @NotNull Long userId,
        @NotNull Long bookId,
        @PositiveOrZero Integer page
) {}