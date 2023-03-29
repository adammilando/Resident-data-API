package com.challangependuduk.chalangependuduk.Controller;

import com.challangependuduk.chalangependuduk.Model.Dtos.KecamatanRequest;
import com.challangependuduk.chalangependuduk.Model.Dtos.KelurahanRequest;
import com.challangependuduk.chalangependuduk.Model.Dtos.SearchRequest;
import com.challangependuduk.chalangependuduk.Model.Entites.Kelurahan;
import com.challangependuduk.chalangependuduk.Model.Response.CommonResponse;
import com.challangependuduk.chalangependuduk.Model.Response.SuccessResponse;
import com.challangependuduk.chalangependuduk.Service.KelurahanService;
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
@RequestMapping("/kelurahan")
@Validated
public class KelurahanController {
    @Autowired
    KelurahanService kelurahanService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{size}/{page}/{sort}")
    public ResponseEntity getAllSubdistrict(@PathVariable("size") int size, @PathVariable("page") int page, @PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(size, page, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page,Sort.by("id").descending());
        }
        Page<Kelurahan> kelurahans = kelurahanService.getAll(pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kelurahans);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping
    public ResponseEntity createEntity(@Valid @RequestBody KelurahanRequest kelurahanRequest){
        Kelurahan kelurahan = modelMapper.map(kelurahanRequest, Kelurahan.class);
        Kelurahan result = kelurahanService.create(kelurahan);
        CommonResponse commonResponse = new SuccessResponse<>("success", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @PostMapping("/addbulk")
    public ResponseEntity createBulk(@RequestBody List< @Valid KelurahanRequest> kelurahanRequests){
        List<Kelurahan> kelurahans = kelurahanRequests.stream().
                map(studentReq -> modelMapper.map(studentReq, Kelurahan.class))
                .collect(Collectors.toList());
        List<Kelurahan> creatBulk = kelurahanService.createBulk(kelurahans);
        CommonResponse commonResponse = new SuccessResponse<>("Success", creatBulk);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/search/KecamatanName/{size}/{page}/{sort}")
    public ResponseEntity findKelurahanByKecamatanName(@RequestBody SearchRequest searchRequest, @PathVariable("size") int size, @PathVariable("page") int page, @PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(size,page, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size,page,Sort.by("id").descending());
        }
        Optional<Page<Kelurahan>> kelurahans = kelurahanService.findKelurahanInKecamatan(searchRequest.getName(),pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success",kelurahans);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Optional<Kelurahan> kelurahan = kelurahanService.findById(id);
        CommonResponse commonResponse = new SuccessResponse<>("success", kelurahan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateKecamatan(@Valid @RequestBody KecamatanRequest kelurahanRequest,@PathVariable Long id){
        Optional<Kelurahan> kelurahan = kelurahanService.findById(id);
        Kelurahan kelurahan1 = modelMapper.map(kelurahanRequest, Kelurahan.class);
        kelurahanService.update(kelurahan1, id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kelurahan.get());
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id){
        Optional<Kelurahan> kelurahan = kelurahanService.findById(id);
        kelurahanService.delete(id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kelurahan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
