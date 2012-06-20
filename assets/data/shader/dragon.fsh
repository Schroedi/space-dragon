#ifdef GL_ES 
precision mediump float;
#endif 
varying vec4 v_color;
varying vec4 v_normal;
varying vec4 v_sunDir;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main()                                  
{                                            
//gl_FragColor = mix(v_color, abs(v_normal), .5);                  
 vec4 color = texture2D(u_texture, vec2(v_texCoords.x,1.0-v_texCoords.y));
 
 vec3 specularColor = vec3(1.0) * .5;
 vec3 r = reflect(v_sunDir, v_normal).xyz;
 vec3 camDir = vec3(0,0,-1);
 
 vec3 finalColor = color.rgb * .3; // ambient
 finalColor += color.rgb * clamp(dot(v_normal.xyz, v_sunDir.xyz), 0.0, 1.0); // diffuse
 finalColor += pow(max(dot(r, camDir), 0.0), 64.0) * specularColor; // specular
  
  //finalColor = v_normal.xyz;
  
 gl_FragColor = vec4(finalColor, 1.0);
} 
