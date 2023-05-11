package olibe.render;

import static org.lwjgl.opengl.GL46.*;

/**
 * Mesh attributes
 */
public enum MeshAttributeType {
    /** Single Float */
    FLOAT(1, Float.BYTES, GL_FLOAT),
    /** Two floats (Vec2) */
    FLOAT2(2, Float.BYTES, GL_FLOAT),
    /** Three floats (Vec3) */
    FLOAT3(3, Float.BYTES, GL_FLOAT),
    /** Four floats (Vec4/Color) */
    FLOAT4(4, Float.BYTES, GL_FLOAT);

    /** Number of components inside the attribute */
    private int count;
    /** Size the attribute (in bytes) */
    private int sizeof;
    /** Corresponding OpenGL Type */
    private int glType;

    /**
     * Create a Mesh attribute type
     * @param elementCount number of components
     * @param elementSizeof component type size (in bytes)
     * @param glType OpenGL component type
     */
    private MeshAttributeType(int elementCount, int elementSizeof, int glType) {
        this.count = elementCount;
        this.sizeof = elementSizeof * elementCount;
        this.glType = glType;
    }

    /**
     * Returns the number of components
     * @return the number of components
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns the total size (in bytes) of this attribute type
     * @return the total size (in bytes) of this attribute type
     */
    public int sizeof() {
        return this.sizeof;
    }

    /**
     * Returns the corresponding OpenGL type
     * @return the corresponding OpenGL type
     */
    public int getGLType() {
        return glType;
    }
}
