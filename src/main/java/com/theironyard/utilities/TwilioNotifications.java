package com.theironyard.utilities;

import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EddyJ on 8/16/16.
 */
@Service
public class TwilioNotifications {

    public static final String TWILIO_NUMBER = "+17026080979";

    @Autowired
    Account twilioAccount;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

    SmsFactory smsFactory;

    @PostConstruct
    public void setup(){
        smsFactory = twilioAccount.getSmsFactory();
    }

    public void chorePending(Parent parent) throws TwilioRestException {
        String phone = parent.getPhone();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", phone));
        params.add(new BasicNameValuePair("From", TWILIO_NUMBER));
        params.add(new BasicNameValuePair("Body", "You have a chore pending that is waiting for your approval!"));
        System.out.println("Pending chore notification sent!");

        smsFactory.create(params);
    }

}
