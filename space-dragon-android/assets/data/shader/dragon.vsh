attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0; 
attribute vec4 a_normal;
uniform mat4 u_worldView;
varying vec4 v_color;
varying vec4 v_normal;
varying vec2 v_texCoords;

void main() 
{
	v_color = vec4(0.3, 1.0, 0.3, 1);
	v_texCoords = a_texCoord0;
	gl_Position =  u_worldView * a_position;
}                          