package upc.assets;

import java.awt.image.BufferedImage;

public class ActiveAnimation {

    private final Animation animation;
    private final int frameTime;
    private final long startTime;
    private int frameIndex;
    private boolean loop;

    private int posX;
    private int posY;

    private boolean done;

    /**
     * Constructeur pour créer une animation active.
     * Permet d'enregistrer la date de création de l'animation,
     * pour pouvoir l'arrêter si l'animation ne se boucle pas.
     * Enregistre aussi l'index du frame à afficher pour faciliter l'utilisation dans le GamePanel.
     *
     * @param animation L'animation à afficher
     * @param frameTime La durée d'affichage de chaque frame (en ms)
     * @param loop Détermine si l'animation sera affichée en boucle
     */
    public ActiveAnimation(Animation animation, int frameTime, boolean loop,
                           int posX, int posY) {
        this.animation = animation;
        this.frameTime = frameTime;
        this.startTime = System.currentTimeMillis();
        this.loop = loop;
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Met à jour l'index du frame à afficher.
     * Met fin à l'animation si la durée d'affichage est dépassée.
     */
    public void update() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        frameIndex = (int) ((elapsedTime / frameTime) % animation.getFrames().size());

        if (!loop && elapsedTime >= (long) animation.getFrames().size() * frameTime) {
             done = true;
        }
    }

    public BufferedImage getCurrentFrame() {
        return animation.getFrames().get(frameIndex);
    }


////    GETTERS    ////
    public int getFrameTime() {
        return frameTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isDone() {
        return done;
    }

    public int getDrawX() {
        return posX - getCurrentFrame().getWidth() / 2;
    }

    public int getDrawY() {
        return posY - getCurrentFrame().getHeight() / 2;
    }
}
