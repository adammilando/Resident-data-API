package com.challangependuduk.chalangependuduk.Repository;

import com.challangependuduk.chalangependuduk.Model.Entites.Kecamatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KecamatanRepo extends JpaRepository<Kecamatan, Long> {
}
