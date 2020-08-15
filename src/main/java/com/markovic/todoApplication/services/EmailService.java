package com.markovic.todoApplication.services;

import com.markovic.todoApplication.constant.EmailConstant;
import com.markovic.todoApplication.domain.Stigma;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.markovic.todoApplication.constant.EmailConstant.*;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {

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
        // TODO: 8/15/2020 -------- InternetAddress needs to be FIXED!
//        message.setRecipient(TO, InternetAddress.parse(email, false));
//        message.setRecipient(TO, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + username + ", you are successfully registered to the Todo Application \n \n Todo Application Support team");
        return message;
    }

    public void sendUserNewActivity(String username, String email, Stigma stigma) throws MessagingException {
        Message message = userNewActivity(username, email, stigma);
        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtptransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtptransport.sendMessage(message, message.getAllRecipients());
        smtptransport.close();
    }

    // TODO: 8/15/2020 Implement sending info about the new stigma too
    private Message userNewActivity(String username, String email, Stigma stigma) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        // TODO: 8/15/2020 -------- InternetAddress needs to be FIXED!
//        message.setRecipient(TO, InternetAddress.parse(email, false));
//        message.setRecipient(TO, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + username + ", you have successfully registered to the Todo Application from a new device - ip, if the activity wasn't by you please secure your account by changing your password and if needed contact us. \n \n Todo Application Support team");
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, EmailConstant.GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, EmailConstant.DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }

}
