package com.challangependuduk.chalangependuduk.Repository;

import com.challangependuduk.chalangependuduk.Model.Entites.Kelurahan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KelurahanRepo extends JpaRepository<Kelurahan, Long> {
    Optional<Page<Kelurahan>> findByKecamatan_NameIgnoreCase(String name, Pageable pageable);
}
