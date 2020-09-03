package com.markovic.todoApplication.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import static com.markovic.todoApplication.constant.EmailConstant.*;

@Service
public class EmailService {

    // When User signs in for the first time - Welcome email
    public void sendNewUserEmail(String username, String email) {
        JavaMailSenderImpl mailSender = getEmailSession();
        SimpleMailMessage message = buildMessage("Welcome " + username + " to the TodoApp",
                "We welcome you " + username + " to the TodoApp. \nWe hope you like the services we carefully crafted for you. \n \n The TodoApp Team",
                email);
        mailSender.send(message);
    }

    // When User logs in through a different device or ip
    public void sendUserNewActivity(String username, String email, String ip) {
        JavaMailSenderImpl mailSender = getEmailSession();
        SimpleMailMessage message = buildMessage("New User Activity",
                "Hello " + username + ", you have successfully registered to the Todo Application from a new device - ip " + ip + ", if the activity wasn't by you please secure your account by changing your password and if needed contact us. \n \n TodoApp Application Team",
                email);
        mailSender.send(message);
    }

    // When User updates his password while being logged in
    public void sendUpdateUserPasswordOccurred(String username, String email) {
        JavaMailSenderImpl mailSender = getEmailSession();
        SimpleMailMessage message = buildMessage("Informing about Password update occurred",
                "Hello " + username + ", you have successfully updated your password to the Todo Application. If this action wasn't made by you please look into your account information. \n \n TodoApp Application Team",
                email);
        mailSender.send(message);
    }

    public void sendResetNewPassword(String username, String email, String newPassword){
        JavaMailSenderImpl mailSender = getEmailSession();
        SimpleMailMessage message = buildMessage("Informing about password resetting",
                "Hello " + username + ", you have requested to reset your password of the Todo Application account. Your new password is " + newPassword + ". If this action wasn't made by you please look into your account information. \n \n TodoApp Application Team",
                email);
        mailSender.send(message);
    }

    // Basic message mail
    private SimpleMailMessage buildMessage(String subject, String text, String toMail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testforapplications1212@gmail.com");
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    // Standard session
    private JavaMailSenderImpl getEmailSession() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(SMTP_SERVER);
        mailSender.setPort(DEFAULT_PORT);
        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);
        return mailSender;
    }








    // --Approach for connection with a real Email

//    // When User signs in for the first time - Welcome email
//    public void sendNewUserEmail(String username, String email) throws MessagingException {
//        Message message = newUserEmail(username, email);
//        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//        smtptransport.connect(SMTP_SERVER, USERNAME, PASSWORD);
//        smtptransport.sendMessage(message, message.getAllRecipients());
//        smtptransport.close();
//    }
//
//    private Message newUserEmail(String username, String email) throws MessagingException {
//        Message message = new MimeMessage(getEmailSession());
//        message.setFrom(new InternetAddress(FROM_EMAIL));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
//        message.setSubject("Welcome to the TodoApp Application");
//        message.setText("Hello " + username + ", you are successfully registered to the TodoApp Application \n \n TodoApp Application Support team");
//        return message;
//    }
//
//    // When User updates his password while being logged in
//    public void sendUpdateUserPassword(String username, String email) throws MessagingException {
//        Message message = newUserEmail(username, email);
//        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//        smtptransport.connect(SMTP_SERVER, USERNAME, PASSWORD);
//        smtptransport.sendMessage(message, message.getAllRecipients());
//        smtptransport.close();
//    }
//
//    private Message updateUserPassword(String username, String email) throws MessagingException {
//        Message message = new MimeMessage(getEmailSession());
//        message.setFrom(new InternetAddress(FROM_EMAIL));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
//        message.setSubject("Informing about Password update occurred");
//        message.setText("Hello " + username + ", you have successfully updated your password to the TodoApp Application. If this action wasn't made by you please look into your account information. \n \n Todo Application Support team");
//        return message;
//    }
//
//    // When User logs in through a different device or ip
//    public void sendUserNewActivity(String username, String email, String ip) throws MessagingException {
//        Message message = userNewActivity(username, email, ip);
//        SMTPTransport smtptransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//        smtptransport.connect(SMTP_SERVER, USERNAME, PASSWORD);
//        smtptransport.sendMessage(message, message.getAllRecipients());
//        smtptransport.close();
//    }
//
//    private Message userNewActivity(String username, String email, String ip) throws MessagingException {
//        Message message = new MimeMessage(getEmailSession());
//        message.setFrom(new InternetAddress(FROM_EMAIL));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CC_EMAIL, false));
//        message.setSubject("New User Activity");
//        message.setText("Hello " + username + ", you have successfully registered to the TodoApp Application from a new device - ip " + ip + ", if the activity wasn't by you please secure your account by changing your password and if needed contact us. \n \n TodoApp Application Support team");
//        return message;
//    }
//
//    // Standard session
//    private Session getEmailSession() {
//        Properties properties = System.getProperties();
//        properties.put(SMTP_HOST, EmailConstant.SMTP_SERVER);
//        properties.put(SMTP_AUTH, true);
//        properties.put(SMTP_PORT, EmailConstant.DEFAULT_PORT);
//        properties.put(SMTP_STARTTLS_ENABLE, true);
//        properties.put(SMTP_STARTTLS_REQUIRED, true);
//        return Session.getInstance(properties, null);
//    }

}
