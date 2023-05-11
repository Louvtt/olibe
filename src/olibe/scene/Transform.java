package olibe.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Transform class
 */
public class Transform {
    /** Position */
    protected Vector3f position;
    /** Scale */
    protected Vector3f scale;
    /** Euler rotation */
    protected Vector3f eulerRotations;
    /** Calculated model matrix */
    protected Matrix4f calculatedMatrix;

    /**
     * Create a base transform
     */
    public Transform() {
        this.position = new Vector3f();
        this.scale = new Vector3f(1f);
        this.eulerRotations = new Vector3f();
        this.calculatedMatrix = new Matrix4f();
        this.recalculate();
    }

    /**
     * Sets the position of this transform
     * @param position new position
     */
    public void setPosition(Vector3f position) {
        if(position == null) return;
        this.position = position;
        this.recalculate();
    }

    /**
     * Sets the scale of this transform
     * @param scale new scale
     */
    public void setScale(Vector3f scale) {
        if(scale == null) return;
        this.scale = scale;
        this.recalculate();
    }

    /**
     * Sets the euleur rotations angles of this transform
     * @param eulerRotations new euleur rotations angles
     */
    public void setEulerRotations(Vector3f eulerRotations) {
        if(eulerRotations == null) return;
        this.eulerRotations = eulerRotations;
        this.recalculate();
    }

    /**
     * Returns the euleur rotation of this transform
     * @return the euleur rotation of this transform
     */
    public Vector3f getEulerRotations() {
        return eulerRotations;
    }
    /**
     * Returns the position of this transform
     * @return the position of this transform
     */
    public Vector3f getPosition() {
        return position;
    }
    /**
     * Returns the scale of this transform
     * @return the scale of this transform
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Recalculate the matrix
     */
    protected void recalculate() {
        calculatedMatrix.identity()
            .rotateX(this.eulerRotations.x) 
            .rotateY(this.eulerRotations.y) 
            .rotateZ(this.eulerRotations.z)
            .scale(this.scale)
            .translate(this.position);
    }

    /**
     * Returns the matrix of this transform
     * @return the matrix of this transform
     */
    public Matrix4f getTransformMatrix() {
        return calculatedMatrix;
    }
}
