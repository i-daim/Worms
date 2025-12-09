package upc.view.gui;

public class ActiveAnimation {

    private final Animation animation;
    private final int frameTime;
    private final long startTime;
    private int frameIndex;
    private boolean loop;

    private boolean done;

    public ActiveAnimation(Animation animation, int frameTime, boolean loop) {
        this.animation = animation;
        this.frameTime = frameTime;
        this.startTime = System.currentTimeMillis();
        this.loop = loop;
    public void update() {
        long timeIndex =  System.currentTimeMillis() / frameTime;

        frameIndex = (int) (timeIndex % animation.getFrames().size());

        int animationLength = animation.getFrames().size() * frameTime;
        if(!loop && timeIndex > startTime + animationLength) {
            done = true;
        }
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
}
