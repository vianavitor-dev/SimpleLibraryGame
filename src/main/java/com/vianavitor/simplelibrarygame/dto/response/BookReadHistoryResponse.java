package com.vianavitor.simplelibrarygame.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record BookReadHistoryResponse(
        Long id,
        StudentInfo user,
        UsersBookReadHistoryResponse.BookInfo book,
        @JsonProperty("last_update") @Nullable LocalDate lastUpdate,
        @JsonProperty("last_page_read") int lastPageRead
) {
    public record StudentInfo(
            Long id,
            String name
    )  {}

    public record BookInfo(
            Long id,
            String title
    ) {}
}
