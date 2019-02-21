precision mediump float;
varying vec2 ft_Position;
uniform sampler2D sTuxture;
void main() {
     lowp vec4 textureColor = texture2D(sTuxture, ft_Position);
     gl_FragColor = vec4((textureColor.rgb + vec3(-0.5)), textureColor.w);
}