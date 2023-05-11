#version 460 core

out vec4 fragColor;
out vec4 fragColor1; // fallback ?

in vec2 oTexCoords;

uniform sampler2D main_colorTexture;
uniform sampler2D main_positionTexture;
uniform sampler2D main_normalTexture;
uniform sampler2D depth_colorTexture;

uniform vec2 mouseFocusPoint;
uniform sampler2D blurTexture;

const float minDistance =  3.0;
const float maxDistance = 50.0;

const float fogMin = 0.00;
const float fogMax = 0.97;

void main() {
  vec2 texCoord = oTexCoords;

  vec4 focusColor = texture(main_colorTexture, texCoord);
  fragColor = focusColor;

  vec4 position = texture(main_positionTexture, texCoord);
  if (position.a <= 0) { fragColor1 = vec4(1); return; }

  vec4 outOfFocusColor = texture(blurTexture,  texCoord);
  vec4 focusPoint      = texture(main_positionTexture, mouseFocusPoint);
  float blur =
    smoothstep
      ( minDistance
      , maxDistance
      , length(position - focusPoint)
      );


  fragColor = mix(focusColor, outOfFocusColor, blur);
  fragColor1 = vec4(blur);
  
  // FOG
  float fog = clamp(1 - texture(depth_colorTexture, texCoord).r, fogMin, fogMax);
  fragColor.a *= fog;
}