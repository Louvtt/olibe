package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.lwjgl.system.*;

import java.nio.*;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Abstract OpenGL buffer Object
 */
public abstract class Buffer extends OpenGLObject {
    /** OpenGL buffer type */
    private int bufferType;

    /**
     * Create a buffer
     * @param bufferType OpenGL buffer type
     * @throws LWJGLException failed to create the buffer
     */
    protected Buffer(int bufferType) throws LWJGLException {
        try(MemoryStack stack = stackPush()) {
			IntBuffer bufferID = stack.mallocInt(1); // int*

            glGenBuffers(bufferID);
            this.renderID = bufferID.get(0);
            if(this.renderID == 0) {
                throw new LWJGLException("Unable to generate buffer");
            } else Log.Get().debug("Generated buffer [id="+this.renderID+"]");
        }
        this.bufferType = bufferType;
    }

    /**
     * Bind this buffer
     */
    public void bind() {
        glBindBuffer(this.bufferType, this.renderID);
    }

    /**
     * Unbind this buffer
     */
    public void unbind() {
        glBindBuffer(this.bufferType, 0);
    }

    /**
     * Delete this buffer
     */
    public void delete() {
        if(this.renderID != 0) {
            this.unbind();
            glDeleteBuffers(this.renderID);
            this.renderID = 0;
        }
    }

    /**
     * Convert a List of Floats into a float array 
     * @param list list of float
     * @return float array
     */
    protected static float[] asFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        int i = 0;
        for(Float value : list) {
            array[i] = value;
            ++i;
        } 
        return array;
    }

    /**
     * Convert a List of Integers into a int array 
     * @param list list of Integers
     * @return int array
     */
    protected static int[] asIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        int i = 0;
        for(Integer value : list) {
            array[i] = value;
            ++i;
        } 
        return array;
    }

    /**
     * Returns the string representation of this buffer
     * @return the string representation of this buffer
     */
    public String toString() {
        return "Buffer[id="+ this.renderID +",type="+this.bufferType+"]";
    }
}
