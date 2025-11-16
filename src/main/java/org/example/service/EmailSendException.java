package org.example.service;

public class EmailSendException extends RuntimeException {

    private int statusCode;
    private String messageBody;

    public EmailSendException(int statusCode, String body) {
        this.statusCode = statusCode;
        this.messageBody = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
