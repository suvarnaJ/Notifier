package com.java.centralizedNotificationBackend.repository;

import com.java.centralizedNotificationBackend.entities.UserTemplates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTemplateRepository extends JpaRepository<UserTemplates,Long> {

    @Query("select u from UserTemplates u where u.user.id = :userId")
    public Page<UserTemplates> findUserTemplatesByUser(@Param("userId")Long userId, Pageable pageable);

    public UserTemplates getUserTemplateByUserTemplate(String userTemplate);

}
