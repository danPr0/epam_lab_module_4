package com.epam.esm.service;

public interface EmailService {

    void sendSignupConfirmationEmail(String receiver, String confirmLink);

    void sendPasswordResetEmail(String receiver, String newPassword, String forwardLink);
}
