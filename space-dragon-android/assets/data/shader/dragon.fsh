//#ifdef GL_ES 
//precision mediump float;
//#endif 
varying vec4 v_color;
varying vec4 v_normal;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main()                                  
{                                            
//gl_FragColor = mix(v_color, abs(v_normal), .5);                  
 gl_FragColor = texture2D(u_texture, vec2(v_texCoords.x,1-v_texCoords.y));
};