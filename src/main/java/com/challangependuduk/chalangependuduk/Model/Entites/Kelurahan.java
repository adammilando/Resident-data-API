package com.challangependuduk.chalangependuduk.Model.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "keluraan")
public class Kelurahan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "nama", nullable = false)
    private String name;
    @Column(name = "kode_pos", nullable = false, unique = true)
    private String kodePos;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_kecamatan")
    private Kecamatan kecamatan;
}
