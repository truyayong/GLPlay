precision mediump float;
varying vec2 ft_Position;
uniform sampler2D sTuxture;
void main() {
    gl_FragColor=texture2D(sTuxture, ft_Position);
}