package com.example.myapplication;

public class MessageBodyModel {

    private String sender,emailForm, emailBody, displaymessagebody, time, message;

    public MessageBodyModel(String sender, String emailFrom, String emailBody, String displaymessagebody, String time, String message) {
        this.sender = sender;
        this.emailForm = emailFrom;
        this.emailBody = emailBody;
        this.displaymessagebody = displaymessagebody;
        this.time = time;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEmailForm() {
        return emailForm;
    }

    public void setEmailForm(String emailForm) {
        this.emailForm = emailForm;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getDisplaymessagebody() {
        return displaymessagebody;
    }

    public void setDisplaymessagebody(String displaymessagebody) {
        this.displaymessagebody = displaymessagebody;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
