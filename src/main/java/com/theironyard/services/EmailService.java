package com.theironyard.services;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.SendGrid;
import org.springframework.stereotype.Service;

/**
 * Created by Nigel on 8/18/16.
 */

@Service
public class EmailService {

    public void sendEmail(String name, String email){
        Email from = new Email("test@example.com");
        String subject = "Hello World from the SendGrid Java Library!";
        Email to = new Email("test@example.com");
        Content content = new Content("text/plain", "Hello, Email!");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));

    }


}
