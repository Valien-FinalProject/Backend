package com.theironyard.services;

import com.theironyard.entities.Child;
import com.theironyard.entities.Parent;
import com.theironyard.entities.Reward;
import com.theironyard.services.ChildRepository;
import com.theironyard.services.ParentRepository;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EddyJ on 8/16/16.
 */
@Service
public class TwilioNotifications {

    @Autowired
    Account twilioAccount;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ChildRepository childRepository;

    @Value("${twilio.number}")
    public String twilioNumber;

    SmsFactory smsFactory;

    @PostConstruct
    public void setup() {
        smsFactory = twilioAccount.getSmsFactory();
    }

    public void chorePending(Parent parent) throws TwilioRestException {
        String phone = parent.getPhone();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", phone));
        params.add(new BasicNameValuePair("From", twilioNumber));
        params.add(new BasicNameValuePair("Body", "You have a chore pending that is waiting for your approval!"));
        System.out.println("Pending chore notification sent!");
        smsFactory.create(params);
    }

    public void parentRegister(Parent parent) throws TwilioRestException {
        String phone = parent.getPhone();
        String message = String.format("Thank you %s for signing up!\n", parent.getName());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", phone));
        params.add(new BasicNameValuePair("From", twilioNumber));
        params.add(new BasicNameValuePair("Body", message));
        System.out.println("Register notification sent!");
        smsFactory.create(params);
    }

    public void wishlistItemAdded(Parent parent, Child child) throws TwilioRestException {
        String phone = parent.getPhone();
        String message = String.format("Hello %s, we are informing you that there is a new item in %s's wishlist!\n", parent.getName(), child.getName());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", phone));
        params.add(new BasicNameValuePair("From", twilioNumber));
        params.add(new BasicNameValuePair("Body", message));
        System.out.println("Child has added an item to their wishlist");
        smsFactory.create(params);
    }

    public void childCashedInPoints(Parent parent, Child child, Reward reward) throws TwilioRestException {
        String phone = parent.getPhone();
        String message = String.format("Hello %s,%s has cashed in some of their points for the reward, %s!\n", parent.getName(), child.getName(), reward.getName());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", phone));
        params.add(new BasicNameValuePair("From", twilioNumber));
        params.add(new BasicNameValuePair("Body", message));
        System.out.println("Child has added an item to their wishlist");
        smsFactory.create(params);
    }
}
