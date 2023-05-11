#version 460 core

out vec4 fragColor;

in vec2 oTexCoords;
in vec3 oNormals;

uniform vec2 parameters;
uniform sampler2D colorTexture;

void main() {
    vec2 texSize  = textureSize(colorTexture, 0).xy;
    fragColor = texture(colorTexture, oTexCoords);

    int size = int(parameters.x);
    if (size <= 0) { return; }

    float separation = parameters.y;
    separation = max(separation, 1);

    fragColor.rgb = vec3(0);
    float count = 0.0;
    for (int i = -size; i <= size; ++i) {
        for (int j = -size; j <= size; ++j) {
            vec2 coord = (gl_FragCoord.xy + (vec2(i, j) * separation)) / texSize;
            fragColor.rgb += texture(colorTexture, coord).rgb;
            count += 1.0;
        }
    }

    fragColor.rgb /= count;
}