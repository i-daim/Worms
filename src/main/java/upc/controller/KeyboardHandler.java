package upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardHandler extends KeyAdapter {

    /** Ensemble des codes de touches actuellement enfoncées (maintenues) */
    private final HashSet<Integer> pressedKeys = new HashSet<>();

    /** Ensemble des codes de touches qui viennent d'être pressées cette frame */
    private final HashSet<Integer> justPressedKeys = new HashSet<>();

    /**
     * Appelée automatiquement lorsqu'une touche est enfoncée.
     * <p>
     * Cette méthode détecte si c'est la première pression de la touche (pas une répétition)
     * et l'ajoute aux deux ensembles :
     * <ul>
     *     <li>{@code pressedKeys} : pour détecter les touches maintenues</li>
     *     <li>{@code justPressedKeys} : pour détecter les pressions ponctuelles</li>
     * </ul>
     *
     * @param e L'événement clavier contenant le code de la touche pressée.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressedKeys.contains(e.getKeyCode())) {
            justPressedKeys.add(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());
    }

    /**
     * Appelée automatiquement lorsqu'une touche est relâchée.
     * <p>
     * Cette méthode retire la touche de l'ensemble des touches maintenues.
     * L'état "vient d'être pressée" n'est pas modifié ici, il sera nettoyé
     * manuellement à la fin de la frame.
     *
     * @param e L'événement clavier contenant le code de la touche relâchée.
     */
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
