package olibe.scene.components;

import olibe.scene.Component;

import org.joml.Matrix4f;

import olibe.render.*;

/**
 * Sprite component
 */
public class SpriteComponent implements Component {
    /** Sprite */
    protected Sprite sprite;
    /** Model matrix */
    protected Matrix4f model;

    /**
     * Create a sprite component
     * @param sprite sprite
     */
    public SpriteComponent(Sprite sprite) {
        this.sprite = sprite;
        model = new Matrix4f().setTranslation(sprite.getPosition().x(), sprite.getPosition().y(), 0);
    }

    @Override
    public void onRender() {
        this.sprite.getShader().setUniform("uAlpha", 1f);
        this.sprite.getShader().setUniform("uModel", model);
        this.sprite.draw();
    }

    @Override
    public void delete() {
        this.sprite.delete();
    }
}
