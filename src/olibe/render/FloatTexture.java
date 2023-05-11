package olibe.render;

import olibe.exception.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * Float data texture
 */
public class FloatTexture extends Texture {

    /**
     * Create a blank float data texture
     * @param width width of the texture
     * @param height height of the texture
     * @throws LWJGLException failed to create the OpenGL texture
     */
    public FloatTexture(int width, int height) 
    throws LWJGLException {
        super();
        createTexture(width, height, 3, GL_LINEAR, GL_RGB32F, GL_FLOAT, null);
    }
}
