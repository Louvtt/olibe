package olibe.render;

/**
 * Color class
 */
public class Color {
    /** Color channels */
    public float r,g,b,a;

    /**
     * Create a color
     * @param r red [0-1]
     * @param g green [0-1]
     * @param b blue [0-1]
     * @param a alpha [0-1]
     */
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Create an opaque color
     * @param r red [0-1]
     * @param g green [0-1]
     * @param b blue [0-1]
     */
    public Color(float r, float g, float b) {
        this(r,g,b,1f);
    }
}
