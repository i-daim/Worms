package upc.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

public class MouseHandler extends MouseAdapter {

    private Point position = new Point(0, 0);
    private final HashSet<Integer> pressedButtons = new HashSet<>();

    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressedButtons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedButtons.remove(e.getButton());
    }

    public Point getPosition() {
        return position;
    }

    public boolean isPressed(int buttonCode) {
        return pressedButtons.contains(buttonCode);
    }
}
