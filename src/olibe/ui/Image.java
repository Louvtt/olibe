package olibe.ui;

import org.joml.Vector2f;

import olibe.core.Log;
import olibe.render.*;
import olibe.exception.LWJGLException;
import olibe.io.ShaderLibrary;

/**
 * Represent an UI Image
 */
public class Image {
    /** Textured Quad */
    protected Mesh quad;
    /** Texture */
    protected Texture texture;
    /** Sprite position */
    protected Vector2f position;
    /** Sprite shader */
    protected Shader shader;

    /**
     * Create an image
     * @param texture texture to draw
     * @param position position of the image (screen UV)
     */
    public Image(Texture texture, Vector2f position) {
        this.texture  = texture;
        this.position = position;
        this.shader   = ShaderLibrary.Get().getShader("screenUITextured");

        this.quad = null;
        try {
            this.quad = Mesh.Quad(texture.getSize().x(), texture.getSize().y());
        } catch(LWJGLException e) {
            Log.Get().error("Failed to create the image quad: " + e.getMessage());
        }
    }

    /**
     * Draw the image
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
     * Set the shader for this image
     * @param shader the shader for this image
     */
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Returns the shader used to draw this image
     * @return the shader used to draw this image
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Delete the image background quad
     */
    public void delete() {
        this.quad.delete();
        this.quad = null;
    }

    /**
     * Returns the image position
     * @return the image position
     */
    public Vector2f getPosition() {
        return position;
    }
}
