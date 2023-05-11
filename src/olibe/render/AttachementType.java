package olibe.render;

/**
 * Frame buffer attachments types
 */
public enum AttachementType {
    // RENDER BUFFERS (doesn't need to access data)
    /** Color attachment (renderbuffer) */
    RENDERBUFFER_COLOR(false),
    /** Stencil attachment (renderbuffer) */
    RENDERBUFFER_STENCIL(false), 
    /** Depth attachment (renderbuffer) */
    RENDERBUFFER_DEPTH(false, true), 
    /** Depth stencil attachment (renderbuffer) */
    RENDERBUFFER_DEPTH_STENCIL(false),

    // TEXTURES (want data)

    /** Color attachment (texture) */
    TEXTURE_COLOR(true),
    /** Position attachment (texture) */
    TEXTURE_POSITION(true, true),
    /** Normals attachment (texture) */
    TEXTURE_NORMAL(true, true),
    /** Depth attachment (texture) */
    TEXTURE_DEPTH(true, true);

    /** is this attachment a texture */
    private boolean isTexture;
    /** do this attachment needs float data */
    private boolean needsFloat;
    /**
     * Create an attachment type
     * @param isTexture is this attachment a texture
     * @param needsFloat do this attachment needs float data
     */
    private AttachementType(boolean isTexture, boolean needsFloat) {
        this.isTexture = isTexture;
        this.needsFloat = needsFloat;
    }
    /**
     * Create an attachment type (without float data)
     * @param isTexture is this attachment a texture
     */
    private AttachementType(boolean isTexture) {
        this(isTexture, false);
    }
    /**
     * Returns true if this attachment type is a texture, false otherwise
     * @return true if this attachment type is a texture, false otherwise
     */
    public boolean isTexture() {
        return this.isTexture;
    }
    /**
     * Returns true if this attachment type needs float data, false otherwise
     * @return true if this attachment type needs float data, false otherwise
     */
    public boolean needsFloat() {
        return this.needsFloat;
    }
}
