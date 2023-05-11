package olibe.render;

import olibe.exception.*;

import static org.lwjgl.opengl.GL46.*;

import java.util.List;

/**
 * OpenGL Index buffer (EBO) wrapper class
 */
public class IndexBuffer extends Buffer {
    /** Numberof indices */
    protected int indexCount;

    /**
     * Create an index buffer
     * @param indices array of indices
     * @throws LWJGLException error while creating the index buffer
     */
    public IndexBuffer(int[] indices) throws LWJGLException {
        super(GL_ELEMENT_ARRAY_BUFFER);
        this.indexCount = indices.length;
        this.bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        this.unbind();
    }

    
    /**
     * Create an index buffer
     * @param indices list of indices
     * @param indexCount number of indices
     * @throws LWJGLException error while creating the index buffer
     */
    public IndexBuffer(List<Integer> indices, int indexCount) throws LWJGLException {
        super(GL_ELEMENT_ARRAY_BUFFER);
        this.indexCount = indexCount;
        this.bind();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, asIntArray(indices), GL_STATIC_DRAW);
        this.unbind();
    }

    /**
     * Returns the number of indices in this index buffer
     * @return the number of indices in this index buffer
     */
    public int getIndexCount() {
        return this.indexCount;
    }
}
