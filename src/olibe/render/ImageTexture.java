package olibe.render;

import olibe.exception.*;
import olibe.core.Log;

import org.lwjgl.system.*;

import java.nio.*;
import java.io.IOException;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * Image texture
 */
public class ImageTexture extends Texture {
    /**
     * Load a image and create an OpenGL texture
     * @param filepath path to the image
     * @param flipVertically flip vertically the image
     * @throws LWJGLException failed to create the OpenGL texture
     * @throws IOException failed to open the image
     */
    public ImageTexture(String filepath, boolean flipVertically)
    throws LWJGLException, IOException {
        super();
        int loaded_width = 0;
        int loaded_height = 0;
        int loaded_channels = 0;
        ByteBuffer bb = null;
        try (MemoryStack stack = stackPush()) {
            IntBuffer widthPtr = stack.mallocInt(1);
            IntBuffer heightPtr = stack.mallocInt(1);
            IntBuffer channelsPtr = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(flipVertically); 
            bb = stbi_load(filepath, widthPtr, heightPtr, channelsPtr, 0);
            if(widthPtr.get(0)    == 0 
            || heightPtr.get(0)   == 0 
            || channelsPtr.get(0) == 0
            || bb == null) {
                throw new IOException("Failed to load texture ["+filepath+"]");
            }
            
            loaded_width = widthPtr.get(0);
            loaded_height = heightPtr.get(0);
            loaded_channels = channelsPtr.get(0);
            Log.Get().debug("Loaded texture ["+filepath+"]: width="+loaded_width+", height="+loaded_height+", channels="+loaded_channels);
        }
        this.createTexture(loaded_width, loaded_height, loaded_channels, GL_LINEAR, channelsToFormat(loaded_channels), GL_UNSIGNED_BYTE, bb);
    }

    /**
     * Load a image and create an OpenGL texture
     * @param filepath path to the image
     * @throws LWJGLException failed to create the OpenGL texture
     * @throws IOException failed to open the image
     */
    public ImageTexture(String filepath)
    throws LWJGLException, IOException {
        this(filepath, true);
    }
}
