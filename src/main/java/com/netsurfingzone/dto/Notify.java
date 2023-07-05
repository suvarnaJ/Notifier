package com.netsurfingzone.dto;

import java.util.ArrayList;

public class Notify {
    main.java.com.netsurfingzone.dto.Contact ContactObject;
    main.java.com.netsurfingzone.dto.Notification NotificationObject;
    main.java.com.netsurfingzone.dto.EventName EventNameObject;
    main.java.com.netsurfingzone.dto.TicketInfo TicketInfoObject;
    main.java.com.netsurfingzone.dto.AdditionalInfo AdditionalInfoObject;

    ArrayList< Object > notes = new ArrayList < Object > ();


    // Getter Methods

    public main.java.com.netsurfingzone.dto.Contact getContact() {
        return ContactObject;
    }

    public main.java.com.netsurfingzone.dto.Notification getNotification() {
        return NotificationObject;
    }

    public main.java.com.netsurfingzone.dto.EventName getEventName() {
        return EventNameObject;
    }

    public main.java.com.netsurfingzone.dto.TicketInfo getTicketInfo() {
        return TicketInfoObject;
    }

    public main.java.com.netsurfingzone.dto.AdditionalInfo getAdditionalInfo() {
        return AdditionalInfoObject;
    }

    public ArrayList<Object> getNotes() {
        return notes;
    }

    // Setter Methods

    public void setContact(main.java.com.netsurfingzone.dto.Contact contactObject) {
        this.ContactObject = contactObject;
    }

    public void setNotification(main.java.com.netsurfingzone.dto.Notification notificationObject) {
        this.NotificationObject = notificationObject;
    }

    public void setEventName(main.java.com.netsurfingzone.dto.EventName eventNameObject) {
        this.EventNameObject = eventNameObject;
    }

    public void setTicketInfo(main.java.com.netsurfingzone.dto.TicketInfo ticketInfoObject) {
        this.TicketInfoObject = ticketInfoObject;
    }

    public void setAdditionalInfo(main.java.com.netsurfingzone.dto.AdditionalInfo additionalInfoObject) {
        this.AdditionalInfoObject = additionalInfoObject;
    }

    public void setNotes(ArrayList<Object> notes) {
        this.notes = notes;
    }
}
