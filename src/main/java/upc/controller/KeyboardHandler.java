package upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardHandler extends KeyAdapter {

    private final HashSet<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
