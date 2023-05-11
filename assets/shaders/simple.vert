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