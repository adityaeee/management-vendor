package com.aditya.management.service.intrface;

import com.aditya.management.DTO.request.VendorRequest;
import com.aditya.management.entity.Vendor;

import java.util.List;

public interface VendorService {
    List<Vendor> getAllVendor();
    Vendor getVendorById(String id);
    Vendor createVendor(VendorRequest request);
    Vendor updateVendor(Vendor request);
    void deleteVendor(String id);
}
