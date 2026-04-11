package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.dto.request.aux.UserInfoData;
import com.vianavitor.simplelibrarygame.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record RegisterStudentRequest(
        @NotNull UserInfoData student,
        @NotBlank String classroomCode,
        Set<Genre> favoriteGenres
) { }