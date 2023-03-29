package com.challangependuduk.chalangependuduk.Model.Dtos;

import com.challangependuduk.chalangependuduk.Model.Entites.Kelurahan;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class PendudukRequest {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotNull(message = "date of birth cannot be null")
    @Past(message = "date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd")
    private Date dob;
    @NotBlank(message = "Cannot be blank")
    @Enumerated(EnumType.STRING)
    private String gender;
    @NotNull(message = "ward Cannot Be null")
    private Kelurahan kelurahan;
}
