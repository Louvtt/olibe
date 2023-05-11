package olibe.render;

import olibe.exception.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * OpenGL Depth data texture
 */
public class DepthTexture extends Texture {

    /**
     * Create a blank depth data texture
     * @param width width of the texture
     * @param height height of the texture
     * @throws LWJGLException failed to create the OpenGL texture
     */
    public DepthTexture(int width, int height) 
    throws LWJGLException {
        super();
        createTexture(width, height, 5, GL_NEAREST, GL_DEPTH_COMPONENT32, GL_FLOAT, null);
    }
}
