package com.java.centralizedNotificationBackend.services;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeMultipart;
@Service
public class EmailService {

    public void sendEmail(String subject,String message,String to) throws MessagingException {

        String from="ms2224850@gmail.com";

        //Variable for gmail
        String host="smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();

        //host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        //Step 1: to get the session object..
        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ms2224850@gmail.com", "alkqsntjejttcwrc");
            }

        });

        session.setDebug(false);

        //Step 2 : compose the message [text,multi media]
        MimeMessage m = new MimeMessage(session);

        //from email
        m.setFrom(from);

        //adding recipient to message
        m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        //adding subject to message
        m.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("src/main/resources/static/file/change_password.png" + "");
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID","<image>");
        multipart.addBodyPart(messageBodyPart);

        m.setContent(multipart);

        //Step 3 : send the message using Transport class
        Transport.send(m);
    }
}
