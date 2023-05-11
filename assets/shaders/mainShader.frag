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
float linearizeDepth(float depth) 
{
    float z = depth * 2.0 - 1.0; // back to NDC 
    return (2.0 * NEAR * FAR) / (FAR + NEAR - z * (FAR - NEAR));	
}

// MAIN

void main() {
    // color
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

    // position
    fragPosColor = oVertexPos;

    // normal
    fragNormalColor = vec4(oNormals, 1);

}
