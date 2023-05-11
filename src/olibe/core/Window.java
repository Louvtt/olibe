package olibe.core;

import org.joml.Vector2i;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import olibe.render.Color;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

/** GLFW Window wrapper class */
public class Window {
    /** Address of the glfw native window */
    protected long nativeWindow;
    /** Title of the window */
    protected String title;
    /** Size of the window */
    protected Vector2i size;

    /** List of resize callbacks */
    protected List<ResizeCallback> resizeCallbacks;
    /** Active window instance */
    protected static Window active; 

    /** Last clear color */
    protected Color lastClearColor;

    /**
     * Create a window
     * @param title title of the window
     * @param width width of the window
     * @param height height of the window
     */
    public Window(String title, int width, int height) {
        this.size = new Vector2i(width, height);
        this.title = title;
        this.createInternal();
    }

    /**
     * Create a fullscreen windowed window
     * @param title title of the window
     */
    public Window(String title) {
        this.size = new Vector2i(400, 400);
        this.title = title;

        this.createInternal();

        // make it fullscreen
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center the window
        glfwSetWindowPos(
            nativeWindow,
            vidmode.width() / 2,
            vidmode.height() / 2
        );
        glfwSetWindowSize(
            nativeWindow,
            vidmode.width(),
            vidmode.height()
        );
        
        size = new Vector2i(vidmode.width(), vidmode.height());
    }

    /**
     * Internal glfw intialisation, window creation
     * and loading of OpenGL
     */
    protected void createInternal() {
        this.lastClearColor = new Color(0f,0f,0f,1f);
        GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
        else Log.Get().debug("GLFW Init success");

        this.createWindow(title);
        
        glfwMakeContextCurrent(nativeWindow);
		glfwSwapInterval(1); // VSYNC

		glfwShowWindow(nativeWindow);
        
        GL.createCapabilities();
        Log.Get().debug("GL Version: " + glGetString(GL_VERSION));
        this.setClearColor(.1f, .1f, .1f, 1.f);

        glViewport(0, 0, this.size.x(), this.size.y());
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); 

        resizeCallbacks = new ArrayList<ResizeCallback>();

        Window.active = this;
    }

    /**
     * Returns the active window
     * @return the active window
     */
    public static Window Get() {
        return Window.active;
    }

    /**
     * Returns the corresponding screen pixel coordinates from a window uv coordinate
     * @param uv uv coordinate (in [-1,1])
     * @return the corresponding screen pixel coordinates
     */
    public Vector2f screenUvToPixel(Vector2f uv) {
        return this.screenUvToPixel(uv.x(), uv.y());
    }

    /**
     * Returns the corresponding screen pixel coordinates from a window uv coordinate
     * @param u u coordinate (in [-1,1])
     * @param v v coordinate (in [-1,1])
     * @return the corresponding screen pixel coordinates
     */
    public Vector2f screenUvToPixel(float u, float v) {
        return new Vector2f(
            (u + 1f) * .5f * this.size.x(),
            (1f - v) * .5f * this.size.y()
        );
    }

    /**
     * Returns the corresponding uv coordinates from screen pixel coordinates
     * @param pixel pixel coordinate
     * @return the corresponding uv coordinates
     */
    public Vector2f pixelToScreenUV(Vector2f pixel) {
        return new Vector2f(
             (pixel.x() / this.size.x()) * 2f - 1f,
            -(pixel.y() / this.size.y()) * 2f + 1f
        );
    }

    /**
     * Register a Resize Callback to this window
     * @param callback callback to register
     */
    public void addCallback(ResizeCallback callback) {
        this.resizeCallbacks.add(callback);
    }

    /**
     * Returns the size of this window
     * @return the size of this window
     */
    public Vector2i getSize() {
        return this.size;
    }

    /**
     * Internal glfw window creation method 
     * @param title title of the window
     */
    protected void createWindow(String title) {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        // glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        nativeWindow = glfwCreateWindow(size.x, size.y, title, NULL, NULL);
        if (nativeWindow == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
        else Log.Get().debug("GLFW window creation success");

        glfwMakeContextCurrent(nativeWindow);
        
        // setup callback
        glfwSetKeyCallback(nativeWindow, (window, key, scancode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
                        
            InputManager.Get().simulateKeyInput(key, action);
		});
        glfwSetInputMode(nativeWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        glfwSetFramebufferSizeCallback(nativeWindow, (window, width, height) -> {
            Window.this.resize(width, height);
        });

        glfwSetCursorPosCallback(nativeWindow, (window, xpos, ypos) -> {
            InputManager.Get().setMousePos((float)xpos, (float)ypos);
        });
        
        glfwSetMouseButtonCallback(nativeWindow, (window, buttonCode, action,  mods) -> {
            InputManager.Get().simulateButtonInput(buttonCode, action);
        });

    }

    /**
     * Called when this window is resized
     * @param width new width of this window
     * @param height new height of this window
     */
    protected void resize(int width, int height) {
        this.size.x = width;
        this.size.y = height;
        glViewport(0, 0, width, height);
        for(ResizeCallback callback : resizeCallbacks) {
            callback.call(width, height);
        }
    }

    /**
     * Returns the last clear color of this window
     * @return the last clear color of this window
     */
    public Color getLastClearColor() {
        return this.lastClearColor;
    }
    /**
     * Sets the background color of the openGL viewport
     * @param r red [0,1]
     * @param g green [0,1]
     * @param b blue [0,1]
     * @param a alpha [0,1]
     */
    public void setClearColor(float r, float g, float b, float a) {
        this.lastClearColor = new Color(r,g,b,a);
        glClearColor(r,g,b,a);
    }

    /**
     * Returns the native glfw window
     * @return the native glfw window
     */
    public long getNativeWindow() {
        return this.nativeWindow;
    }

    /**
     * Returns true if this window should be closed, false otherwise
     * @return true if this window should be closed, false otherwise
     */
    public boolean shouldBeClosed() {
        return glfwWindowShouldClose(nativeWindow);
    }

    /**
     * Begin a new frame
     */
    public void beginFrame() {
        this.beginFrame(true);
    }

    /**
     * Begin a new frame
     * @param clearDepth true if depth is needed, false otherwise
     */
    public void beginFrame(boolean clearDepth) {
        if(clearDepth) {
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);
            glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        } else {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT); // clear the framebuffer
        }
    }

    /**
     * End a frame, update events and swap buffers
     */
    public void endFrame() {
        InputManager.Get().updateStates();
        glfwPollEvents();
        glfwSwapBuffers(nativeWindow); // swap the color buffers
    }

    /**
     * Destroy this window
     */
    public void destroy() {
        glfwDestroyWindow(nativeWindow);
        glfwTerminate();
    }
}
