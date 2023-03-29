package com.challangependuduk.chalangependuduk.Repository;

import com.challangependuduk.chalangependuduk.Model.Entites.Kependudukan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendudukRepo extends JpaRepository<Kependudukan, String> {
    Optional<Kependudukan> findByKelurahan_Kecamatan_Code(String code);
    Optional<Kependudukan> findByNikIgnoreCase(String nik);
    Optional<Page<Kependudukan>> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
