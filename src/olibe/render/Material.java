package olibe.render;

/**
 * Material class
 */
public abstract class Material {

    /**
     * Create a Material
     */
    public Material() {}

    /**
     * Set the materials uniform 
     * @param shader shader that needs material data
     */
    public abstract void setUniforms(Shader shader);
}