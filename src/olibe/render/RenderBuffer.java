package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.joml.Vector2i;

import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * OpenGL RenderBuffer (RBO) wrapper class
 */
public class RenderBuffer extends OpenGLObject {
    /** Type of attachment it is */
    protected AttachementType type;

    /**
     * Create a renderbuffer
     * @param type type of render buffer
     * @param size size of the renderbuffer
     * @throws LWJGLException error while creating the renderbuffer
     */
    public RenderBuffer(AttachementType type, Vector2i size) 
    throws LWJGLException {
        this.type = type;
        try(MemoryStack stack = stackPush()) {
            IntBuffer bufferID = stack.mallocInt(1);
            glGenRenderbuffers(bufferID);

            this.renderID = bufferID.get(0);
            if(this.renderID == 0) {
                throw new LWJGLException("Unable to generate renderbuffer");
            } else Log.Get().debug("Generated renderbuffer [id="+this.renderID+"]");
        }

        this.bind();
        this.resize(size);
        this.unbind();
    }

    /**
     * Returns the corresponding OpenGL storage type from its type
     * @return the corresponding OpenGL storage type from its type
     */
    protected int getGLStorageType() {
        switch(this.type) {
            case RENDERBUFFER_DEPTH:
                return GL_DEPTH_COMPONENT32;
            case RENDERBUFFER_STENCIL:
                return GL_STENCIL_INDEX8;
            case RENDERBUFFER_DEPTH_STENCIL:
                return GL_DEPTH32F_STENCIL8;
            case RENDERBUFFER_COLOR:
                return GL_RGB;
            case TEXTURE_COLOR:
                return GL_RGB;
            case TEXTURE_DEPTH:
            case TEXTURE_NORMAL:
            case TEXTURE_POSITION:
                return GL_RGB32F;
            default:
                break;
        }
        return 0; // fails
    }

    /**
     * Bind this render buffer
     */
    public void bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, this.renderID);  
    }

    /**
     * Unbind this render buffer
     */
    public void unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0);  
    }

    /**
     * Resize this render buffer
     * @param size new size
     */
    public void resize(Vector2i size) {
        this.bind();
        glRenderbufferStorage(GL_RENDERBUFFER, this.getGLStorageType(), size.x(), size.y());
        this.unbind();
    }

    /**
     * Delete this render buffer
     */
    public void delete() {
        if(this.renderID != 0) {
            this.unbind();
            glDeleteRenderbuffers(this.renderID);
            this.renderID = 0;
        }
    }
}
