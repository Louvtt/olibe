package olibe.scene.components;

import java.security.InvalidParameterException;

import olibe.io.ShaderLibrary;
import olibe.render.Shader;
import olibe.scene.Component;
import olibe.ui.Text;

/**
 * Text component
 */
public class TextComponent implements Component {
    /** Text */
    protected Text text;
    /** Text shader */
    protected Shader shader;

    /**
     * Create a text component
     * @param text text
     */
    public TextComponent(Text text) {
        if(text == null) {
            throw new InvalidParameterException("[TextComponent] text is null.");
        }

        this.text = text;
        this.shader = ShaderLibrary.Get().getShader("text");
    }

    public void onRenderUI() {
        this.text.draw(this.shader);        
    }

    @Override
    public void delete() {
        text.delete();
    }
}
