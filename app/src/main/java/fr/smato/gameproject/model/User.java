package fr.smato.gameproject.model;

import fr.smato.gameproject.utils.Data;

public class User implements Data {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    //private String club;


    public User(String id, String username, String imageURL, String status/*, String club*/) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
       // this.club = club;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }


    /*public String getClub() {
        return club;
    }*/

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
