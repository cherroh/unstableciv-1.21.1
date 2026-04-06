#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec3 pinkTint = vec3(1.0, 0.4, 0.8);
    vec3 tinted = mix(color.rgb, pinkTint, 0.35);
    fragColor = vec4(tinted, 1.0);
}
