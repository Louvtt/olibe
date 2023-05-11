#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;
in vec3 oNormals;

uniform vec4 uColor;

void main() {
    fragColor = vec4(uColor);
}