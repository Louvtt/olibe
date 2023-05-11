package olibe.render;


/**
 * Color material
 */
public class ColorMaterial extends Material {
    /** Diffuse color */
    protected Color color;
    /**
     * Create a color material
     * @param color diffuse color
     */
    public ColorMaterial(Color color) {
        super();
        this.color = color;
    }
    
    @Override
    public void setUniforms(Shader shader) {
        shader.setUniform("cmaterial.color", color);
        shader.setUniform("activeMaterial", 1);
    }

    @Override
    public String toString() {
        return "ColorMaterial";
    }
}
