#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;

uniform sampler2D uTex;
uniform float uAlpha;

void main() {
    fragColor = texture(uTex, oTexCoords);
    fragColor.a *= uAlpha;
}