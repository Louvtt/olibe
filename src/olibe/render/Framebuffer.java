package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.joml.Vector2i;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * OpenGL FrameBuffer (FBO) wrapper class
 */
public class Framebuffer extends OpenGLObject {
    /** List of render buffer attachments */
    protected List<RenderBuffer> rbAttachments;
    /** List of texture attachments */
    protected Map<String, Texture> texAttachments;

    /**
     * Create a framebuffer
     * @param attachmentTypes list fo attachments
     * @param size size of the screen
     * @throws LWJGLException failed to create attachments or framebuffer
     */
    public Framebuffer(List<AttachementType> attachmentTypes, Vector2i size) 
    throws LWJGLException {
        try(MemoryStack stack = stackPush()) {
            IntBuffer bufferID = stack.mallocInt(1);
            
            glGenFramebuffers(bufferID);
            this.renderID = bufferID.get(0);
            if(this.renderID == 0) {
                throw new LWJGLException("Unable to generate framebuffer");
            } else Log.Get().debug("Generated framebuffer [id="+this.renderID+"]");
        }
        
        this.bind();
        int colorAttachmentCount = 0;
        this.rbAttachments = new ArrayList<RenderBuffer>();
        this.texAttachments = new HashMap<>();
        for(AttachementType attachmentType : attachmentTypes) {
            int type = getGLAttachmentType(attachmentType);
            if(type == GL_COLOR_ATTACHMENT0) {
                type += colorAttachmentCount;
                colorAttachmentCount++;
            }

            if(attachmentType.isTexture()) {
                Texture tex = null;
                if(type == GL_DEPTH_COMPONENT) {
                    tex = new DepthTexture(size.x(), size.y());
                } else {
                    tex = attachmentType.needsFloat()?
                        new FloatTexture(size.x(), size.y())
                    :   new DataTexture(size.x(), size.y());
                }
                glFramebufferTexture2D(GL_FRAMEBUFFER, type, GL_TEXTURE_2D, tex.getRenderID(), 0);
                this.texAttachments.put(this.getGLTypeTexName(attachmentType), tex);
            } else {
                RenderBuffer attachement = new RenderBuffer(attachmentType, size);
                attachement.bind();
                glFramebufferRenderbuffer(GL_FRAMEBUFFER, type, GL_RENDERBUFFER, attachement.getRenderID());
                this.rbAttachments.add(attachement);
            }
        }
        this.resize(size);

        /* Bind color buffers correctly */
        int colorBuffers[] = new int[colorAttachmentCount];
        for(int i = 0; i < colorAttachmentCount; ++i) {
            colorBuffers[i] = GL_COLOR_ATTACHMENT0 + i;
        }
        glDrawBuffers(colorBuffers);

        this.complete();
        
        this.unbind();
        
    }

    /**
     * Returns the corresponding name from the attachment type
     * @param type attachment type
     * @return the corresponding name from the attachment type
     */
    protected String getGLTypeTexName(AttachementType type) {
        switch(type) {
            case TEXTURE_COLOR:
                return "color";
            case TEXTURE_POSITION:
                return "position";
            case TEXTURE_NORMAL:
                return "normal";
            case TEXTURE_DEPTH:
                return "depth";
            default: break;
        }
        return "other";
    }

    /**
     * Return the corresponding OpenGL type from an attachment type
     * @param type attachment type
     * @return the corresponding OpenGL type from an attachment type
     */
    protected int getGLAttachmentType(AttachementType type) {
        switch(type) {
            case TEXTURE_DEPTH:
            case RENDERBUFFER_DEPTH:
                return GL_DEPTH_ATTACHMENT;
            case RENDERBUFFER_STENCIL:
                return GL_STENCIL_ATTACHMENT;
            case RENDERBUFFER_DEPTH_STENCIL:
                return GL_DEPTH_STENCIL_ATTACHMENT;
            case TEXTURE_COLOR:
            case TEXTURE_POSITION:
            case TEXTURE_NORMAL:
            case RENDERBUFFER_COLOR:
                return GL_COLOR_ATTACHMENT0;
            default:
                break;
        }
        return 0; // fails
    }

    /**
     * Test the completion of the framebuffer
     * @throws LWJGLException framebuffer is not complete
     */
    protected void complete() 
    throws LWJGLException {
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE) {
            throw new LWJGLException(toString() + " have an invalid status [status="+this.statusToString(status)+"]");
        }
    }

    /**
     * Return the corresponding status string of the framebuffer
     * @param status OpenGL status code
     * @return the corresponding status string of the framebuffer
     */
    protected String statusToString(int status) {
        switch(status) {
            case GL_FRAMEBUFFER_UNDEFINED: return "GL_FRAMEBUFFER_UNDEFINED";
            case GL_FRAMEBUFFER_COMPLETE: return "GL_FRAMEBUFFER_COMPLETE";
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: return "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT";
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT: return "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT";
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER: return "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER";
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: return "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER";
            case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE: return "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE";
            case GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS: return "GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS";
            case GL_FRAMEBUFFER_UNSUPPORTED: return "GL_FRAMEBUFFER_UNSUPPORTED";
            default: break;
        }
        return "GL_UNKNOWN";
    }

    /**
     * Resize the framebuffer
     * @param size new size
     * @throws LWJGLException failed to resize the attachments
     */
    public void resize(Vector2i size) 
    throws LWJGLException {
        for(Texture tex : this.texAttachments.values()) {
            tex.resize(size);
        }
        for(RenderBuffer attachment : this.rbAttachments) {
            attachment.resize(size);
        }

        this.complete();
    }

    /**
     * Bind this framebuffer
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.renderID);
    }

    /**
     * Unind this framebuffer
     */
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Returns the textures attachments map 
     * @return the textures attachments map
     */
    public Map<String, Texture> getTextures() {
        return this.texAttachments;
    }

    /**
     * Delete the framebuffer and its attachments
     */
    public void delete() {
        this.unbind();
        for(Texture tex : this.texAttachments.values()) {
            tex.delete();
        }
        this.texAttachments.clear();

        for(RenderBuffer attachment : this.rbAttachments) {
            attachment.delete();
        }
        this.rbAttachments.clear();
        glDeleteFramebuffers(this.renderID);
    }

    @Override
    public String toString() {
        return "Framebuffer[id="+this.renderID+"]";
    }
}