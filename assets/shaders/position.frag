#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;
in vec3 oNormals;
in vec4 oVertexPos;

uniform sampler2D uTex;

void main() {
    fragColor = oVertexPos;
}