package com.vianavitor.simplelibrarygame.dto.request.aux;

import jakarta.validation.constraints.NotBlank;

public record UserInfoData (
        @NotBlank(message = "username is required") String username,
        @NotBlank(message = "password is required") String password,
        String name
) { }
