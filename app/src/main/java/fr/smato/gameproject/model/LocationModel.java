package fr.smato.gameproject.model;

public class LocationModel {

    private double x;
    private double y;
    private String room;

    public LocationModel(double x, double y, String room) {
        this.x = x;
        this.y = y;
        this.room = room;
    }

    public LocationModel() {

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getRoom() {
        return room;
    }
}
