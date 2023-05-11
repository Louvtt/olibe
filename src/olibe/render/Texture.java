package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.joml.Vector2i;
import org.lwjgl.system.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

import java.nio.*;

/**
 * OpenGL Texture wrapper class
 */
public class Texture extends OpenGLObject {
    /** Pixel size */
    protected Vector2i size;
    /** Channels */
    protected int channels;
    /** Last bound location */
    protected int lastLocation;
    /** OpenGL internal type */
    protected int internalType;
    /** OpenGL internal Format */
    protected int internalFormat;

    /**
     * Create a blank texture
     */
    protected Texture() {
        this.size = new Vector2i(0, 0);
        this.channels = 0;
        this.internalFormat = GL_RGB;
        this.internalType = GL_UNSIGNED_BYTE;
        this.lastLocation = 0;
        this.renderID = 0;
    }

    /**
     * Create a texture
     * @param width width
     * @param height height
     * @param channels channels
     * @param filter OpenGL texture filter (GL_LINEAR/GL_NEAREST)
     * @param internalFormat internal OpenGL format
     * @param internalType internal OpenGL type
     * @param data raw pixel data
     * @throws LWJGLException failed to create the texture
     */
    protected void createTexture(int width, int height, int channels, int filter, int internalFormat, int internalType, ByteBuffer data)
    throws LWJGLException {
        this.size.x = width;
        this.size.y = height;
        this.channels = channels;
        this.internalFormat = internalFormat;
        this.internalType   = internalType;

        try (MemoryStack stack = stackPush()) {
            IntBuffer textureID = stack.mallocInt(1);
            glGenTextures(textureID);
            renderID = textureID.get(0);
            if(this.renderID == 0) {
                throw new LWJGLException("Unable to generate texture");
            } else Log.Get().debug("Generated texture [id="+this.renderID+"]");
        }

        this.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glTexImage2D(GL_TEXTURE_2D,0, this.internalFormat, size.x(), size.y(), 0, channelsToFormat(channels), this.internalType, data);
        this.unbind();
    }

    /**
     * Returns the corresponding format for a specified number of channels
     * @param channels number of channels
     * @return the corresponding format for a specified number of channels
     */
    protected int channelsToFormat(int channels) {
        switch(channels) {
            case 1: return GL_RED;
            case 2: return GL_RG;
            case 3: return GL_RGB;
            case 4: return GL_RGBA;
            case 5: return GL_DEPTH_COMPONENT;
            default: break;
        }
        return GL_RED;
    }

    /**
     * Bind this texture to GL_TEXTURE0
     */
    public void bind() {
        this.bind(0);
    }

    /**
     * Bind this texture to a specified location
     * @param location new location
     */
    public void bind(int location) {
        this.lastLocation = location;
        glActiveTexture(GL_TEXTURE0 + location);
        glBindTexture(GL_TEXTURE_2D, this.renderID);
    }

    /**
     * Unbind this texture from its last bound location
     */
    public void unbind() {
        glActiveTexture(GL_TEXTURE0 + this.lastLocation);
        glBindTexture(GL_TEXTURE_2D, this.renderID);
    }

    /**
     * Returns this texture size
     * @return this texture size
     */
    public Vector2i getSize() {
        return this.size;
    }

    /**
     * Resize this texture
     * @param size new size
     */
    public void resize(Vector2i size) {
        this.bind();
        this.size = size;
        glTexImage2D(GL_TEXTURE_2D, 0, this.internalFormat, size.x(), size.y(), 0, channelsToFormat(channels), this.internalType, 0);
        this.unbind();
    }

    /**
     * Delete this texture
     */
    public void delete() {
        if(this.renderID != 0) {
            this.unbind();
            glDeleteTextures(this.renderID);
            this.renderID = 0;
        }
    }

    /**
     * Returns the string representation of this texture
     * @return the string representation of this texture
     */
    public String toString() {
        return "Texture[id="+ this.renderID +"]";
    }
}
