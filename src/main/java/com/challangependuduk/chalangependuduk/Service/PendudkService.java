package com.challangependuduk.chalangependuduk.Service;

import com.challangependuduk.chalangependuduk.Exception.NotFoundException;
import com.challangependuduk.chalangependuduk.Model.Entites.Kependudukan;
import com.challangependuduk.chalangependuduk.Repository.KelurahanRepo;
import com.challangependuduk.chalangependuduk.Repository.PendudukRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PendudkService {

    @Autowired
    private PendudukRepo pendudukRepo;
    @Autowired
    private KelurahanRepo kelurahanRepo;

    public Page<Kependudukan> getAll(Pageable pageable){
        try {
            List<Kependudukan> kependudukans = pendudukRepo.findAll();
            if (kependudukans.isEmpty()){
                throw new NotFoundException("Database Empty");
            }
            Page<Kependudukan> kelurahanPage = pendudukRepo.findAll(pageable);
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

    public Kependudukan create(Kependudukan kependudukan){
        try {
            List<Kependudukan> kependudukans = pendudukRepo.findAll();
            if (kependudukans.stream().anyMatch(existingNik ->
                    existingNik.getNik().equalsIgnoreCase(kependudukan.getNik()))){
                throw new DataIntegrityViolationException("Nik Already Exists "+ kependudukan.getNik());
            }
            kependudukan.setNik(generateNik(kependudukan));
            return pendudukRepo.save(kependudukan);
        }catch (DataIntegrityViolationException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Kependudukan> createBulk(List<Kependudukan> kependudukans){
        try {
            List<Kependudukan> kependudukanList = pendudukRepo.findAll();
            for (Kependudukan kependudukan : kependudukans){
                Set<String> NikSet= kependudukans.stream().map(Kependudukan::getNik).collect(Collectors.toSet());
                if (NikSet.size()!= kependudukans.size()){
                    throw new DataIntegrityViolationException("Duplicate Nik Found in bulk data " + kependudukan.getNik());
                }
                if (kependudukanList.stream().anyMatch(existingNik -> existingNik.getName().equalsIgnoreCase(kependudukan.getNik()))){
                    throw new DataIntegrityViolationException("Nik Already Exists " + kependudukan.getNik());
                }
                String nik = generateNik(kependudukan);
                kependudukan.setNik(nik);
            }
            return pendudukRepo.saveAll(kependudukans);
        }catch (DataIntegrityViolationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Optional<Kependudukan> findbyId(String id){
        try {
            Optional<Kependudukan> kependudukan = pendudukRepo.findById(id);
            if (kependudukan.isEmpty()){
                throw new NotFoundException("Cannot find citizen with given id");
            }
            return kependudukan;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Kependudukan> findByNik(String nik){
        try {
            Optional<Kependudukan> kependudukan = pendudukRepo.findByNikIgnoreCase(nik);
            if (kependudukan.isEmpty()){
                throw new NotFoundException("Cannot Find citizen");
            }
            return kependudukan;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Kependudukan kependudukan, String id) {
        try {
            Optional<Kependudukan> pendudukOptional = pendudukRepo.findByNikIgnoreCase(id);
            if (pendudukOptional.isEmpty()){
                throw new NotFoundException(id + " Not found");
            }
            List<Kependudukan> kependudukans = pendudukRepo.findAll();
            if (kependudukans.stream().anyMatch(existingNik ->
                    existingNik.getNik().equalsIgnoreCase(kependudukan.getNik()))) {
                throw new DataIntegrityViolationException("nik already exists " + kependudukan.getNik());
            }
            Kependudukan existingcitizen = pendudukOptional.get();
            existingcitizen.setName(kependudukan.getName());
            existingcitizen.setAddress(kependudukan.getAddress());
            existingcitizen.setDob(kependudukan.getDob());
            existingcitizen.setGender(kependudukan.getGender());
            existingcitizen.setNik(kependudukan.getNik());
            existingcitizen.setKelurahan(kependudukan.getKelurahan());
            pendudukRepo.save(existingcitizen);
        }catch (NotFoundException | DataIntegrityViolationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        try {
            boolean pendudukDelete = pendudukRepo.existsById(id);
            if (!pendudukDelete){
                throw new NotFoundException(id + " Not found");
            }
            pendudukRepo.deleteById(id);
        }catch (NotFoundException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Optional<Page<Kependudukan>> findByName(String name, Pageable pageable){
        try {
            Optional<Page<Kependudukan>> kependudukanPage= pendudukRepo.findByNameContainsIgnoreCase(name, pageable);
            if (kependudukanPage.isEmpty()){
                throw new NotFoundException("Cannot Found Citizen With given Name");
            }
            Page<Kependudukan> kelurahanPage = pendudukRepo.findAll(pageable);
            if (kelurahanPage.isEmpty()){
                throw new NotFoundException("Wrong Page Size");
            }
            return kependudukanPage;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String generateNik(Kependudukan kependudukan){
        // Mendapatkan kode wilayah berdasarkan kelurahan
        String kodeWilayahJaksel= "3174";
        String kodeWilayah = kelurahanRepo.findById(kependudukan.getKelurahan().getId()).get().getKecamatan().getCode();

        Date dob = kependudukan.getDob();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        String dateOfBirth = String.format("%02d%02d%02d", cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR) % 100);
        if (kependudukan.getGender().equalsIgnoreCase("female")){
            int day = cal.get(Calendar.DATE) + 40;
            dateOfBirth = String.format("%02d%02d%02d", day, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR) % 100);
        }

        int randomNumber = (int)(Math.random()*10000);

        return kodeWilayahJaksel + kodeWilayah + dateOfBirth + String.format("%04d", randomNumber);
    }
}
