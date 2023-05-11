package olibe.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Represent a 3D Camera
 */
public abstract class Camera {
    /** Projection matrix */
    protected Matrix4f projMatrix;
    /** View matrix */
    protected Matrix4f viewMatrix;

    /** Position */
    protected Vector3f position;
    /** Screen aspect ratio */
    protected float aspectRatio;
    /** Near plane distance */
    protected final static float NEAR_PLANE = .01f;
    /** Far plane distance */
    protected final static float FAR_PLANE = 1000f;

    /**
     * Create a camera
     * @param width width
     * @param height height
     * @param fov field of view (degrees)
     */
    public Camera(int width, int height) {
        this.viewMatrix = new Matrix4f();
        this.projMatrix = new Matrix4f();

        this.position = new Vector3f();
        this.aspectRatio = (float)width / height;

        updateMatrices();
    }

    /**
     * Update the camera matrices (proj and view)
     */
    public abstract void updateMatrices();

    /**
     * Move the camera by a certain amount
     * @param translation translation
     */
    public void move(Vector3f translation) {
        this.position.x += translation.x;
        this.position.y += translation.y;
        this.position.z += translation.z;

        updateMatrices();
    }
    
    /**
     * Set the camera position
     * @param newPosition new position
     * @return this
     */
    public Camera setPosition(Vector3f newPosition) {
        this.position = newPosition;

        updateMatrices();
        return this;
    }

    /**
     * Resize the camera
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        this.aspectRatio = (float) width / height;
        
        updateMatrices();
    }

    /**
     * Returns the projection matrix
     * @return the projection matrix
     */
    public Matrix4f getProjectionMat() {
        return this.projMatrix;
    }

    /**
     * Returns the view matrix
     * @return the view matrix
     */
    public Matrix4f getViewMat() {
        return this.viewMatrix;
    }   

    /**
     * Returns the camera position
     * @return the camera position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Update the camera
     * @param delta frame delta time (in seconds)
     */
    public void update(float delta) {}
}