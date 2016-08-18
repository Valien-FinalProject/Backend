package com.theironyard.services;

import com.sendgrid.SendGrid;
import org.springframework.stereotype.Service;

/**
 * Created by Nigel on 8/18/16.
 */

@Service
public class EmailService {

    public void sendEmail(String name, String email){

        SendGrid sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));

    }


}
