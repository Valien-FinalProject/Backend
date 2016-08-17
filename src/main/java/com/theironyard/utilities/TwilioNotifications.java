package com.theironyard.utilities;

import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by EddyJ on 8/16/16.
 */
public class TwilioNotifications {

    @Autowired
    Account twilioAccount;

    SmsFactory smsFactory;

    @PostConstruct
    public void setup(){
        smsFactory = twilioAccount.getSmsFactory();
    }

    

}
