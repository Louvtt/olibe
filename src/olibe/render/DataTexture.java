package olibe.render;

import olibe.exception.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * Plain data texture
 */
public class DataTexture extends Texture {

    /**
     * Create a blank rgba data texture
     * @param width width of the texture
     * @param height height of the texture
     * @throws LWJGLException error while creating this data texture
     */
    public DataTexture(int width, int height) 
    throws LWJGLException {
        super();
        createTexture(width, height, 4, GL_LINEAR, GL_RGBA, GL_UNSIGNED_BYTE, null);
    }
}
