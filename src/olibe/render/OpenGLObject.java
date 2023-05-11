package olibe.render;

/**
 * Base of every OpenGL wrapper class
 */
public abstract class OpenGLObject {
    /** OpenGL render ID */
    protected int renderID;
    /** Create a blank OpenGL Object */
    protected OpenGLObject() {
        this.renderID = 0;
    }

    /** Bind this OpenGL object */
    public abstract void bind();

    /** Unind this OpenGL object */
    public abstract void unbind();

    /** Delete this OpenGL object */
    public abstract void delete();

    /**
     * Returns the OpenGL render ID
     * @return the OpenGL render ID
     */
    public int getRenderID() {
        return renderID;
    }
}
