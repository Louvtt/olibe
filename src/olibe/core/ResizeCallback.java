package olibe.core;

/** Resize callback (called when the window is resized) */
public abstract class ResizeCallback implements Callback {
    /**
     * Create a Resize Callback
     */
    protected ResizeCallback() {}

    /**
     * Called when the window is resized
     * @param w new width of the window
     * @param h new height of the window
     */
    public abstract void call(int w, int h);
}