package olibe.ui;

import java.util.*;

import org.joml.Vector2f;
import olibe.core.*;

/** Represent a 2D ui button */
public abstract class Button {
    // data

    /** Position */
    protected Vector2f position;
    /** Size */
    protected Vector2f size;

    // states

    /** Callback for click events */
    public interface Callback {
        /** called when the button is clicked */
        public void onClick();
    }

    /** True if the button is hovered, false otherwise */
    protected boolean hovered;
    /** True if the button is clicked, false otherwise */
    protected boolean clicked;
    /** List of callbacks when the button is clicked */
    protected List<Callback> callbacks;

    /**
     * Create a button
     */
    public Button() {
        this.hovered = false;
        this.clicked = false;
        this.callbacks = new LinkedList<Button.Callback>();
    }

    /**
     * Update button state (hover and click)
     */
    public void update() {
        final Vector2f mousePos = InputManager.Get().getScreenNormalizedMousePos();
        this.hovered = false;
        this.clicked = false;
        if(this.position.x() - this.size.x() < mousePos.x() 
        && mousePos.x() < this.position.x() + this.size.x()
        && this.position.y() - this.size.y() < mousePos.y() 
        && mousePos.y() < this.position.y() + this.size.y()) {
            this.hovered = true;
            if(InputManager.Get().isButtonPressed(0)) { // LEFT MOUSE BUTTON
                this.clicked = true;
                for(Callback c : this.callbacks) {
                    c.onClick();
                }
            } 
        }
    }

    /**
     * Returns true if the button has been clicked last frame
     * @return true if the button has been clicked last frame
     */
    public boolean isClicked() {
        return this.clicked;
    }

    /**
     * Draw the button (its background and text)
     */
    public abstract void draw();

    /** 
     * Delete the button graphics objects
     */
    public abstract void delete();

    /**
     * Register a callback to this button
     * @param callback the callback to register
     * @return this
     */
    public Button registerCallback(Button.Callback callback) {
        this.callbacks.add(callback);
        return this;
    }
}