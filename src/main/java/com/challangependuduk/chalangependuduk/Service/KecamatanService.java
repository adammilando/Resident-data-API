package com.challangependuduk.chalangependuduk.Service;

import com.challangependuduk.chalangependuduk.Exception.NotFoundException;
import com.challangependuduk.chalangependuduk.Model.Entites.Kecamatan;
import com.challangependuduk.chalangependuduk.Repository.KecamatanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KecamatanService {
    @Autowired
    private KecamatanRepo kecamatanRepo;

    public Page<Kecamatan> getAll(Pageable pageable){
        try {
            List<Kecamatan> kecamatans = kecamatanRepo.findAll();
            if (kecamatans.isEmpty()){
                throw new NotFoundException("Database Empty");
            }
            Page<Kecamatan> kecamatanPage = kecamatanRepo.findAll(pageable);
            if (kecamatanPage.isEmpty()){
                throw new NotFoundException("Wrong Page Size");
            }
            return kecamatanPage;
        }catch (NotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Kecamatan create(Kecamatan kecamatan){
        try {
            List<Kecamatan> kecamatans = kecamatanRepo.findAll();
            if (kecamatans.stream().anyMatch(existingKecamatan ->
                    existingKecamatan.getName().equalsIgnoreCase(kecamatan.getName()))){
                throw new DataIntegrityViolationException("sub district already exist" + kecamatan.getName());
            }
            return kecamatanRepo.save(kecamatan);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Kecamatan> createBulk(List<Kecamatan> kecamatans){
        try {
            List<Kecamatan> kecamatanList = kecamatanRepo.findAll();
            for (Kecamatan kecamatan : kecamatans){
                Set<String> nameSet= kecamatans.stream().map(Kecamatan::getName).collect(Collectors.toSet());
                if (nameSet.size()!= kecamatans.size()){
                    throw new DataIntegrityViolationException("Duplicate subdistrict found in bulk data "+ kecamatan.getName());
                }
                if (kecamatanList.stream().anyMatch(existingSubdis ->
                        existingSubdis.getName().equalsIgnoreCase(kecamatan.getName()))){
                    throw new DataIntegrityViolationException("sub district already exists " + kecamatan.getName());
                }
            }
            return kecamatanRepo.saveAll(kecamatans);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Optional<Kecamatan> findById(Long id) {
        try {
            Optional<Kecamatan> kecamatan = kecamatanRepo.findById(id);
            if (kecamatan.isEmpty()){
                throw new NotFoundException("subdistrict With " + id + " Not Exists");
            }
            return kecamatan;
        }catch (NotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void update(Kecamatan kecamatan, Long id) {
        try {
            Optional<Kecamatan> optionalKecamatan = kecamatanRepo.findById(id);
            if (optionalKecamatan.isEmpty()){
                throw new NotFoundException(id + " Not found");
            }
            List<Kecamatan> kecamatans = kecamatanRepo.findAll();
            if (kecamatans.stream().anyMatch(existingTeacher ->
                    existingTeacher.getName().equalsIgnoreCase(kecamatan.getName()))) {
                throw new DataIntegrityViolationException("Subdistrict already exists " + kecamatan.getName());
            }
            Kecamatan existingSubdistrict = optionalKecamatan.get();
            existingSubdistrict.setName(kecamatan.getName());
            kecamatanRepo.save(existingSubdistrict);
        }catch (NotFoundException | DataIntegrityViolationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        try {
            boolean kecamatanDelete = kecamatanRepo.existsById(id);
            if (!kecamatanDelete){
                throw new NotFoundException(id + " Not found");
            }
            kecamatanRepo.deleteById(id);
        }catch (NotFoundException e){
            throw e;
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("Cannot Delete subdistrict with id " + id + " because it has relation with other data");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
