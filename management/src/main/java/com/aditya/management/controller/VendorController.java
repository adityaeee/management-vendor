package com.aditya.management.controller;

import com.aditya.management.DTO.request.VendorRequest;
import com.aditya.management.DTO.response.BaseResponse;
import com.aditya.management.DTO.response.CommonResponse;
import com.aditya.management.constant.APiUrl;
import com.aditya.management.entity.Vendor;
import com.aditya.management.service.RateLimitingService;
import com.aditya.management.service.intrface.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APiUrl.VENDOR)
public class VendorController {

    private final VendorService vendorService;
    private final RateLimitingService rateLimitingService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createVendor(@RequestBody VendorRequest request) {

        Vendor vendor = vendorService.createVendor(request);

        BaseResponse response = CommonResponse.<Vendor>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully create vendor")
                .data(vendor)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping
    public ResponseEntity<BaseResponse> updateVendor(@RequestBody Vendor request) {

        Vendor vendor = vendorService.updateVendor(request);

        BaseResponse response = CommonResponse.<Vendor>builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully update data vendor")
                .data(vendor)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllVendor(HttpServletRequest httpRequest) {

        if (rateLimitingService.tryConsume(httpRequest)) {
            List<Vendor> vendor = vendorService.getAllVendor();

            BaseResponse response = CommonResponse.<List<Vendor>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully retrieved vendors")
                    .data(vendor)
                    .build();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(path = APiUrl.PATH_VAR_ID)
    public ResponseEntity<BaseResponse> getVendorById (@PathVariable String id) {

        Vendor vendor = vendorService.getVendorById(id);

        BaseResponse response = CommonResponse.<Vendor>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get data vendor")
                .data(vendor)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping(path = APiUrl.PATH_VAR_ID)
    public ResponseEntity<BaseResponse> deleteVendorById (@PathVariable String id) {

        vendorService.deleteVendor(id);

        BaseResponse response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully delete data vendor")
                .data("Deleted")
                .build();

        return ResponseEntity.ok(response);
    }
}