package com.vianavitor.simplelibrarygame.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record UsersBookReadHistoryResponse(
        Long id,
        BookInfo book,
        @JsonProperty("last_update") @Nullable LocalDate lastUpdate,
        @JsonProperty("last_page_read") int lastPageRead
) {
    public record BookInfo(
            Long id,
            String title
    ) {}
}
