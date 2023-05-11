package olibe.scene.components;

import java.security.InvalidParameterException;

import olibe.scene.Component;
import olibe.ui.*;

/**
 * Button component
 */
public class ButtonComponent implements Component {
    /** Button */
    protected Button button;

    /**
     * Create a button component
     * @param button button
     */
    public ButtonComponent(Button button) {
        if(button == null) {
            throw new InvalidParameterException("[ButtonComponent] button is null.");
        }

        this.button = button;
    }

    public void onUpdate(double delta) {
        this.button.update();
    }

    public void onRenderUI() {
        this.button.draw();
    }

    public void delete() {
        this.button.delete();
    }

    /**
     * Returns the button of this button component
     * @return the button of this button component
     */
    public Button getButton() {
        return this.button;
    }
}
