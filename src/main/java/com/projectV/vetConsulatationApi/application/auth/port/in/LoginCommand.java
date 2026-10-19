package com.projectV.vetConsulatationApi.application.auth.port.in;

public record LoginCommand(
        String email,
        String password
) {}