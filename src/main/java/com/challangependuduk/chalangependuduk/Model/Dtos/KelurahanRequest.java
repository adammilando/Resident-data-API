package com.challangependuduk.chalangependuduk.Model.Dtos;

import com.challangependuduk.chalangependuduk.Model.Entites.Kecamatan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KelurahanRequest {
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotBlank(message = "postal code cannot be blank")
    private String kodePos;
    @NotNull(message = "subdistric cannot be null")
    private Kecamatan kecamatan;
}
