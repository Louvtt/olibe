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