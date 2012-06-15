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
	public float[] vertsWingDown = new float[15];
	public Mesh meshBody;
	public Mesh meshWingUp;
	public Mesh meshWingDown;

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

		i = 0;

		vertsWingDown[i++] = -1; // x1
		vertsWingDown[i++] = 0; // y1
		vertsWingDown[i++] = 0;
		vertsWingDown[i++] = 0f; // u1
		vertsWingDown[i++] = 0f; // v1

		vertsWingDown[i++] = -2f; // x2
		vertsWingDown[i++] = -1; // y2
		vertsWingDown[i++] = 0;
		vertsWingDown[i++] = 1f; // u2
		vertsWingDown[i++] = 0f; // v2

		vertsWingDown[i++] = -2f; // x3
		vertsWingDown[i++] = -1f; // y2
		vertsWingDown[i++] = 1;
		vertsWingDown[i++] = 1f; // u3
		vertsWingDown[i++] = 1f; // v3

		meshBody = new Mesh(true, 3, 0, // static mesh with 4 vertices and no
				// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		meshWingUp = new Mesh(true, 3, 0, // static mesh with 4 vertices and
											// no
				// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		meshWingDown = new Mesh(true, 3, 0, // static mesh with 4 vertices and
											// no
				// indices
				new VertexAttribute(Usage.Position, 3,
						ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
						Usage.TextureCoordinates, 2,
						ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		meshBody.setVertices(vertsBody);
		meshWingUp.setVertices(vertsWingUp);
		meshWingDown.setVertices(vertsWingDown);
		
		rightWingDown = 1.0f;
	}

	public void render(PerspectiveCamera camera) {
		Matrix4 mat = camera.combined;
		mat.translate(position);
		Game.shaderMain.begin();
		Game.shaderMain.setUniformMatrix("u_worldView", mat);

		meshBody.render(Game.shaderMain, GL20.GL_TRIANGLES);
		if (leftWingDown < 0.5) {
			meshWingUp.render(Game.shaderMain, GL20.GL_TRIANGLES);
		} else {
			meshWingDown.render(Game.shaderMain, GL20.GL_TRIANGLES);
		}

		mat.scale(-1, 1, 1);
		Game.shaderMain.setUniformMatrix("u_worldView", mat);

		if (rightWingDown < 0.5) {
			meshWingUp.render(Game.shaderMain, GL20.GL_TRIANGLES);
		} else {
			meshWingDown.render(Game.shaderMain, GL20.GL_TRIANGLES);
		}
		mat.scale(-1, 1, 1);
		Game.shaderMain.setUniformMatrix("u_worldView", mat);

		Game.shaderMain.end();
	}

	// updates position of dragon
	public void update() {

	}

}
