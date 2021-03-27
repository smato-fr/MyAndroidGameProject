package fr.smato.gameproject.model;

public class Friend {
    private String friend1, friend2;
    private int state;

    public Friend(String friend1, String friend2, int state) {
        this.friend1 = friend1;
        this.friend2 = friend2;
        this.state = state;
    }

    public Friend() {

    }

    public String getFriend1() {
        return friend1;
    }

    public String getFriend2() {
        return friend2;
    }

    public int getState() {
        return state;
    }

    public void setFriend1(String friend1) {
        this.friend1 = friend1;
    }

    public void setFriend2(String friend2) {
        this.friend2 = friend2;
    }

    public void setState(int state) {
        this.state = state;
    }
}
