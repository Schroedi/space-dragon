package com.chris.spacedragon;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Dragon {
	public Vector3 position = new Vector3();
	public Quaternion orientation = new Quaternion();
	// 0 = left wing up, 1 = left wing down
	public float leftWingDown;
	// 0 = right wing up, 1 = right wing down
	public float rightWingDown;

	public float[] vertsBody = new float[15];
	public float[] vertsWingUp = new float[15];
	public Mesh meshBody;
	public Mesh meshWing;

	// loading of assets etc
	public void create() {
		int i = 0;

		vertsBody[i++] = 0; // x1
		vertsBody[i++] = 0; // y1
		vertsBody[i++] = -1;
		vertsBody[i++] = 0f; // u1
		vertsBody[i++] = 0f; // v1

		vertsBody[i++] = 1f; // x2
		vertsBody[i++] = 0; // y2
		vertsBody[i++] = 0;
		vertsBody[i++] = 1f; // u2
		vertsBody[i++] = 0f; // v2

		vertsBody[i++] = -1f; // x3
		vertsBody[i++] = 0f; // y2
		vertsBody[i++] = 0;
		vertsBody[i++] = 1f; // u3
		vertsBody[i++] = 1f; // v3

		i = 0;

		vertsWingUp[i++] = -1; // x1
		vertsWingUp[i++] = 0; // y1
		vertsWingUp[i++] = 0;
		vertsWingUp[i++] = 0f; // u1
		vertsWingUp[i++] = 0f; // v1

		vertsWingUp[i++] = -2f; // x2
		vertsWingUp[i++] = 0; // y2
		vertsWingUp[i++] = 0;
		vertsWingUp[i++] = 1f; // u2
		vertsWingUp[i++] = 0f; // v2

		vertsWingUp[i++] = -2f; // x3
		vertsWingUp[i++] = 0f; // y2
		vertsWingUp[i++] = 1;
		vertsWingUp[i++] = 1f; // u3
		vertsWingUp[i++] = 1f; // v3

		meshBody = new Mesh(true, 3, 0, // static mesh with 4 vertices and no
				// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		meshWing = new Mesh(true, 3, 0, // static mesh with 4 vertices and
										// no
				// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		meshBody.setVertices(vertsBody);
		meshWing.setVertices(vertsWingUp);

		rightWingDown = 1.0f;
		leftWingDown = 1.0f;
	}

	public void render(PerspectiveCamera camera) {
		Matrix4 mat = camera.combined;
		mat.translate(position);
		Matrix4 wing = mat.cpy();
		Game.shaderMain.begin();

		// render body
		Game.shaderMain.setUniformMatrix("u_worldView", mat);
		meshBody.render(Game.shaderMain, GL20.GL_TRIANGLES);

		// render left wing
		wing.rotate(0, 0, -1, leftWingDown * -45.0f);
		Game.shaderMain.setUniformMatrix("u_worldView", wing);

		meshWing.render(Game.shaderMain, GL20.GL_TRIANGLES);

		// render right wing
		wing = mat.cpy();
		wing.scale(-1, 1, 1);
		wing.rotate(0, 0, -1, rightWingDown * -45.0f);

		Game.shaderMain.setUniformMatrix("u_worldView", wing);

		meshWing.render(Game.shaderMain, GL20.GL_TRIANGLES);

		Game.shaderMain.end();
	}

	// updates position of dragon
	public void update() {

	}

}
