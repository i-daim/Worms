package upc.controller;

import upc.view.gui.GamePanel;

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

    public boolean isMouseNearEdge(GamePanel gamePanel) {
        if (position == null) {
            return false;
        }
        int width = gamePanel.getWidth();
        int height = gamePanel.getHeight();
        int limit = (int) (50 * GamePanel.ZOOM);

        return position.x > width - limit || position.x < limit
                || position.y > height - limit || position.y < limit;
    }
}
