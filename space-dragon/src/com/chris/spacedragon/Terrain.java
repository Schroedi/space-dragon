package com.chris.spacedragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Terrain {
	private static int NUMVERTX = 10;
	private static int NUMVERTY = 10;
	private static int NUMVERT = NUMVERTX * NUMVERTY; // number of vertices
	private static int NUMCOMP = 5; // * 5 components (xyz,uv)
	static float[] verts = new float[NUMVERT * NUMCOMP];
	static short[] indices = new short[(NUMVERTX - 1) * (NUMVERTY - 1) * 6];
	public static Mesh mesh;
	private static ShaderProgram shaderTerrain;

	static {
		for (int y = 0; y < NUMVERTY; ++y) {
			for (int x = 0; x < NUMVERTX; ++x) {
				float offX = (x - NUMVERTY/2) * 10;
				float offY = y * -10;

				verts[y * (NUMVERTX * NUMCOMP) + (x* NUMCOMP) + 0] = offX;
				verts[y * (NUMVERTX * NUMCOMP) + (x* NUMCOMP) + 1] = 0; // y
				verts[y * (NUMVERTX * NUMCOMP) + (x* NUMCOMP) + 2] = offY; //
				
				verts[y * (NUMVERTX * NUMCOMP) + (x* NUMCOMP) + 3] = 0; // u
				verts[y * (NUMVERTX * NUMCOMP) + (x* NUMCOMP) + 4] = 0; // v
			}
		}

		int idx = 0;
		short vidx = 0;
		for (int y = 0; y < NUMVERTY - 1; y++) {
			for (int x = 0; x < NUMVERTX - 1; x++) {
				vidx = (short) (x + y * NUMVERTX);
				indices[idx++] = vidx;
				indices[idx++] = (short) (vidx + 1);
				indices[idx++] = (short) (vidx + NUMVERTX + 1);

				indices[idx++] = vidx;
				indices[idx++] = (short) (vidx + NUMVERTX + 1);
				indices[idx++] = (short) (vidx + NUMVERTX);
			}
		}

		mesh = ObjLoader.loadObj(Gdx.files.internal("data/models/canyon.obj").read());
		mesh.scale(2f, 4f, 2f);
//		mesh = new Mesh(true, NUMVERT, (NUMVERTX - 1) * (NUMVERTY - 1) * 6, // static
//																			// mesh
//																			// with
//																			// 4
//																			// vertices
//																			// and
//																			// no
//				// indices
//				new VertexAttribute(Usage.Position, 3,
//						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
//						Usage.TextureCoordinates, 2,
//						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
//
//		mesh.setVertices(verts);
//		mesh.setIndices(indices);

		String vertexShader = "attribute vec4 a_position;    \n"
				+ "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
				+ "uniform vec3 v_camPos;\n" + "uniform vec3 v_camDir;\n"
				+ "uniform mat4 u_worldView;\n" + "varying vec4 v_color;"
				+ "varying vec2 v_texCoords;"
				+ "void main()                  \n"
				+ "{                            \n"
				+ "   v_color = vec4(1, 0.5, 1, 1); \n"
				+ "   v_texCoords = a_texCoord0; \n"
				+ "   gl_Position =  u_worldView * a_position;  \n"
				+ "}                            \n";
		String fragmentShader = "#ifdef GL_ES\n" + "precision mediump float;\n"
				+ "#endif\n" + "varying vec4 v_color;\n"
				+ "varying vec2 v_texCoords;\n"
				+ "uniform sampler2D u_texture;\n"
				+ "void main()                                  \n"
				+ "{                                            \n"
				+ "  gl_FragColor = v_color;\n" + "}";
		shaderTerrain = new ShaderProgram(vertexShader, fragmentShader);
	}

	static void render(PerspectiveCamera camera) {
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

		shaderTerrain.begin();
		shaderTerrain.setUniformMatrix("u_worldView", camera.combined);

		Terrain.mesh.render(Game.shaderMain, GL20.GL_TRIANGLES);

		shaderTerrain.end();
	}
}
