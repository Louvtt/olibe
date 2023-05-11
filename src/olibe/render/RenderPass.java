package olibe.render;

import java.util.*;

import olibe.core.*;
import olibe.exception.LWJGLException;

/**
 * Render pass
 */
public class RenderPass {
    /** Shader of this pass */
    protected Shader shader;
    /** Frame buffer to render to */
    protected Framebuffer framebuffer;
    /** Needs depth cleared */
    protected boolean clearDepth;
    /** Name */
    protected String name;

    /**
     * Create a render pass
     * @param name name
     * @param shader shader
     * @param clearDepth needs depth cleared
     * @throws LWJGLException failed to create the framebuffer for this render pass
     */
    public RenderPass(String name, Shader shader, boolean clearDepth)
    throws LWJGLException {
        this.shader = shader;
        this.clearDepth = true;
        this.name = name;

        List<AttachementType> fbAttachements = new ArrayList<>();
        fbAttachements.add(AttachementType.TEXTURE_COLOR);
        fbAttachements.add(AttachementType.RENDERBUFFER_DEPTH_STENCIL);
        framebuffer = new Framebuffer(fbAttachements, Window.Get().getSize());
    }

    /**
     * Create a render pass (depth cleared by default)
     * @param name name
     * @param shader shader
     * @param attachementTypes attachments in the framebuffer
     * @throws LWJGLException failed to create the framebuffer for this render pass
     */
    public RenderPass(String name, Shader shader, List<AttachementType> attachementTypes)
    throws LWJGLException {
        this.shader = shader;
        this.clearDepth = true;
        this.name = name;

        framebuffer = new Framebuffer(attachementTypes, Window.Get().getSize());
    }

    /**
     * Begin the render pass
     */
    public void begin() {
        this.framebuffer.bind();
        Window.Get().beginFrame(this.clearDepth);
    }

    /**
     * End the render pass
     */
    public void end() {
        this.framebuffer.unbind();
    }

    /**
     * Returns the framebuffer of this render pass
     * @return the framebuffer of this render pass
     */
    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    /**
     * Returns the shader of this render pass
     * @return the shader of this render pass
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Returns the name of this render pass
     * @return the name of this render pass
     */
    public String getName() {
        return name;
    }
}
