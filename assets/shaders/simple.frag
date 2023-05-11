#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;
in vec3 oNormals;

uniform sampler2D uTex;

void main() {
    float grey = (dot(oNormals, vec3(0,1,0)) + 1) * .5;
    vec4 col = vec4(0,.5,.3, 1);
    fragColor = col * grey;
}