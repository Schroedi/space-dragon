#ifdef GL_ES 
precision mediump float;
#endif 
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0; 
uniform mat4 u_worldView;
uniform mat4 u_realWorldView;
uniform mat4 u_realView;
varying vec4 v_color;
varying vec4 v_normal;
varying vec4 v_sunDir;
varying vec2 v_texCoords;

uniform vec3 sunDir;

void main() 
{
 v_color = vec4(0.3, 1.0, 0.3, 1);
 v_texCoords = a_texCoord0;
 gl_Position =  u_worldView * vec4(a_position,1);
 v_normal = normalize(u_realWorldView * vec4(a_normal,0));
 v_sunDir = normalize(u_realView * vec4(sunDir,0));
} 
