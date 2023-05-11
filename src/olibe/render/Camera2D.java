package olibe.render;

import org.joml.Vector2i;

import olibe.core.Window;

public class Camera2D extends Camera {

    public Camera2D(int width, int height) {
        super(width, height);
    }

    @Override
    public void updateMatrices() {
        Vector2i winSize = Window.Get().getSize();
        projMatrix.setOrtho2D(-winSize.x() * .5f, winSize.x() * .5f, -winSize.y() * .5f, winSize.y() * .5f);

        viewMatrix.setTranslation(this.position);
    }
    
}
