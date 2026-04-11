package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Genre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.Set;

 public record AddBookRequest(
        @NotNull BookInfo book,
        boolean confirmed
) {
    public record BookInfo(
            @NotNull String title,
            String synopsis,
            @NotEmpty Set<Author> authors,
            @NotEmpty Set<Genre> genres,
            @PositiveOrZero Integer pages,
            LocalDate releasedAt
    ) {}
}