uniform mat4 u_Matrix;

attribute vec4 a_Position;
attribute vec2 a_TextureCoordinate;

varying vec2 v_TextureCoordinate;

void main() {
   v_TextureCoordinate = a_TextureCoordinate;
   gl_Position = u_Matrix * a_Position;
}
