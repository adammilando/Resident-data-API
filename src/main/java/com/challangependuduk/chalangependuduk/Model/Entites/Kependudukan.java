package com.challangependuduk.chalangependuduk.Model.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "penduduk")
public class Kependudukan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob", nullable = false)
    private Date dob;

    @Column(name = "Gender")
    private String Gender;
    @Column(name = "nik", nullable = false)
    private String nik;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_Kelurahan")
    private Kelurahan kelurahan;
}
