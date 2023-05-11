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