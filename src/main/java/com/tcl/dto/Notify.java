package com.tcl.dto;

import java.util.ArrayList;

public class Notify {
    main.java.com.tcl.dto.Contact ContactObject;
    main.java.com.tcl.dto.Notification NotificationObject;
    main.java.com.tcl.dto.EventName EventNameObject;
    main.java.com.tcl.dto.TicketInfo TicketInfoObject;
    main.java.com.tcl.dto.AdditionalInfo AdditionalInfoObject;

    ArrayList< Object > notes = new ArrayList < Object > ();


    // Getter Methods

    public main.java.com.tcl.dto.Contact getContact() {
        return ContactObject;
    }

    public main.java.com.tcl.dto.Notification getNotification() {
        return NotificationObject;
    }

    public main.java.com.tcl.dto.EventName getEventName() {
        return EventNameObject;
    }

    public main.java.com.tcl.dto.TicketInfo getTicketInfo() {
        return TicketInfoObject;
    }

    public main.java.com.tcl.dto.AdditionalInfo getAdditionalInfo() {
        return AdditionalInfoObject;
    }

    public ArrayList<Object> getNotes() {
        return notes;
    }

    // Setter Methods

    public void setContact(main.java.com.tcl.dto.Contact contactObject) {
        this.ContactObject = contactObject;
    }

    public void setNotification(main.java.com.tcl.dto.Notification notificationObject) {
        this.NotificationObject = notificationObject;
    }

    public void setEventName(main.java.com.tcl.dto.EventName eventNameObject) {
        this.EventNameObject = eventNameObject;
    }

    public void setTicketInfo(main.java.com.tcl.dto.TicketInfo ticketInfoObject) {
        this.TicketInfoObject = ticketInfoObject;
    }

    public void setAdditionalInfo(main.java.com.tcl.dto.AdditionalInfo additionalInfoObject) {
        this.AdditionalInfoObject = additionalInfoObject;
    }

    public void setNotes(ArrayList<Object> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Notify{" +
                "ContactObject=" + ContactObject +
                ", NotificationObject=" + NotificationObject +
                ", EventNameObject=" + EventNameObject +
                ", TicketInfoObject=" + TicketInfoObject +
                ", AdditionalInfoObject=" + AdditionalInfoObject +
                ", notes=" + notes +
                '}';
    }
}
