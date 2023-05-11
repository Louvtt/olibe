package olibe.render;

import org.joml.Vector3f;
import org.joml.Matrix4f;

/**
 * Model class
 */
public class Model {
    /** Array of meshes */
    protected Mesh[] meshes;
    /** Textures */
    protected Texture[] textures;
    /** Position */
    protected Vector3f position;
    /** Model matrix */
    protected Matrix4f model;

    /**
     * Create a model from meshes and textures
     * @param meshes array of meshes
     * @param textures array of textures
     */
    public Model(Mesh[] meshes, Texture[] textures) {
        this.meshes   = meshes;
        this.textures = textures;
        this.model    = new Matrix4f().identity();
        this.position = new Vector3f();
    }

    /**
     * Change the model position
     * @param newPosition new position
     */
    public void setPosition(Vector3f newPosition) {
        this.position = newPosition;
        this.recalculateModelMatrix();
    }

    /**
     * Recalculate the model matrix
     */
    protected void recalculateModelMatrix() {
        this.model.identity()
            .translation(this.position);
    }

    /**
     * Draw this model using a shader
     * @param shader shader to use
     */
    public void draw(Shader shader) {
        if(textures != null) {
            for(int i = 0; i < this.textures.length; ++i) {
                shader.setUniform("uTex"+i, i);
                this.textures[i].bind(i);
            }
        }

        shader.setUniform("uModel", this.model);
        for(int i = 0; i < this.meshes.length; ++i) {
            this.meshes[i].draw(shader);
        }
    }

    
}
