package com.java.centralizedNotificationBackend.services;

import com.java.centralizedNotificationBackend.entities.UserTemplates;

import java.util.List;

public interface UserTemplateService {

    //creating userTemplate
    public UserTemplates createUserTemplate(UserTemplates userTemplates);

    //get userTemplate by userTemplateId
    public UserTemplates getUserTemplateById(Long userTemplateId);

    //get all userTemplate
    public List<UserTemplates> getAllUserTemplate();

    //delete userTemplate by id
    public void deleteUserTemplate(Long userTemplateId);

    //delete allUserTemplate by id
    public void deleteAllUserTemplate();

    //update UserTemplate by id
    public UserTemplates updateUserTemplate(Long id,UserTemplates userTemplates);
}
