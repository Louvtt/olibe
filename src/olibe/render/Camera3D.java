package olibe.render;

import org.joml.Vector3f;

public class Camera3D extends Camera {
    /** Position */
    protected Vector3f position;
    /** Forward vector */
    protected Vector3f forward;
    /** Up vector */
    protected Vector3f up;

    /** Field of view */
    protected float fov;

    /**
     * Create a 3D camera
     * @param width width of the screen
     * @param height height of the screen
     * @param fov field of view (degrees)
     */
    public Camera3D(int width, int height, float fov) {
        super(width, height);
        this.fov = fov;

        this.position = new Vector3f(0f, 0f, 6f);
        this.forward  = new Vector3f(0f, -1f, 0f);
        this.up       = new Vector3f(0f, 1f, 0f);
    }

    @Override
    public void updateMatrices() {
        projMatrix.setPerspective((float) Math.toRadians(fov), aspectRatio, NEAR_PLANE, FAR_PLANE);

        Vector3f target = new Vector3f(position).add(this.forward);
        viewMatrix.setLookAt(this.position, target, up);  
    }


    /**
     * Set the camera target position
     * @param targetPosition target position
     * @return this
     */
    public Camera lookAt(Vector3f targetPosition) {
        this.forward = new Vector3f(targetPosition).sub(this.position).normalize();

        updateMatrices();
        return this;
    } 

}
