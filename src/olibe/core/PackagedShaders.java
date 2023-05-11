package olibe.core;

import olibe.exception.LWJGLException;
import olibe.io.ShaderLibrary;
import olibe.render.Shader;

public class PackagedShaders {
    protected static final String SIMPLE_POSNORMTEX_VERT = """
        #version 460 core

        layout (location = 0) in vec3 aPos;
        layout (location = 1) in vec3 aNormals;
        layout (location = 2) in vec2 aTexCoords;
        
        out vec2 oTexCoords;
        out vec3 oNormals;
        out vec4 oVertexPos;
        
        uniform mat4 uProj;
        uniform mat4 uView;
        uniform mat4 uModel;
        
        void main() {
            vec4 pos = uProj * uView * uModel * vec4(aPos, 1);
            oVertexPos = pos;
            gl_Position = pos;
        
            oTexCoords = aTexCoords;
            oNormals = aNormals;
        }
    """;

    protected static final String SIMPLE_POSTEX_VERT = """
        #version 460 core

        layout (location = 0) in vec3 aPos;
        layout (location = 1) in vec2 aTexCoords;
        
        out vec2 oTexCoords;
        out vec4 oVertexPos;
        
        uniform mat4 uProj;
        uniform mat4 uView;
        uniform mat4 uModel;

        uniform float uTime;
        
        void main() {
            vec4 pos = uProj * uView * uModel * vec4(aPos, 1);
            oVertexPos = pos;
            gl_Position = pos;
            oTexCoords = aTexCoords;
        }
    """;

    protected static final String[] SCREEN_SHADER = {
        """
            #version 460 core

            layout (location = 0) in vec3 aPos;
            layout (location = 1) in vec2 aTexCoords;

            out vec2 oTexCoords;

            void main() {
                gl_Position = vec4(aPos.x, aPos.y, 0, 1);
                oTexCoords = aTexCoords;
            }
        """,
        """
            #version 460 core

            out vec4 fragColor;

            in vec2 oTexCoords;

            uniform sampler2D finalTexture;
            void main() {
                fragColor = texture(finalTexture, oTexCoords);
            }      
        """
    };

    protected static final String SCREENUI_VERT = """
        #version 460 core

        layout (location = 0) in vec3 aPos;
        layout (location = 1) in vec2 aTexCoords;

        out vec2 oTexCoords;
        uniform vec2 uScreenSize;
        uniform vec2 uPosition;

        void main() {
            gl_Position = vec4(uPosition.xy + 2*vec2(aPos.x / uScreenSize.x, aPos.y / uScreenSize.y), 0, 1);
            oTexCoords = aTexCoords;
        }
    """;

    protected static final String[] DEPTH_SHADER = {
        SIMPLE_POSNORMTEX_VERT,
        """
            #version 460 core

            out vec4 fragColor;
            
            in float depth;
            
            const float NEAR = 0.1; 
            const float FAR  = 1000.0; 
              
            float linearizeDepth(float depth) 
            {
                float z = depth * 2.0 - 1.0; // back to NDC 
                return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));	
            }
            
            void main() {
                float depth = linearizeDepth(gl_FragCoord.z) / FAR;
                fragColor = vec4(vec3(depth),1);//texture(uTex, oTexCoords);
            } 
        """
    };

    protected static final String[] TEXT_SHADER = {
        SCREENUI_VERT,
        """
            #version 460 core

            out vec4 fragColor;
            
            in vec2 oTexCoords;
            in vec3 oNormals;
            
            uniform sampler2D uAtlas;
            uniform vec4 uColor;
            
            void main() {
                float color = texture(uAtlas, oTexCoords).r;
                fragColor = vec4(color) * uColor;
            }        
        """
    };

    protected static final String[] SCREENUI_SHADER = {
        SCREENUI_VERT,
        """
            #version 460 core

            out vec4 fragColor;

            in vec2 oTexCoords;
            in vec3 oNormals;

            uniform vec4 uColor;

            void main() {
                fragColor = vec4(uColor);
            }   
        """
    };

    protected static final String[] SPRITE_SHADER = {
        SIMPLE_POSTEX_VERT,
        """
            #version 460 core

            layout (location = 0) out vec4 fragColor;

            in vec2 oTexCoords;

            uniform sampler2D uTex;
            uniform float uAlpha;

            void main() {
                fragColor = texture(uTex, oTexCoords);
                fragColor.a *= uAlpha;
            }
        """
    };

    protected static final String[] SCREENUI_TEX_SHADER = {
        SCREENUI_VERT,
        SPRITE_SHADER[1]
    };

    protected static final String[] MAINDEFFERED_SHADER = {
        SIMPLE_POSNORMTEX_VERT,
        """
            #version 460 core
            // OUTPUTS
            layout (location = 0) out vec4 fragColor;
            layout (location = 1) out vec4 fragPosColor;
            layout (location = 2) out vec4 fragNormalColor;

            // INPUTS
            in vec2 oTexCoords;
            in vec3 oNormals;
            in vec4 oVertexPos;

            // MATERIALS
            uniform int activeMaterial;
            uniform struct ColorMaterial {
                vec4 color;
            };
            uniform ColorMaterial cmaterial;
            uniform vec3 sunDirection;
            // DEPTH
            const float NEAR = 0.1; 
            const float FAR  = 1000.0; 
            float linearizeDepth(float depth) {
                float z = depth * 2.0 - 1.0; // back to NDC 
                return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));	
            }

            void main() {
                switch(activeMaterial) {
                    case 1: {// color mat
                        float lightPower = (dot(oNormals, vec3(0,1,0)) + 1) * .5;
                        fragColor = cmaterial.color * vec4(vec3(lightPower), 1);
                        break;
                    }
                    default:
                        fragColor = vec4(1, 0, 1, 1);
                        break; 
                }
                fragPosColor = oVertexPos;
                fragNormalColor = vec4(oNormals, 1);
            }
        """
    };

    protected static final String[][] SHADERS = {
        {"depth", DEPTH_SHADER[0], DEPTH_SHADER[1]},
        {"screen", SCREEN_SHADER[0], SCREEN_SHADER[1]},
        {"text", TEXT_SHADER[0],TEXT_SHADER[1]},
        {"screenUI", SCREENUI_SHADER[0], SCREENUI_SHADER[1]},
        {"screenUITextured", SCREENUI_TEX_SHADER[0], SCREENUI_TEX_SHADER[1]},
        {"sprite", SPRITE_SHADER[0], SPRITE_SHADER[1]},
        {"maindeffered", MAINDEFFERED_SHADER[0], MAINDEFFERED_SHADER[1]}
    };

    public static void loadAll() {
        try {
            for(int i = 0; i < SHADERS.length; ++i) {
                String name = SHADERS[i][0];
                String vert = SHADERS[i][1];
                String frag = SHADERS[i][2];
                Shader shader = new Shader(vert, frag);
                ShaderLibrary.Get().registerShader(name, shader);
            }
        } catch(LWJGLException e) {
            System.err.print("[ERROR] [PACKAGED SHADER] ");
            e.printStackTrace(System.err);
        }
    }
}
