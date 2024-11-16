package com.aditya.management.service;

import com.aditya.management.DTO.request.VendorRequest;
import com.aditya.management.entity.Vendor;
import com.aditya.management.repository.VendorRepository;
import com.aditya.management.service.intrface.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public List<Vendor> getAllVendor() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor getVendorById(String id) {
        return getVendorOrElseThrowException(id);
    }

    @Override
    public Vendor createVendor(VendorRequest request) {
        return vendorRepository.saveAndFlush(Vendor.builder()
                .name(request.getName())
                .build());
    }

    @Override
    public Vendor updateVendor(Vendor request) {
        Vendor vendor = getVendorOrElseThrowException(request.getId());

        vendor.setName(request.getName());

        return vendorRepository.saveAndFlush(vendor);
    }

    @Override
    public void deleteVendor(String id) {
        Vendor vendor = getVendorOrElseThrowException(id);

        vendorRepository.delete(vendor);
    }

    public Vendor getVendorOrElseThrowException(String id) {
        return vendorRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
    }
}
