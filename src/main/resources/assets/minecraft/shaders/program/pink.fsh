#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec3 pinkTint = vec3(1.0, 0.35, 0.75);

    float pulse = sin(Time * 3.0) * 0.5 + 0.5;
    float shimmer = sin(Time * 1.7) * 0.3 + 0.7;

    vec3 animatedTint = pinkTint * (0.8 + 0.2 * pulse);
    float mixFactor = 0.25 + pulse * 0.45;
    vec3 tinted = mix(color.rgb, animatedTint, mixFactor);
    tinted = mix(tinted, vec3(1.0, 0.6, 0.9), 0.08 * shimmer);

    fragColor = vec4(tinted, color.a);
}