package olibe.core;

import java.util.*;

import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

/** Class that manages keyboard and mouse inputs */
public class InputManager {

    /** States of the key and buttons */
    private enum State {
        /** First frame down */
        PRESSED,
        /** Down for more than 1 frame */
        DOWN,
        /** First frame up */ 
        RELEASED, 
        /** Up for more than 1 frame */
        UP;
    };
    /** States of the keys that has been inputed */
    protected Map<Integer, State> keyStates;
    /** Unique Instance of this class (Singleton) */
    protected static InputManager instance;
    /** States of the buttons that has been inputed */
    protected Map<Integer, State> buttonStates;
    /** Screen position of the mouse */
    protected Vector2f mousePos;

    /**
     * Returns the current instance of InputManager
     * @return the current instance of InputManager
     */
    public static InputManager Get() {
        if(instance == null) instance = new InputManager();
        return instance;
    }

    /**
     * Create an InputManager
     */
    protected InputManager() {
        this.keyStates = new HashMap<Integer,State>();
        this.buttonStates = new HashMap<Integer,State>();
        this.mousePos = new Vector2f();
    }

    /**
     * Simulate a key input
     * @param keyCode key code
     * @param status status of the key
     */
    public void simulateKeyInput(int keyCode, int status) {
        this.keyStates.put(keyCode, glfwStateToState(status));
    }

    /**
     * Simulate a button input
     * @param buttonCode button code
     * @param status status of the button
     */
    public void simulateButtonInput(int buttonCode, int status) {
        this.buttonStates.put(buttonCode, glfwStateToState(status));
    }

    /**
     * Sets the mouse position 
     * @param x x screen position
     * @param y y screen position
     */
    public void setMousePos(float x, float y) {
        this.mousePos.x = x;
        this.mousePos.y = y;
    }

    /**
     * Returns the screen mouse position
     * @return the screen mouse position
     */
    public Vector2f getMousePos() {
        return mousePos;
    }

    /**
     * Returns the normalized screen mouse position in [-1,1] range
     * @return the normalized screen mouse position in [-1,1] range
     */
    public Vector2f getScreenNormalizedMousePos() {
        return Window.Get().pixelToScreenUV(this.mousePos);
    }

    /**
     * Returns true if the key is up or released, false otherwise
     * @param keyCode key code
     * @return true if the key is up or released, false otherwise
     */
    public boolean isKeyUp(int keyCode) {
        if(!this.keyStates.containsKey(keyCode)) return true;
        else return this.keyStates.get(keyCode) == State.RELEASED
                 || this.keyStates.get(keyCode) == State.UP;
    }

    /**
     * Returns true if the key is pressed, false otherwise
     * @param keyCode key code
     * @return true if the key is pressed, false otherwise
     */
    public boolean isKeyPressed(int keyCode) {
        if(!this.keyStates.containsKey(keyCode)) return false;
        else return this.keyStates.get(keyCode) == State.PRESSED;
    }

    /**
     * Returns true if the key is down or pressed, false otherwise
     * @param keyCode key code
     * @return true if the key is down or pressed, false otherwise
     */
    public boolean isKeyDown(int keyCode) {
        if(!this.keyStates.containsKey(keyCode)) return false;
        else return this.keyStates.get(keyCode) == State.PRESSED
                 || this.keyStates.get(keyCode) == State.DOWN;
    }

    /**
     * Returns true if the key is released, false otherwise
     * @param keyCode key code
     * @return true if the key is released, false otherwise
     */
    public boolean isKeyReleased(int keyCode) {
        if(!this.keyStates.containsKey(keyCode)) return false;
        else return this.keyStates.get(keyCode) == State.RELEASED;
    }

    /**
     * Returns true if the button is up or released, false otherwise
     * @param buttonCode key code
     * @return true if the button is up or released, false otherwise
     */
    public boolean isButtonUp(int buttonCode) {
        if(!this.buttonStates.containsKey(buttonCode)) return true;
        else return this.buttonStates.get(buttonCode) == State.RELEASED
                 || this.buttonStates.get(buttonCode) == State.UP;
    }

    /**
     * Returns true if the button is pressed, false otherwise
     * @param buttonCode key code
     * @return true if the button is pressed, false otherwise
     */
    public boolean isButtonPressed(int buttonCode) {
        if(!this.buttonStates.containsKey(buttonCode)) return false;
        else return this.buttonStates.get(buttonCode) == State.PRESSED;
    }

    /**
     * Returns true if the button is down or pressed, false otherwise
     * @param buttonCode key code
     * @return true if the button is down or pressed, false otherwise
     */
    public boolean isButtonDown(int buttonCode) {
        if(!this.buttonStates.containsKey(buttonCode)) return false;
        else return this.buttonStates.get(buttonCode) == State.PRESSED
                 || this.buttonStates.get(buttonCode) == State.DOWN;
    }

    /**
     * Returns true if the button is released, false otherwise
     * @param buttonCode key code
     * @return true if the button is released, false otherwise
     */
    public boolean isButtonReleased(int buttonCode) {
        if(!this.buttonStates.containsKey(buttonCode)) return false;
        else return this.buttonStates.get(buttonCode) == State.RELEASED;
    }

    /**
     * Converts the GLFW button or key status to InputManager.State
     * @param glfwState glfw state
     * @return the corresponding InputManager.State
     */
    protected State glfwStateToState(int glfwState) {
        switch(glfwState) {
            case GLFW_RELEASE: return State.RELEASED;
            case GLFW_PRESS  : return State.PRESSED;
            case GLFW_REPEAT : return State.DOWN;
            default: break;
        }
        return State.UP;
    }

    /**
     * Called at the end of the frame to update pressed as down
     * and released as up
     */
    public void updateStates() {
        for(Integer keyCode : this.keyStates.keySet()) {
            switch(this.keyStates.get(keyCode)) {
                case PRESSED:
                    this.keyStates.put(keyCode, State.DOWN);
                    break;
                case RELEASED:
                    this.keyStates.put(keyCode, State.UP);
                    break;
                default: break;
            }
        }

        for(Integer buttonCode : this.buttonStates.keySet()) {
            switch(this.buttonStates.get(buttonCode)) {
                case PRESSED:
                    this.buttonStates.put(buttonCode, State.DOWN);
                    break;
                case RELEASED:
                    this.buttonStates.put(buttonCode, State.UP);
                    break;
                default: break;
            }
        }
    }

}