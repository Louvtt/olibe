package olibe.render;

import org.joml.Vector2f;

import olibe.core.Log;
import olibe.exception.LWJGLException;
import olibe.io.ShaderLibrary;

/**
 * Represent a 2D Sprite
 */
public class Sprite {
    /** Textured Quad */
    protected Mesh quad;
    /** Texture */
    protected Texture texture;
    /** Sprite position */
    protected Vector2f position;
    /** Sprite shader */
    protected Shader shader;

    /**
     * Create a sprite
     * @param texture texture to draw
     * @param position position of the sprite (pixel)
     */
    public Sprite(Texture texture, Vector2f position) {
        this.texture  = texture;
        this.position = position;
        this.shader   = ShaderLibrary.Get().getShader("sprite");

        this.quad = null;
        try {
            this.quad = Mesh.Quad(texture.getSize().x(), texture.getSize().y());
        } catch(LWJGLException e) {
            Log.Get().error("Failed to create the sprite quad: " + e.getMessage());
        }
    }

    /**
     * Draw the sprite
     */
    public void draw() {
        this.shader.bind();
        this.texture.bind(0);
        this.shader.setUniform("uTex", 0);
        this.shader.setUniform("uPosition", this.position);
        this.quad.draw(this.shader);
        this.shader.unbind();
    }

    /**
     * Set the shader for this sprite
     * @param shader the shader for this sprite
     */
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Returns the shader used to draw this sprite
     * @return the shader used to draw this sprite
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Delete the sprite background quad
     */
    public void delete() {
        this.quad.delete();
        this.quad = null;
    }

    /**
     * Returns the sprite position
     * @return the sprite position
     */
    public Vector2f getPosition() {
        return position;
    }
}
