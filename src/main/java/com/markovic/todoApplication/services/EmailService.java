package com.markovic.todoApplication.services;

import com.markovic.todoApplication.constant.EmailConstant;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.markovic.todoApplication.constant.EmailConstant.*;

@Service
public class EmailService {

    // When User signs in for the first time - Welcome email
    public void sendNewUserEmail(String username, String email) throws MessagingException {
        Message message = newUserEmail(username, email);
        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtptransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtptransport.sendMessage(message, message.getAllRecipients());
        smtptransport.close();
    }

    private Message newUserEmail(String username, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("Welcome to the Todo Application");
        message.setText("Hello " + username + ", you are successfully registered to the Todo Application \n \n Todo Application Support team");
        return message;
    }

    // When User updates his password while being logged in
    public void sendUpdateUserPassword(String username, String email) throws MessagingException {
        Message message = newUserEmail(username, email);
        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtptransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtptransport.sendMessage(message, message.getAllRecipients());
        smtptransport.close();
    }

    private Message updateUserPassword(String username, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("Informing about Password update occurred");
        message.setText("Hello " + username + ", you have successfully updated your password to the Todo Application. If this action wasn't made by you please look into your account information. \n \n Todo Application Support team");
        return message;
    }

    // When User logs in through a different device or ip
    public void sendUserNewActivity(String username, String email, String ip) throws MessagingException {
        Message message = userNewActivity(username, email, ip);
        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtptransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtptransport.sendMessage(message, message.getAllRecipients());
        smtptransport.close();
    }

    // TODO: 8/15/2020 Implement sending info about the new stigma too
    private Message userNewActivity(String username, String email, String ip) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("New User Activity");
        message.setText("Hello " + username + ", you have successfully registered to the Todo Application from a new device - ip " + ip + ", if the activity wasn't by you please secure your account by changing your password and if needed contact us. \n \n Todo Application Support team");
        return message;
    }

    // Standard session
    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, EmailConstant.GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, EmailConstant.DEFAULT_PORT);
        // Commenting it maybe since I am not using yet ssl certificate
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }

}
