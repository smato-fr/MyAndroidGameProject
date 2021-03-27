package fr.smato.gameproject.model;

public class LocationModel {

    private double x;
    private double y;

    public LocationModel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public LocationModel() {

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
