package upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardHandler extends KeyAdapter {

    private final HashSet<Integer> pressedKeys = new HashSet<>();
    private final HashSet<Integer> justPressedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) {
            justPressedKeys.add(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public void clearJustPressed() {
        justPressedKeys.clear();
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isJustKeyPressed(int keyCode) {
        return justPressedKeys.contains(keyCode);
    }
}
