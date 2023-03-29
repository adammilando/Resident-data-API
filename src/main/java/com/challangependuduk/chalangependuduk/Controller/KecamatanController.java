package com.challangependuduk.chalangependuduk.Controller;

import com.challangependuduk.chalangependuduk.Model.Dtos.KecamatanRequest;
import com.challangependuduk.chalangependuduk.Model.Entites.Kecamatan;
import com.challangependuduk.chalangependuduk.Model.Response.CommonResponse;
import com.challangependuduk.chalangependuduk.Model.Response.SuccessResponse;
import com.challangependuduk.chalangependuduk.Service.KecamatanService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/subdistrict")
@Validated
public class KecamatanController {
    @Autowired
    KecamatanService kecamatanService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{size}/{page}/{sort}")
    public ResponseEntity getAllSubdistrict(@PathVariable("size") int size, @PathVariable("page") int page, @PathVariable("sort") String sort){
        Pageable pageable = PageRequest.of(size, page, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page,Sort.by("id").descending());
        }
        Page<Kecamatan> kecamatans = kecamatanService.getAll(pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kecamatans);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping
    public ResponseEntity createEntity(@Valid @RequestBody KecamatanRequest kecamatanRequest){
        Kecamatan kecamatan = modelMapper.map(kecamatanRequest, Kecamatan.class);
        Kecamatan result = kecamatanService.create(kecamatan);
        CommonResponse commonResponse = new SuccessResponse<>("success", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @PostMapping("/addbulk")
    public ResponseEntity createBulk(@RequestBody List< @Valid KecamatanRequest> kecamatanRequests){
        List<Kecamatan> students = kecamatanRequests.stream().
                map(studentReq -> modelMapper.map(studentReq, Kecamatan.class))
                .collect(Collectors.toList());
        List<Kecamatan> creatBulk = kecamatanService.createBulk(students);
        CommonResponse commonResponse = new SuccessResponse<>("Success", creatBulk);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity getKecamatanById(@PathVariable Long id){
        Optional<Kecamatan> kecamatan = kecamatanService.findById(id);
        CommonResponse commonResponse = new SuccessResponse<>("success", kecamatan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateKecamatan(@Valid @RequestBody KecamatanRequest kecamatanRequest,@PathVariable Long id){
        Optional<Kecamatan> kecamatan = kecamatanService.findById(id);
        Kecamatan kecamatan1 = modelMapper.map(kecamatanRequest, Kecamatan.class);
        kecamatanService.update(kecamatan1, id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kecamatan.get());
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudent(@PathVariable Long id){
        Optional<Kecamatan> kecamatan = kecamatanService.findById(id);
        kecamatanService.delete(id);
        CommonResponse commonResponse = new SuccessResponse<>("Success", kecamatan);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
