package olibe.render;

import java.util.*;

import olibe.core.Window;
import olibe.exception.LWJGLException;
import olibe.io.ShaderLibrary;

/**
 * Depth Render Pass
 */
public class DepthRenderPass extends RenderPass {

    /** Attachements for the depth render pass */
    private final static AttachementType[] ATTACHMENTS = {
        AttachementType.TEXTURE_COLOR,
        AttachementType.RENDERBUFFER_DEPTH_STENCIL
    };
    /** Last window clear color */
    private Color windowColorBeforePass;

    /**
     * Create a Depth Render Pass 
     */
    public DepthRenderPass() throws LWJGLException {
        super("depth", ShaderLibrary.Get().getShader("depth"), Arrays.asList(ATTACHMENTS));
    }
    
    @Override
    public void begin() {
        windowColorBeforePass = Window.Get().getLastClearColor();
        Window.Get().setClearColor(0, 0, 0, 0);
        this.framebuffer.bind();
        Window.Get().beginFrame(this.clearDepth);
    }

    @Override
    public void end() {
        super.end();
        Window.Get().setClearColor(windowColorBeforePass.r, windowColorBeforePass.g, windowColorBeforePass.b, windowColorBeforePass.a);
    }
}
