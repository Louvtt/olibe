package olibe.io;

import olibe.core.Log;
import olibe.exception.LWJGLException;
import olibe.render.*;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.assimp.Assimp.*;

import java.io.IOException;
import java.util.*;

/**
 * Model loader class
 */
public class ModelLoader {

    /** Useless model loader constructor */
    private ModelLoader() {}
    /**
     * Load a model from a file
     * @param filepath path to the file
     * @return the loaded model
     * @throws LWJGLException failed when creating corresponding opengl objects
     * @throws IOException failed to find the file
     */
    public static Model load(String filepath) 
    throws LWJGLException, IOException {
        final AIScene loadedScene = aiImportFile(filepath, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
        if(loadedScene == null) throw new IOException("Cannot load Model at ["+filepath+"]: " + aiGetErrorString());
        
        final int numMaterials = loadedScene.mNumMaterials();
        final PointerBuffer aiMaterials = loadedScene.mMaterials();
        Material[] materials = new Material[numMaterials];
        for(int i = 0; i < numMaterials; ++i) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            materials[i] = processMaterial(aiMaterial);
        }

        final int numMeshes = loadedScene.mNumMeshes();
        final PointerBuffer aiMeshes = loadedScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for(int i = 0; i < numMeshes; ++i) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            meshes[i] = processMesh(aiMesh, materials);
        }

        Log.Get().debug("Model ["+filepath+"] loaded (Has "+meshes.length+" meshes)");
        return new Model(meshes, null);
    }

    /**
     * Returns the processed ai mesh data 
     * @param aiMesh assimp mesh data
     * @param materials list of loaded materails
     * @return the processed ai mesh data 
     * @throws LWJGLException failed to create corresponding opengl objects
     */
    private static Mesh processMesh(AIMesh aiMesh, Material[] materials) 
    throws LWJGLException {

        // process indices
        final AIFace.Buffer aiFaces = aiMesh.mFaces();
        List<Integer> indices = new ArrayList<Integer>(aiMesh.mNumFaces() * 3); // assume triangles
        int indexCount = 0;
        while(aiFaces.remaining() > 0) {
            AIFace face = aiFaces.get();
            if(face.mNumIndices() != 3) continue; // skip if not triangle
            for(int i = 0; i < face.mNumIndices(); ++i) {
                indices.add(face.mIndices().get(i));
                indexCount++;
            }
        }
        IndexBuffer ebo = new IndexBuffer(indices, indexCount);
        Log.Get().debug("   Mesh has " + aiMesh.mNumFaces() + " tris");// => " + indices.capacity() + " indices");

        // process vertices
        final AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        final AIVector3D.Buffer aiNormals  = aiMesh.mNormals();
        final AIVector3D.Buffer aiTexCoords = aiMesh.mTextureCoords(0);
        
        List<Float> vertices = new ArrayList<Float>(aiMesh.mNumVertices() * (3 + 3 + 2));
        int vertexCount = 0;
        while(aiVertices.remaining() > 0) {
            AIVector3D vertex = aiVertices.get();
            vertices.add(vertex.x());
            vertices.add(vertex.y());
            vertices.add(vertex.z());
            // System.out.println("v " + vertex.x() + " " + vertex.y() + " " + vertex.z());

            AIVector3D normal = aiNormals.get();
            vertices.add(normal.x());
            vertices.add(normal.y());
            vertices.add(normal.z());

            AIVector3D texCoord = aiTexCoords.get();
            vertices.add(texCoord.x());
            vertices.add(1 - texCoord.y());
            vertexCount++;
        }
        Log.Get().debug("   Mesh has " + aiMesh.mNumVertices() + " vertices; " + vertexCount + " - " + vertices.size());
        VertexBuffer vbo = new VertexBuffer(vertices, vertexCount);
        MeshAttributeType[] attribs = {
            MeshAttributeType.FLOAT3, // pos
            MeshAttributeType.FLOAT3, // normal
            MeshAttributeType.FLOAT2  // tex
        };

        Mesh m = new Mesh(vbo, ebo, attribs);
        m.setMaterial(materials[aiMesh.mMaterialIndex()]);
        return m;
    } 

    /**
     * Returns the processed material data
     * @param aiMaterial assimp material data
     * @return the processed material data
     */
    private static Material processMaterial(AIMaterial aiMaterial) {
        AIColor4D aiColor = AIColor4D.create();
        Color ambient = new Color(1f, 1f, 1f);
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, aiColor);
        if (result == 0) {
            ambient = new Color(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a());
        }
        return new ColorMaterial(ambient);
    }
}