package olibe.render;

import olibe.exception.*;

import static org.lwjgl.opengl.GL46.*;

import java.util.*;

/**
 * Dynamic OpenGL Vertex Buffer (VBO) wrapper class
 */
public class DynamicVertexBuffer extends VertexBuffer {
    /** Current vertex index */
    protected int currentVertexIndex;
    /** Vertex data size (in bytes) */
    protected int vertexDataSize;

    /**
     * Create a dynamic vertex buffer
     * @param maxVertexCount max vertices in this vertex buffer 
     * @param vertexDataSize per vertex data size (in bytes)
     * @throws LWJGLException failed to create the opengl buffer
     */
    public DynamicVertexBuffer(int maxVertexCount, int vertexDataSize) 
    throws LWJGLException {
        super(maxVertexCount, vertexDataSize);
        this.vertexDataSize = vertexDataSize;
    }

    /**
     * Set the data in this buffer and move the cursor in the buffer
     * @param data data to set
     * @param vertexCount number of vertices (in size of vertexDataSize) in data
     */
    public void setData(List<Float> data, int vertexCount) {
        this.setData(Buffer.asFloatArray(data), vertexCount);
    }

    /**
     * Set the data in this buffer and move the cursor in the buffer
     * @param data data to set
     * @param vertexCount number of vertices (in size of vertexDataSize) in data
     */
    public void setData(float[] data, int vertexCount) {
        if((this.currentVertexIndex + vertexCount) > this.vertexCount) {
            return;
        }

        this.bind();
        glBufferSubData(GL_ARRAY_BUFFER, currentVertexIndex * vertexDataSize, data);
        this.unbind();
        this.currentVertexIndex += vertexCount;
    }

    /**
     * Reset the cursor in the buffer
     */
    public void reset() {
        this.currentVertexIndex = 0;
    }

    /**
     * Gets the number of vertices actives in the buffer
     */
    public int getVertexCount() {
        return this.currentVertexIndex;
    }
}