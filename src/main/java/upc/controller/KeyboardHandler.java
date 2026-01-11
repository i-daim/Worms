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


    /**
     * Nettoie l'ensemble des touches "vient d'être pressées".
     * <p>
     * Cette méthode doit être appelée à la fin de chaque frame de jeu,
     * après avoir traité toutes les entrées clavier. Cela permet de détecter
     * correctement les pressions ponctuelles (une seule frame) et d'éviter
     * qu'une action se répète involontairement.
     * <p>
     * Typiquement appelée dans la méthode {@code update()} du contrôleur principal.
     */
    public void clearJustPressed() {
        justPressedKeys.clear();
    }

    /**
     * Vérifie si une touche est actuellement enfoncée (maintenue).
     * <p>
     * Cette méthode retourne true tant que la touche reste enfoncée,
     * ce qui est utile pour les actions continues comme les déplacements.
     *
     * @param keyCode Le code de la touche à vérifier (ex: {@link KeyEvent#VK_Q}).
     * @return true si la touche est actuellement enfoncée, false sinon.
     */
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /**
     * Vérifie si une touche vient d'être pressée cette frame.
     * <p>
     * Cette méthode retourne true une seule fois lors de la première pression,
     * puis false tant que la touche n'est pas relâchée puis pressée à nouveau.
     * C'est utile pour les actions ponctuelles comme le saut, la pause, ou la sauvegarde.
     * <p>
     * <b>Important :</b> {@link #clearJustPressed()} doit être appelé à chaque frame
     * pour que cette méthode fonctionne correctement.
     *
     * @param keyCode Le code de la touche à vérifier (ex: {@link KeyEvent#VK_SPACE}).
     * @return true si la touche vient d'être pressée cette frame, false sinon.
     */
    public boolean isJustKeyPressed(int keyCode) {
        return justPressedKeys.contains(keyCode);
    }
}
