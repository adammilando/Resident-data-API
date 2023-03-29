package com.challangependuduk.chalangependuduk.Service;

import com.challangependuduk.chalangependuduk.Exception.NotFoundException;
import com.challangependuduk.chalangependuduk.Model.Entites.Kecamatan;
import com.challangependuduk.chalangependuduk.Model.Entites.Kelurahan;
import com.challangependuduk.chalangependuduk.Repository.KelurahanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KelurahanService {
    @Autowired
    private KelurahanRepo kelurahanRepo;

    public Page<Kelurahan> getAll(Pageable pageable){
        try {
            List<Kelurahan> kelurahans = kelurahanRepo.findAll();
            if (kelurahans.isEmpty()){
                throw new NotFoundException("Database Empty");
            }
            Page<Kelurahan> kelurahanPage = kelurahanRepo.findAll(pageable);
            if (kelurahanPage.isEmpty()){
                throw new NotFoundException("Wrong Page Size");
            }
            return kelurahanPage;
        }catch (NotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Kelurahan create(Kelurahan kelurahan){
        try {
            List<Kelurahan> kelurahans = kelurahanRepo.findAll();
            if (kelurahans.stream().anyMatch(existingKelurahan ->
                    existingKelurahan.getKodePos().equalsIgnoreCase(kelurahan.getKodePos()))){
                throw new DataIntegrityViolationException("ward already exist" + kelurahan.getKodePos());
            }
            return kelurahanRepo.save(kelurahan);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Kelurahan> createBulk(List<Kelurahan> kelurahans){
        try {
            for (Kelurahan kelurahan : kelurahans){
                Set<String> kodeSet= kelurahans.stream().map(Kelurahan::getKodePos).collect(Collectors.toSet());
                if (kodeSet.size()!= kelurahans.size()){
                    throw new DataIntegrityViolationException("Duplicate ward code found in bulk data "+ kelurahan.getKodePos());
                }
                if (kelurahans.stream().anyMatch(existingSubdis ->
                        existingSubdis.getName().equalsIgnoreCase(kelurahan.getKodePos()))){
                    throw new DataIntegrityViolationException("ward code already exists " + kelurahan.getKodePos());
                }
            }
            return kelurahanRepo.saveAll(kelurahans);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Optional<Kelurahan> findById(Long id) {
        try {
            Optional<Kelurahan> kelurahan = kelurahanRepo.findById(id);
            if (kelurahan.isEmpty()){
                throw new NotFoundException("ward With " + id + " Not Exists");
            }
            return kelurahan;
        }catch (NotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void update(Kelurahan kelurahan, Long id) {
        try {
            Optional<Kelurahan> kelurahanOptional = kelurahanRepo.findById(id);
            if (kelurahanOptional.isEmpty()){
                throw new NotFoundException(id + " Not found");
            }
            List<Kelurahan> kelurahans = kelurahanRepo.findAll();
            if (kelurahans.stream().anyMatch(existingTeacher ->
                    existingTeacher.getName().equalsIgnoreCase(kelurahan.getKodePos()))) {
                throw new DataIntegrityViolationException("ward code already exists " + kelurahan.getKodePos());
            }
            Kelurahan existingward = kelurahanOptional.get();
            existingward.setName(kelurahan.getName());
            existingward.setKodePos(kelurahan.getKodePos());
            existingward.setKecamatan(kelurahan.getKecamatan());
            kelurahanRepo.save(existingward);
        }catch (NotFoundException | DataIntegrityViolationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        try {
            boolean kelurahanDelete = kelurahanRepo.existsById(id);
            if (!kelurahanDelete){
                throw new NotFoundException(id + " Not found");
            }
            kelurahanRepo.deleteById(id);
        }catch (NotFoundException e){
            throw e;
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("Cannot Delete ward with id " + id + " because it has relation with other data");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Optional<Page<Kelurahan>> findKelurahanInKecamatan(String name, Pageable pageable){
        try {
            Optional<Page<Kelurahan>> kelurahans = kelurahanRepo.findByKecamatan_NameIgnoreCase(name, pageable);
            if (kelurahans.isEmpty()){
                throw new NotFoundException("Cannot Find Subdistrict");
            }
            Page<Kelurahan> kelurahanPage = kelurahanRepo.findAll(pageable);
            if (kelurahanPage.isEmpty()){
                throw new NotFoundException("Wrong Page Size");
            }
            return kelurahans;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
