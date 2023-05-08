package com.netsurfingzone.dto;

import com.netsurfingzone.dto.Body;
import com.netsurfingzone.dto.ToRecipient;
import com.netsurfingzone.dto.CcRecipient;

import java.util.ArrayList;

public class Message{
    public String subject;
    public Body body;
    public ArrayList<ToRecipient> toRecipients;
    public ArrayList<CcRecipient> ccRecipients;
}
