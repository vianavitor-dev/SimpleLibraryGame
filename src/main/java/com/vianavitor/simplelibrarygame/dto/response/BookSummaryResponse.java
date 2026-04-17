package com.vianavitor.simplelibrarygame.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record BookSummaryResponse(
        Long id,
        String text,
        @JsonProperty("written_by") StudentInfo writtenBy,
        BookInfo book,
        @JsonProperty("written_at") LocalDate writtenAt
) {
    public record StudentInfo(
            Long id,
            String name
    ) {}

    public record BookInfo(
            Long id,
            String title
    ) {}
}
