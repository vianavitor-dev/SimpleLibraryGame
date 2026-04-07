package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.model.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AddBookRequest(
        @Valid @NotNull Book book,
        boolean confirmed
) {}