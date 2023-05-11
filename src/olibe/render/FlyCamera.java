package olibe.render;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;
import org.joml.Vector2f;

import olibe.core.*;

/**
 * Fly Camera 
 * - see: https://learnopengl.com/Getting-started/Camera
 */
public class FlyCamera extends Camera3D {
    /** Rotation of the camera */
    protected double yaw, pitch;
    /** Last mouse pos */
    protected Vector2f lastMousePos;
    /** Mouse controls toggle */
    protected boolean enableOrbitControls;

    /**
     * Create a Fly camera
     * @param width screen of the texture
     * @param height screen of the texture
     * @param fov field of view (in degrees)
     */
    public FlyCamera(int width, int height, float fov) {
        super(width, height, fov);

        this.yaw   = -90.0;
        this.pitch = 0;
        this.enableOrbitControls = true;
        this.lastMousePos = new Vector2f();

        update(0);
    }

    @Override
    public void update(float delta) {
        final float cameraSpeed = 25f * delta;
        final Vector3f fw = new Vector3f(forward).mul(cameraSpeed);
        final Vector3f right = new Vector3f(forward).cross(up).mul(cameraSpeed);
        
        // walk around
        if (InputManager.Get().isKeyDown(GLFW_KEY_W))
            position.add(fw);
        if (InputManager.Get().isKeyDown(GLFW_KEY_S))
            position.sub(fw);
        if (InputManager.Get().isKeyDown(GLFW_KEY_A))
            position.sub(right);
        if (InputManager.Get().isKeyDown(GLFW_KEY_D))
            position.add(right);
        if(InputManager.Get().isKeyDown(GLFW_KEY_SPACE))
            position.add(up);
        if(InputManager.Get().isKeyDown(GLFW_KEY_LEFT_SHIFT))
            position.sub(up);

        if(InputManager.Get().isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if(glfwGetInputMode(Window.Get().getNativeWindow(), GLFW_CURSOR) == GLFW_CURSOR_NORMAL) {
                glfwSetInputMode(Window.Get().getNativeWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            } else {
                glfwSetInputMode(Window.Get().getNativeWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        }

        // orbit
        final double sensivity = .1;
        final Vector2f mousePos = InputManager.Get().getMousePos();
        yaw   += (double)(mousePos.x() - lastMousePos.x()) * sensivity; // x offset
        pitch += (double)(lastMousePos.y() - mousePos.y()) * sensivity; // y offset
        lastMousePos.x = mousePos.x;
        lastMousePos.y = mousePos.y;
        
        if(pitch > 89)  pitch = 89;
        if(pitch < -89) pitch = -89;
        
        if(glfwGetInputMode(Window.Get().getNativeWindow(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            // update forward
            Vector3f direction = new Vector3f();
            direction.x = (float) ((float) Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            direction.y = (float) Math.sin(Math.toRadians(pitch));
            direction.z = (float) ((float) Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            forward = direction.normalize();
        }
        updateMatrices();
    }
}
