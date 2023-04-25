package com.java.centralizedNotificationBackend.services.Impl;

import com.java.centralizedNotificationBackend.entities.UserTemplates;
import com.java.centralizedNotificationBackend.repository.UserTemplateRepository;
import com.java.centralizedNotificationBackend.services.UserTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserTemplateServiceImpl implements UserTemplateService {

    @Autowired
    private UserTemplateRepository userTemplateRepository;

    @Override
    public UserTemplates createUserTemplate(UserTemplates userTemplates) {
        return userTemplateRepository.save(userTemplates);
    }

    @Override
    public UserTemplates getUserTemplateById(Long userTemplateId) {
        return this.userTemplateRepository.getById(userTemplateId);
    }

    @Override
    public List<UserTemplates> getAllUserTemplate() {
        return this.userTemplateRepository.findAll();
    }

    @Override
    public void deleteUserTemplate(Long userTemplateId) {
        this.userTemplateRepository.deleteById(userTemplateId);
    }

    @Override
    public void deleteAllUserTemplate() {
        this.userTemplateRepository.deleteAll();
    }

    @Override
    public UserTemplates updateUserTemplate(Long id, UserTemplates userTemplates) {
        UserTemplates userTemplates1 = this.userTemplateRepository.findById(id).get();
        userTemplates1.setUserTemplate(userTemplates.getUserTemplate());
        userTemplates1.setUser(userTemplates.getUser());
        userTemplates1.setUpdatedAt(new Date());
        return this.userTemplateRepository.save(userTemplates1);
    }
}
