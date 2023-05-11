package olibe.ui;

import java.util.*;

import org.joml.Vector2f;
import org.joml.Vector3f;

import olibe.core.Log;
import olibe.exception.LWJGLException;
import olibe.render.*;
import olibe.ui.Font.CharInfo;

/**
 * Text class
 */
public class Text {
    /** Font */
    protected Font font;
    /** Text content */
    protected String textContent;
    /** Position in uv screen coordinates */
    protected Vector2f position;
    /** Text size (factor) */
    protected float textSize;
    /** Computed output size (in pixels)  */
    protected Vector2f computedOutputSize;
    /** True for centered, otherwise it's bottom-left aligned */
    protected boolean centered;
    /** Size of each vertex in the vbo (in bytes) */
    protected static final int VERTEX_DATA_SIZE = 5 * 4; // 5 floats (pos3d, texCoords)
    /** Max length of the text content */
    protected static final int TEXT_MAX_LENGTH  = 512; // 5 floats (pos3d, texCoords)
    /** Mesh of the text */
    protected Mesh mesh;
    /** Color of the text */
    protected Color color;
    /** Attributes of the mesh */
    protected static final MeshAttributeType[] TEXT_MESH_ATTRIBUTES = {
        MeshAttributeType.FLOAT3, // pos
        MeshAttributeType.FLOAT2  // tex
    };

    /**
     * Create a text
     * @param font font of the text
     * @param textContent text content
     * @param position position in uv screen coordinates
     */
    public Text(Font font, String textContent, Vector2f position)
    {
        this.font = font;
        this.textContent = textContent;
        this.position = position;
        this.textSize = 1;
        this.centered = true;
        this.color = new Color(1,1,1); // white by default
        this.computedOutputSize = new Vector2f(0f,0f);

        this.buildMesh();
    }

    /**
     * Change text size
     * @param textSize text size (factor)
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
     
        this.updateMesh();
    }

    /**
     * Change the text content
     * @param textContent new text content
     */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    
        this.updateMesh();
    }

    /**
     * Initialize the mesh of this text
     */
    protected void buildMesh() {
        try {
            VertexBuffer vertexBuffer = new DynamicVertexBuffer(TEXT_MAX_LENGTH * 4, VERTEX_DATA_SIZE);
            Mesh newMesh = new Mesh(vertexBuffer, null, TEXT_MESH_ATTRIBUTES);
            this.mesh = newMesh;

            this.updateMesh();
        } catch (LWJGLException e) {
            Log.Get().error("Error build text mesh");
        }
    }

    /**
     * Update this text mesh (recreate vertices from start)
     */
    protected void updateMesh() {
        if(this.mesh == null) return; // no mesh ?

        List<Float> vertices = new ArrayList<Float>();
        int vertexCount = 0;
        Vector3f currentPos = new Vector3f(0f, 0f, 0f);

        float sizeX = 0f;
        float lineSizeX = 0f;
        this.computedOutputSize.y = this.font.getLineHeight() * this.textSize;
        if(centered) {
            for(int i = 0; i < this.textContent.length(); ++i) {
                if(this.textContent.charAt(i) == '\n') {
                    lineSizeX = 0f;
                    this.computedOutputSize.y += this.font.getLineHeight() * this.textSize;
                    continue;
                }

                CharInfo charInfo = this.font.getCharInfo(this.textContent.charAt(i));
                if(charInfo == null) continue;
                lineSizeX += charInfo.dest.width() * this.textSize;
                if(lineSizeX > sizeX) {
                    sizeX = lineSizeX;
                    this.computedOutputSize.x = sizeX;
                }
            }

            currentPos.x = -this.computedOutputSize.x * .5f;
        }
        
        // create vertices
        for(int i = 0; i < this.textContent.length(); ++i) {
            if(this.textContent.charAt(i) == '\n') {
                currentPos.x  = (this.centered)? -this.computedOutputSize.x * .5f : 0f;
                currentPos.y -= this.font.getLineHeight() * this.textSize;
            } else { 
                vertexCount += 6;

                // vertices
                final CharInfo charInfo = this.font.getCharInfo(this.textContent.charAt(i));
                if(charInfo == null) continue;
                // first tri
                // top-left
                vertices.add(currentPos.x());
                vertices.add(currentPos.y()); 
                vertices.add(0f);
                vertices.add(charInfo.source.x());
                vertices.add(charInfo.source.y() + charInfo.source.height());
                // top-right
                vertices.add(currentPos.x() + charInfo.dest.width() * this.textSize);
                vertices.add(currentPos.y()); 
                vertices.add(0f);
                vertices.add(charInfo.source.x() + charInfo.source.width());
                vertices.add(charInfo.source.y() + charInfo.source.height());
                // bottom-right
                vertices.add(currentPos.x() + charInfo.dest.width()  * this.textSize);
                vertices.add(currentPos.y() + charInfo.dest.height() * this.textSize); 
                vertices.add(0f);
                vertices.add(charInfo.source.x() + charInfo.source.width());
                vertices.add(charInfo.source.y());

                // second tri
                // bottom-right
                vertices.add(currentPos.x() + charInfo.dest.width()  * this.textSize);
                vertices.add(currentPos.y() + charInfo.dest.height() * this.textSize); 
                vertices.add(0f);
                vertices.add(charInfo.source.x() + charInfo.source.width());
                vertices.add(charInfo.source.y());
                // bottom-left
                vertices.add(currentPos.x());
                vertices.add(currentPos.y() + charInfo.dest.height() * this.textSize); 
                vertices.add(0f);
                vertices.add(charInfo.source.x());
                vertices.add(charInfo.source.y());
                // top-left
                vertices.add(currentPos.x());
                vertices.add(currentPos.y()); 
                vertices.add(0f);
                vertices.add(charInfo.source.x());
                vertices.add(charInfo.source.y() + charInfo.source.height());
                
                // advance
                currentPos.x += charInfo.advance * this.textSize;
            }
        }

        DynamicVertexBuffer vbo = (DynamicVertexBuffer)this.mesh.getVertexBuffer();
        vbo.reset();
        vbo.setData(vertices, vertexCount);
    }

    /**
     * Draw the text
     * @param textShader shader to use (it uses uAtlas and uPosition)
     */
    public void draw(Shader textShader) {
        if(this.mesh != null) {
            this.font.getAtlas().bind(0);
            textShader.setUniform("uAtlas", 0);
            textShader.setUniform("uPosition", this.position);
            textShader.setUniform("uColor", this.color);
            this.mesh.draw(textShader);
            this.font.getAtlas().unbind();
        }
    }

    /**
     * Delete this text and its opengl objects
     */
    public void delete() {
        if(this.mesh != null) {
            this.mesh.delete();
            this.mesh = null;
        }
    }

    /**
     * Returns the computed output size (in pixels)
     * @return the computed output size (in pixels)
     */
    public Vector2f getComputedOutputSize() {
        return computedOutputSize;
    }

    /**
     * Returns the uv screen position (in [-1,1])
     * @return the uv screen position (in [-1,1])
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Returns true if the text is centered, false otherwise
     * @return true if the text is centered, false otherwise
     */
    public boolean isCentered() {
        return centered;
    }

    /**
     * Change the text alignment to left or center
     * @param centered centered if true, left if false
     */
    public void setCentered(boolean centered) {
        this.centered = centered;
        this.buildMesh();
    }

    /**
     * Set the color of the text
     * @param color new color of the text
     */
    public void setColor(Color color) {
        this.color = color;
    }
}