package com.theironyard.services;

import com.sendgrid.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Nigel on 8/18/16.
 */

@Service
public class EmailService {

    public void sendEmail(String name, String email) throws IOException {
        Email from = new Email("test@example.com");
        String subject = "New Notification from Iron Chores!";
        Email to = new Email(email);
        Content content = new Content("text/plain", "Hello, " + name);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sendGrid.api(request);
            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);
        } catch (IOException ex) {
            throw ex;

    }


}
