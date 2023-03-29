package com.challangependuduk.chalangependuduk.Controller;

import com.challangependuduk.chalangependuduk.Model.Dtos.PendudukRequest;
import com.challangependuduk.chalangependuduk.Model.Dtos.SearchRequest;
import com.challangependuduk.chalangependuduk.Model.Entites.Kependudukan;
import com.challangependuduk.chalangependuduk.Model.Response.CommonResponse;
import com.challangependuduk.chalangependuduk.Model.Response.SuccessResponse;
import com.challangependuduk.chalangependuduk.Service.PendudkService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/citizen")
@Validated
public class PendudukController {

    @Autowired
    PendudkService pendudkService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{size}/{page}/{sort}")
    public ResponseEntity getAllSubdistrict(@PathVariable("size") int size, @PathVariable("page") int page, @PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(size, page, Sort.by("nik").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page,Sort.by("nik").descending());
        }
        Page<Kependudukan> kependudukans = pendudkService.getAll(pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kependudukans);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping
    public ResponseEntity createEntity(@Valid @RequestBody PendudukRequest pendudukRequest){
        Kependudukan kependudukan = modelMapper.map(pendudukRequest, Kependudukan.class);
        Kependudukan result = pendudkService.create(kependudukan);
        CommonResponse commonResponse = new SuccessResponse<>("success", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @PostMapping("/addbulk")
    public ResponseEntity createBulk(@RequestBody List< @Valid PendudukRequest> pendudukRequests){
        List<Kependudukan> kependudukans = pendudukRequests.stream().
                map(studentReq -> modelMapper.map(studentReq, Kependudukan.class))
                .collect(Collectors.toList());
        List<Kependudukan> creatBulk = pendudkService.createBulk(kependudukans);
        CommonResponse commonResponse = new SuccessResponse<>("Success", creatBulk);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/search/citizenByName/{size}/{page}/{sort}")
    public ResponseEntity findByName(@RequestBody SearchRequest searchRequest, @PathVariable("size") int size, @PathVariable("page") int page,@PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(size,page, Sort.by("nik").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size,page, Sort.by("nik").descending());
        }
        Optional<Page<Kependudukan>> kependudukanPage = pendudkService.findByName(searchRequest.getName(), pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kependudukanPage);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }


    @GetMapping("/{nik}")
    public ResponseEntity findByNik(@PathVariable String nik){
        Optional<Kependudukan> kependudukan = pendudkService.findByNik(nik);
        CommonResponse commonResponse = new SuccessResponse<>("success", kependudukan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateKecamatan(@Valid @RequestBody PendudukRequest pendudukRequest, @PathVariable String id){
        Optional<Kependudukan> kependudukan = pendudkService.findbyId(id);
        Kependudukan kependudukan1 = modelMapper.map(pendudukRequest, Kependudukan.class);
        pendudkService.update(kependudukan1, id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kependudukan.get());
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudent(@PathVariable String id){
        Optional<Kependudukan> kependudukan = pendudkService.findbyId(id);
        pendudkService.delete(id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kependudukan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
