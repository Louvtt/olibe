package olibe.ui;

/**
 * Rectangle class
 */
public class Rect {
    /** Position X */
    private float x;
    /** Position Y */
    private float y;
    /** Width (Size X) */
    private float width;
    /** Height (Size Y) */
    private float height;

    /**
     * Create a rect
     * @param x position x
     * @param y position y
     * @param width width (size x)
     * @param heigt heigt (size y)
     */
    public Rect(float x,float y,float width,float heigt) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = heigt;
    }

    /**
     * Returns the x position
     * @return the x position
     */
    public float x() {
        return this.x;
    }

    /**
     * Returns the y position
     * @return the y position
     */
    public float y() {
        return this.y;
    }

    /**
     * Returns the width
     * @return the width
     */
    public float width() {
        return this.width;
    }

    /**
     * Returns the height
     * @return the height
     */
    public float height() {
        return this.height;
    }

    /**
     * Returns the string representation of this rect
     * @return the string representation of this rect
     */
    public String toString() {
        return String.format("Rect: [x=%f,y=%f,w=%f,h=%f]", this.x, this.y, this.width, this.height);
    }
}
