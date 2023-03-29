package com.challangependuduk.chalangependuduk.Model.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KecamatanRequest {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "code cannot be blank")
    private String code;
}
