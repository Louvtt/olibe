package olibe.ui;

import org.joml.Vector2f;
import org.joml.Vector2i;

import olibe.render.*;
import olibe.core.*;
import olibe.exception.*;
import olibe.io.ShaderLibrary;

/**
 * Text Button
 */
public class TextButton extends Button {
    // Geometry

    /** Text */
    protected Text text;
    /** Background */
    protected Mesh back;

    // style

    /** Padding around the button (in pixels) */
    protected float padding;
    /** Base color of this button */
    protected Color color;
    /** Color of the button when hovered */
    protected Color hoverColor;

    // shaders

    /** Shader to render the text */
    protected Shader textShader;
    /** Shader to render the background */
    protected Shader screenUIShader;

    /**
     * Create a text button
     * @param text text of the button
     * @param font font of the text
     * @param position position of the button (xy center in screen UV)
     */
    public TextButton(String text, Font font, Vector2f position) {
        super();
        
        this.screenUIShader = ShaderLibrary.Get().getShader("screenUI");
        this.textShader = ShaderLibrary.Get().getShader("text");

        this.text = new Text(font, text, position);

        // default style

        this.color      = new Color(0f, 0f, 0f);
        this.hoverColor = new Color(0f, 0f, 0f, .5f);
        this.padding    = 4f;

        // compute rect

        Vector2i winSize = Window.Get().getSize();
        Vector2f textSize = new Vector2f(this.text.getComputedOutputSize()).add(2f*padding, 2f*padding);
        this.size     = new Vector2f(textSize).div(winSize.x(), winSize.y());
        this.position = new Vector2f(position).add(0f, this.size.y() * .5f);
    
        try {
            this.back = Mesh.Quad(textSize.x(), textSize.y());
        } catch (LWJGLException e) { 
            Log.Get().error("[Button] Failed to create the background quad : " + e.getMessage()); System.exit(-1); 
        }
    }

    /**
     * Draw the text button
     */
    public void draw() {
        this.screenUIShader.setUniform("uColor", (this.hovered? this.hoverColor : this.color));
        this.screenUIShader.setUniform("uPosition", this.position);
        this.back.draw(this.screenUIShader);        
        this.text.draw(this.textShader); 
    }

    @Override
    public void delete() {
        this.text.delete();
        this.back.delete();
    }
}
