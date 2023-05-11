package olibe.scene.components;

import java.security.InvalidParameterException;

import olibe.render.Model;
import olibe.render.Shader;
import olibe.scene.Component;

/**
 * Model component
 */
public class ModelComponent implements Component {

    /** Model */
    protected Model model;

    /**
     * Create a model component
     * @param model model
     */
    public ModelComponent(Model model) {
        this.model = model;
        if(this.model == null) {
            throw new InvalidParameterException("[ModelComponent] model is null.");
        }
    }

    public void onRender() {
        this.model.draw(Shader.GetActiveShader());        
    }

    @Override
    public void delete() {}
}
