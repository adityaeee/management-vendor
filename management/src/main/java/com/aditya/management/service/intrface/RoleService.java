package com.aditya.management.service.intrface;

import com.aditya.management.constant.UserRole;
import com.aditya.management.entity.Role;

public interface RoleService {
    Role getOrSave (UserRole role);
}
