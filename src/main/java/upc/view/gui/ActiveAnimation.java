package upc.view.gui;

public class ActiveAnimation {

    private final Animation animation;
    private final int frameTime;
    private final long startTime;

    public ActiveAnimation(Animation animation, int frameTime) {
        this.animation = animation;
        this.frameTime = frameTime;
        this.startTime = System.currentTimeMillis();
    }
}
