package com.vianavitor.simplelibrarygame.dto.request;

import com.vianavitor.simplelibrarygame.model.Professor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterProfessorRequest(
        @Valid @NotNull Professor professor,
        String classroomCode  // optional
) {}