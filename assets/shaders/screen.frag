#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;

uniform sampler2D colorTexture;
uniform sampler2D depthTexture;
uniform sampler2D positionTexture;

uniform sampler2D blurTexture;
uniform sampler2D dofTexture;

void main() {
    fragColor = texture(dofTexture, oTexCoords);
}