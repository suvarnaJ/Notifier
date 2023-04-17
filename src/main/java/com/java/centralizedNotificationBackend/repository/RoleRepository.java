package com.java.centralizedNotificationBackend.repository;

import com.java.centralizedNotificationBackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
