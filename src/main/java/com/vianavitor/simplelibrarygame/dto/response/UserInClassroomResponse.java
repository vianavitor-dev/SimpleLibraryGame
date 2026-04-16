package com.vianavitor.simplelibrarygame.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record UserInClassroomResponse(
        Long id,
        String name,
        String username,
        LocalDate lastLogin,
        @Nullable CurrentBookInfo currentBook,
        @JsonProperty("is_professor") boolean isProfessor,
        boolean active
) {
    public record CurrentBookInfo (
            @Nullable Long id,
            @Nullable String title
    ) {}
}
