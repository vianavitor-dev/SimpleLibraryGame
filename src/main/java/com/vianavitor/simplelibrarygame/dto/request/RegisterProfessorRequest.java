package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.dto.request.aux.UserInfoData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterProfessorRequest(
        @Valid @NotNull UserInfoData professor,
        @NotBlank String classroomCode
) {}