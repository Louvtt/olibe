package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import org.lwjgl.system.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * OpenGL Shader wrapper class
 */
public class Shader extends OpenGLObject {
    /** Current active shader (last bound shader) */
    protected static Shader activeShader;

    /**
     * Create a shader
     * @param vertexFilepath path to vertex shader file
     * @param fragmentFilepath path to fragment shader file
     * @throws LWJGLException failed to create vertex/fragment shader or program
     */
    public Shader(String vertexCode, String fragmentCode) 
    throws LWJGLException {
        renderID = this.createProgram();

        int vert = this.compileShader(GL_VERTEX_SHADER, vertexCode);
        int frag = this.compileShader(GL_FRAGMENT_SHADER, fragmentCode);
        
        // error checking
        glLinkProgram(renderID);
        if(glGetProgrami(renderID, GL_LINK_STATUS) == 0) {
            int len = glGetProgrami(renderID, GL_INFO_LOG_LENGTH);
            throw new LWJGLException("Error linking shader program : " + glGetProgramInfoLog(renderID, len));
        }

        glValidateProgram(renderID);
        if (glGetProgrami(renderID, GL_VALIDATE_STATUS) == 0) {
            int len = glGetProgrami(renderID, GL_INFO_LOG_LENGTH);
            Log.Get().debug("Warning validating Shader code: " + glGetProgramInfoLog(renderID, len));
        }

        this.disposeShaders(vert, frag);

        activeShader = this;

        Log.Get().debug("Create shader program [id="+renderID+"]");
    }

    /**
     * Compile a shader
     * @param type OpenGL shader type
     * @param shaderCode shader source code
     * @return the OpenGL shader ID
     * @throws LWJGLException error when creating or compiling the shader
     */
    protected int compileShader(int type, String shaderCode) 
    throws LWJGLException {
        int shader = glCreateShader(type);
        if(shader == 0) {
            throw new LWJGLException("Unable to create shader object.");
        }
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            int len = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
            throw new LWJGLException("Error compiling shader : " + glGetShaderInfoLog(shader, len));
        }

        glAttachShader(renderID, shader);

        return shader;
    }

    /**
     * Dispose fragment and vertex shader
     * @param vert vertex shader OpenGL id
     * @param frag fragment shader OpenGL id
     */
    protected void disposeShaders(int vert, int frag) {
        if(vert != 0) {
            glDeleteShader(vert);
        }
        if(frag != 0) {
            glDeleteShader(frag);
        }
    }

    /**
     * Create the OpenGL shader program
     * @return the create OpenGL program render ID
     * @throws LWJGLException error creating the program
     */
    protected int createProgram()
    throws LWJGLException {
		int program = glCreateProgram();
		if (program == 0)
			throw new LWJGLException("Error creating program");
		return program;
	}

    /**
     * Bind this shader program
     */
    public void bind() {
        glUseProgram(renderID);
    }

    /**
     * Unbind this shader program
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Delete this shader program
     */
    public void delete() {
        if(renderID != 0) {
            glDeleteProgram(renderID);
            renderID = 0;
        }
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        try(MemoryStack stack = stackPush()) {
            glProgramUniformMatrix4fv(renderID, location, false, value.get(stack.mallocFloat(16)));
        }
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, float value) {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        glProgramUniform1f(renderID, location, value);
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, Vector2f value) {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        glProgramUniform2f(renderID, location, value.x(), value.y());
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, Vector3f value) {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        glProgramUniform3f(renderID, location, value.x(), value.y(), value.z());
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, Color value) {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        glProgramUniform4f(renderID, location, value.r, value.g, value.b, value.a);
    }

    /**
     * Set the uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    public void setUniform(String name, int value)  {
        int location = glGetUniformLocation(renderID, name);
        if(location == -1) return;
        glProgramUniform1i(renderID, location, value);
    }

    /**
     * Returns the string representation of this shader
     * @return the string representation of this shader
     */
    public String toString() {
        return "Shader[id="+ this.renderID +"]";
    }

    /**
     * Make a shader the active shader
     * @param shader shader to make active 
     */
    public static void makeActive(Shader shader) {
        activeShader = shader;
    }

    /**
     * Returns the currently active shader
     * @return the currently active shader
     */
    public static Shader GetActiveShader() {
        return activeShader;
    }
}