package com.lucumango.ding;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.UUID;

public class TestSender {

    public static void main(String[] args) {
        // To use this, you need to set environment variables or replace these with actual values
        final String username = System.getenv("GMAIL_USER");
        final String password = System.getenv("GMAIL_APP_PASSWORD");
        
        if (username == null || password == null) {
            System.err.println("GMAIL_USER or GMAIL_APP_PASSWORD environment variables are not set.");
            System.err.println("Please set them before running the script.");
            return;
        }

        String toEmail = "recipient@example.com"; // Replace with recipient email
        if (args.length > 0) {
            toEmail = args[0];
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("Test Email with Tracker - Ding");

            // Build the tracking URL
            String trackingId = UUID.randomUUID().toString();
            // In a real scenario, this domain comes from the SAM deployment (Output TrackingApi)
            String trackingUrl = "https://your-api-gateway-url/Prod/track/" + trackingId + ".gif";
            System.out.println("Generated Tracking ID: " + trackingId);
            System.out.println("Tracking URL: " + trackingUrl);

            String htmlContent = "<h2>Hello from Ding!</h2>"
                    + "<p>This is a test email to verify the tracking pixel functionality.</p>"
                    + "<img src=\"" + trackingUrl + "\" width=\"1\" height=\"1\" border=\"0\" alt=\"\" />";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            System.out.println("Sending email...");
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
