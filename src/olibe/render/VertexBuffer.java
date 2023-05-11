package olibe.render;

import olibe.exception.*;

import static org.lwjgl.opengl.GL46.*;

import java.util.*;

/**
 * OpenGL Vertex Buffer (VBO) wrapper class
 */
public class VertexBuffer extends Buffer {
    /** Number of vertices */
    protected int vertexCount;

    /**
     * Create a vertex buffer
     * @param vertices vertices data
     * @param vertexCount vertex count
     * @throws LWJGLException error while creating vertex buffer
     */
    public VertexBuffer(float[] vertices, int vertexCount) throws LWJGLException {
        super(GL_ARRAY_BUFFER);
        this.vertexCount = vertexCount;
        this.bind();
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        this.unbind();
    }

    /**
     * Create a vertex buffer
     * @param vertices vertices data
     * @param vertexCount vertex count
     * @throws LWJGLException error while creating vertex buffer
     */
    public VertexBuffer(List<Float> vertices, int vertexCount) throws LWJGLException {
        this(Buffer.asFloatArray(vertices), vertexCount);
    }

    /**
     * Create an empty dynamic vertex buffer
     * @param reservedVertexCount reserved/max vertex count
     * @param vertexDataSize per vertex size (in bytes)
     * @throws LWJGLException error while creating vertex buffer
     */
    protected VertexBuffer(int reservedVertexCount, int vertexDataSize) throws LWJGLException {
        super(GL_ARRAY_BUFFER);
        this.vertexCount = reservedVertexCount;
        this.bind();
        final int totalSize = reservedVertexCount * vertexDataSize;
        glBufferData(GL_ARRAY_BUFFER, totalSize, GL_DYNAMIC_DRAW);
        this.unbind();
    }

    /**
     * Returns the number of vertices in this vertex buffer
     * @return the number of vertices in this vertex buffer
     */
    public int getVertexCount() {
        return this.vertexCount;
    }
}
