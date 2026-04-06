#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    vec3 pink = vec3(1.0, 0.4, 0.7);
    color.rgb = mix(color.rgb, pink, 0.6);

    fragColor = color;
}