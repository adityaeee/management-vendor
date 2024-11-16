package com.aditya.management.service;

import com.aditya.management.constant.UserRole;
import com.aditya.management.entity.Role;
import com.aditya.management.repository.RoleRepository;
import com.aditya.management.service.intrface.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role)
                .orElseGet(()-> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
