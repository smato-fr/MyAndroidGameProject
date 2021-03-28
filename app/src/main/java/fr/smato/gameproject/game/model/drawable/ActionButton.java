package fr.smato.gameproject.game.model.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.utils.callback.Event;

public class ActionButton extends Entity {

    private int mainButtonSize;
    private int actionButtonSize;

    private int actionButtonDistance;
    private Bitmap mainButtonImage;
    private List<ButtonAction> actionButtons;

    private boolean extanded = false;

    public ActionButton(Context context, GameViewI game) {
        super(context, game);
        mainButtonImage = WaitingGameView.loadImage(R.drawable.ic_test);
        actionButtons = new ArrayList<>();
    }

    @Override
    public void resize(Location loc, Object... objects3) {
        super.location = loc;
        mainButtonSize = (int) objects3[0];
        actionButtonSize = (int) objects3[1];
        actionButtonDistance = (int) objects3[2];

        super.circleRadious = mainButtonSize/2;

        mainButtonImage = WaitingGameView.resizeImage(mainButtonImage, mainButtonSize, mainButtonSize);
        for (int i = 0; i < actionButtons.size(); i++) {
            ButtonAction ac = actionButtons.get(i);
            ac.image = WaitingGameView.resizeImage(ac.image, actionButtonSize, actionButtonSize);
            ac.pos = getActionButtonLocation(Math.toRadians(120/actionButtons.size()*i));
        }

    }

    @Override
    public void update(){}

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(mainButtonImage, (int) location.x, (int) location.y, paint);
        if (extanded) {
            for (ButtonAction ac : actionButtons) {
                canvas.drawBitmap(ac.image, (int) ac.pos.x, (int) ac.pos.y, paint);
            }
        }

    }

    @Override
    public double getX() {
        return location.x + mainButtonSize/2 ;
    }

    @Override
    public double getY() {
        return location.y + mainButtonSize/2;
    }

    private Location getActionButtonLocation(double radians) {
        return new Location(getX() - (actionButtonDistance*Math.cos(radians)), getY() - actionButtonDistance*Math.sin(radians));
    }


    public void setActionButtons(List<ButtonAction> actionButtons) {
        this.actionButtons = actionButtons;
    }

    public void touch(double x, double y) {

        if (isTouched(x, y))   {
            extanded = !extanded;
        }

        else if (extanded) {

            for (ButtonAction ac : actionButtons) {
                if (ac.isTouched(x, y)) {
                    ac.onClick();
                }
            }

        }

    }

    public ButtonAction newButtonAction(final int imageRes, final Event event) {

        return new ButtonAction(imageRes) {
            @Override
            public void onClick() {
                event.onEvent();
            }
        } ;

    }

    public abstract class ButtonAction {

        public Bitmap image;
        public Location pos;


        private ButtonAction(int imageRes) {
            image = WaitingGameView.loadImage(imageRes);
        }


        public abstract void onClick();

        public boolean isTouched(double x, double y) {

            if (Math.sqrt(Math.pow(x - getX(), 2) + Math.pow(y - getY(), 2)) < actionButtonSize/2) {
                return true;
            }

            return false;

        }

        private double getX() {
            return pos.x + actionButtonSize/2;
        }

        private double getY() {
            return pos.y + actionButtonSize/2;
        }
    }
}

