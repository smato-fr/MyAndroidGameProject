package fr.smato.gameproject.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String date;
    public String id;

    public Chat(String sender, String receiver, String message, String date, int id) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
    }

    public Chat() {

    }


    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

}
