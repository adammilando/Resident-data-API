package com.challangependuduk.chalangependuduk.Model.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "kecamatan")
public class Kecamatan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name" , nullable = false, unique = true)
    private String name;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
}
