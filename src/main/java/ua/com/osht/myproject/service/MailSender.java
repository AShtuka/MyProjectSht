package ua.com.osht.myproject.service;

public interface MailSender {
    void send(String emailTo, String subject, String message);
}
