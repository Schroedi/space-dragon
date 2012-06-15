package com.chris.spacedragon;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Terrain {
	static float[] verts = new float[20];
	public static Mesh mesh;

	Terrain() {
		int i = 0;

		verts[i++] = -1; // x1
		verts[i++] = -1; // y1
		verts[i++] = 1;
		verts[i++] = 0f; // u1
		verts[i++] = 0f; // v1

		verts[i++] = 1f; // x2
		verts[i++] = -1; // y2
		verts[i++] = 0;
		verts[i++] = 1f; // u2
		verts[i++] = 0f; // v2

		verts[i++] = 1f; // x3
		verts[i++] = 1f; // y2
		verts[i++] = 0;
		verts[i++] = 1f; // u3
		verts[i++] = 1f; // v3

		verts[i++] = -1; // x4
		verts[i++] = 1f; // y4
		verts[i++] = 0;
		verts[i++] = 0f; // u4
		verts[i++] = 1f; // v4

		mesh = new Mesh(true, 4, 0, // static mesh with 4 vertices and no
									// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		mesh.setVertices(verts);
	}

	static void render(PerspectiveCamera camera) {
		Game.shaderMain.begin();
		Game.shaderMain.setUniformMatrix("u_worldView", camera.combined);

		Terrain.mesh.render(Game.shaderMain, GL20.GL_TRIANGLES);

		Game.shaderMain.end();
	}
}
