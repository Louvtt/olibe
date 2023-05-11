package olibe.render;

import olibe.core.Log;
import olibe.exception.*;

import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * OpenGL Mesh abstraction (VAO wrapper) class 
 */
public class Mesh {
    /** OpenGL Vertex array (VAO) render ID  */
    protected int vertexArrayID;
    /** Number of Vertices in the mesh */
    protected int vertexCount;
    /** Vertex buffer */
    protected VertexBuffer vbo;
    /** Index buffer (optionnal) */
    protected IndexBuffer ebo;
    /** Material */
    protected Material material;

    /**
     * Create a mesh from a Vertex buffer and index buffer
     * @param vbo vertex buffer
     * @param ebo index buffer (can be <code>null</code>)
     * @param attributes mesh attributes
     * @throws LWJGLException failed to create/setup the VAO
     * @throws IllegalArgumentException vbo can't be null
     */
    public Mesh(VertexBuffer vbo, IndexBuffer ebo, MeshAttributeType[] attributes) 
    throws LWJGLException, IllegalArgumentException {
        if(vbo == null) 
            throw new IllegalArgumentException("VBO can't be null.");
        
        this.vbo = vbo;
        this.ebo = ebo;

        try (MemoryStack stack = stackPush()) {
            IntBuffer vertexArray = stack.mallocInt(1);
            glGenVertexArrays(vertexArray);
            vertexArrayID = vertexArray.get(0);
        }        
        if (vertexArrayID == 0) {
            throw new LWJGLException("Error while creating vertex array");
        }
        
        Log.Get().debug("Create vertex array [id="+vertexArrayID+"]");

        glBindVertexArray(vertexArrayID);
        this.vbo.bind();
        if(this.ebo != null) this.ebo.bind();
        
        // set layout
        bindAttributes(attributes);	
        glBindVertexArray(0);

        material = null;
    }

    /**
     * Create a Mesh from a vertex buffer
     * @param vbo vertex buffer
     * @param attributes mesh attributes
     * @throws LWJGLException error while creating/setup the VAO
     * @throws IllegalArgumentException vbo can't be null
     */
    public Mesh(VertexBuffer vbo, MeshAttributeType[] attributes)
    throws LWJGLException, IllegalArgumentException {
        this(vbo, null, attributes);
    }

    /**
     * Change the material
     * @param material new material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Bind the mesh attributes to the vertex array
     * @param attributes mesh attributes to bind
     */
    protected void bindAttributes(MeshAttributeType[] attributes) {
        // calculate stride
        int stride = 0;
        for(int i = 0; i < attributes.length; ++i) {
            stride += attributes[i].sizeof();
        }

        // bind each one
        int offset = 0;
        for(int i = 0; i < attributes.length; ++i) {
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, attributes[i].getCount(), attributes[i].getGLType(), false, stride, offset);
            offset += attributes[i].sizeof();	
        }
    }

    /**
     * Draw this mesh
     * @param shader shader to use
     */
    public void draw(Shader shader) {
        shader.bind();

        if(material != null) {
            material.setUniforms(shader);
        }

        glBindVertexArray(vertexArrayID);
        // this.vbo.bind();
        if(this.ebo != null) {
            // this.ebo.bind();
            glDrawElements(GL_TRIANGLES, this.ebo.getIndexCount(), GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, this.vbo.getVertexCount());
        }
        glBindVertexArray(0);

        shader.unbind();
    }

    /**
     * Returns the vertex array ID
     * @return the vertex array ID
     */
    public int getVertexArrayID() {
        return this.vertexArrayID;
    }

    /**
     * Delete this mesh and its OpenGL objects (vbo/ebo/vao)
     */
    public void delete() {
        if(this.vbo != null) {
            this.vbo.delete();
            this.vbo = null;
        }

        if(this.ebo != null) {
            this.ebo.delete();
            this.ebo = null;
        }


        if(this.vertexArrayID != 0) {
            glBindVertexArray(0);
            glDeleteVertexArrays(this.vertexArrayID);
            this.vertexArrayID = 0;
        }
    }

    /**
     * Create a quad 
     * @param size size of the quad
     * @return created quad
     * @throws LWJGLException failed to create the quad
     */
    public static Mesh Quad(float size) 
    throws LWJGLException {
        return Mesh.Quad(size, size);
    }

    /**
     * Create a quad 
     * @param width width of the quad
     * @param height height of the quad
     * @return created quad
     * @throws LWJGLException failed to create the quad
     */
    public static Mesh Quad(float width, float height) 
    throws LWJGLException {
        final float halfX = width  * .5f;
        final float halfY = height * .5f;
        final float[] quadVertices = {
            // first tri
            // pos            // tex
            -halfX, -halfY, 0f, 0f, 0f, // top left
             halfX, -halfY, 0f, 1f, 0f, // top right
             halfX,  halfY, 0f, 1f, 1f, // bottom right
            // second tri
             halfX,  halfY, 0f, 1f, 1f, // bottom right
            -halfX,  halfY, 0f, 0f, 1f, // bottom left
            -halfX, -halfY, 0f, 0f, 0f  // top left
        };
        final VertexBuffer quadVbo = new VertexBuffer(quadVertices, 6);
        final MeshAttributeType[] attribs = {
            MeshAttributeType.FLOAT3,
            MeshAttributeType.FLOAT2
        };
        return new Mesh(quadVbo, attribs);
    }

    /**
     * Returns the vertex buffer
     * @return the vertex buffer
     */
    public VertexBuffer getVertexBuffer() {
        return this.vbo;
    }
}