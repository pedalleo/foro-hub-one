package com.ForoHub.ChallenAlura.dto;

public record TopicoResponseDTO(
        String titulo,
        String mensaje,
        String status,
        String autor,
        String curso
) {}
