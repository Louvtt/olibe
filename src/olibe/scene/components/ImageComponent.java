package olibe.scene.components;

import olibe.scene.Component;

import olibe.io.ShaderLibrary;
import olibe.ui.*;

/**
 * Image component
 */
public class ImageComponent implements Component {
    /** Image */
    protected Image image;

    /**
     * Create a image component
     * @param image image
     */
    public ImageComponent(Image image) {
        this.image = image;
        this.image.setShader(ShaderLibrary.Get().getShader("screenUI"));
    }

    @Override
    public void onRenderUI() {
        this.image.draw();
    }

    @Override
    public void delete() {
        this.image.delete();
    }
}
