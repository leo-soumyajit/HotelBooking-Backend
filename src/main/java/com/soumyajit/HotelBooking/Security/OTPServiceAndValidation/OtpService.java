package com.soumyajit.HotelBooking.Security.OTPServiceAndValidation;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;


@Service
public class OtpService {

    @Autowired
    private JavaMailSender emailSender;

    private final Map<String, OTPDetails> otpStorage = new HashMap<>();
    private final Map<String, Boolean> otpVerified = new HashMap<>();

    public String generateOTP() {
        int otp = (int)(Math.random() * 9000) + 1000;
        return String.valueOf(otp);
    }

    public void sendOTPEmail(String email, String otp) {
        try {
            // Validate email address format
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("hotelbookingapp2025@gmail.com");
            helper.setTo(email);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP code is: " + otp+" \nNote : This OTP is valid for only 10 minutes ");

            emailSender.send(message);
            System.out.println("OTP sent successfully.Check your gmail ");
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            System.err.println("Invalid email address: " + e.getMessage());
        }
    }

    public void saveOTP(String email, String otp) {
        otpStorage.put(email, new OTPDetails(otp, System.currentTimeMillis()));
        otpVerified.put(email, false); // Set OTP verification status to false
    }

    public OTPDetails getOTPDetails(String email) {
        return otpStorage.get(email);
    }

    public void deleteOTP(String email) {
        otpStorage.remove(email);
        // Do not remove verification status here to retain it for signup
    }

    public void clearOTPVerified(String email) {
        otpVerified.remove(email); // Clear OTP verification status
    }

    public boolean isOTPExpired(long timestamp) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - timestamp) > (10 * 60 * 1000); // 10 minutes in milliseconds
    }

    public void markOTPVerified(String email) {
        otpVerified.put(email, true); // Mark OTP as verified
    }

    public boolean isOTPVerified(String email) {
        return otpVerified.getOrDefault(email, false);
    }
}
