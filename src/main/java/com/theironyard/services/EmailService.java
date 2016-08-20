package com.theironyard.services;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Nigel on 8/18/16.
 */

@Service
public class EmailService {

    @Value("${sendgrid.key}")
    private String sendgridKey;

    private SendGrid sendGrid;

    @PostConstruct
    public void setup(){
        sendGrid = new SendGrid(sendgridKey);
    }

    public void sendEmail(String name, String email) throws IOException {
        Email from = new Email("Valien.webapp@gmail.com");
        String subject = "New Notification from Valien Inc.";
        Email to = new Email(email);
        Content content = new Content("text/plain", "Valien salutes you, " + name);
        Mail mail = new Mail(from, subject, to, content);

        //System.out.println(System.getenv())
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sendGrid.api(request);
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);
        } catch (IOException ex) {throw ex;}
    }
}
