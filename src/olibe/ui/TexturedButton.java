package olibe.ui;

import org.joml.Vector2f;
import org.joml.Vector2i;

import olibe.core.Window;
import olibe.render.*;

/**
 * Textured button
 */
public class TexturedButton extends Button {
    /** Image of the button */
    protected Image image;

    /**
     * Create a textured button
     * @param texture texture of the button
     * @param position position of the button (screen UV)
     */
    public TexturedButton(Texture texture, Vector2f position) {
        super();
        this.image = new Image(texture, position);

        this.position = position;
        final Vector2i winSize = Window.Get().getSize();
        this.size = new Vector2f(texture.getSize().x(), texture.getSize().y).div(winSize.x(), winSize.y());
    }

    @Override
    public void draw() {
        this.image.getShader().setUniform("uAlpha", (this.hovered?.8f:1f));
        this.image.draw();
    }

    @Override
    public void delete() {
        this.image.delete();
    }
}
