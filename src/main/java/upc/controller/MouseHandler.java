package upc.controller;

import upc.view.gui.GamePanel;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class MouseHandler extends MouseAdapter {

    private Point position = new Point(0, 0);
    private final HashSet<Integer> pressedButtons = new HashSet<>();
    private final HashSet<Integer> justPressedButtons = new HashSet<>();


////    EVENTS    ////
    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!pressedButtons.contains(e.getButton())) {
            justPressedButtons.add(e.getButton());
        }
        pressedButtons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedButtons.remove(e.getButton());
    }


////    FONCTIONS    ////
    public void clearJustPressed() {
        justPressedButtons.clear();
    }

    public boolean isMouseNearEdge(GamePanel gamePanel) {
        if (position == null) {
            return false;
        }
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM) + 50;

        // empêche la cam de se déplacer quand on appuie sur le bouton "Skip"
        if (position.y < 50) {
            return false;
        }

        return position.x > width - limit || position.x < limit
                || position.y > height - limit || position.y < limit;
    }

////    GETTERS    ////
    public Point getPosition() {
        return position;
    }

    public boolean isPressed(int buttonCode) {
        return pressedButtons.contains(buttonCode);
    }

    public boolean isJustPressed(int buttonCode) {
        return justPressedButtons.contains(buttonCode);
    }


}
