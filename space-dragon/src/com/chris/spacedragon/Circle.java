package com.chris.spacedragon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Circle {
	private Game game;
	
	public static Texture textureCircle;

	public static Mesh mesh;

	public static float[] verts = new float[20];

	public static ShaderProgram shaderCircleTexture;

	public Vector3 position;

	public float size = 1.0f;

	public float t = 0;

	public static List<Circle> circles = new ArrayList<Circle>();

	public static void initializeAll() {
		int i = 0;

		verts[i++] = -1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0;
		verts[i++] = 0f; // u1
		verts[i++] = 0f; // v1

		verts[i++] = 1; // x1
		verts[i++] = 1; // y1
		verts[i++] = 0;
		verts[i++] = 1f; // u1
		verts[i++] = 0f; // v1

		verts[i++] = 1; // x1
		verts[i++] = -1; // y1
		verts[i++] = 0;
		verts[i++] = 1f; // u1
		verts[i++] = 1f; // v1

		verts[i++] = -1; // x1
		verts[i++] = -1; // y1
		verts[i++] = 0;
		verts[i++] = 0f; // u1
		verts[i++] = 1f; // v1

		mesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
				Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE
						+ "0"));
		mesh.setVertices(verts);

		String vertexShader = "attribute vec4 a_position;    \n"
				+ "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
				+ "uniform mat4 u_worldView;\n" + "varying vec4 v_color;"
				+ "varying vec2 v_texCoords;"
				+ "void main()                  \n"
				+ "{                            \n"
				+ "   v_color = vec4(1.0, 1.0, 1.0, 1); \n"
				+ "   v_texCoords = a_texCoord0; \n"
				+ "   gl_Position =  u_worldView * a_position;  \n"
				+ "}                            \n";
		String fragmentShader = "#ifdef GL_ES\n"
				+ "precision mediump float;\n"
				+ "#endif\n"
				+ "varying vec4 v_color;\n"
				+ "varying vec2 v_texCoords;\n"
				+ "uniform sampler2D u_texture;\n"
				+ "void main()                                  \n"
				+ "{                                            \n"
				+ "gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
				+ "}";
		shaderCircleTexture = new ShaderProgram(vertexShader, fragmentShader);

		textureCircle = new Texture(Gdx.files.internal("data/textures/circle.png"));
	}

	public Circle(float r, Game game) {
		size = r;
		t = (float) (Math.random() * 360.0f);
		this.game = game;
	}
	
	public void Hit() {
		this.game.addPoints(100);
	}

	static void addToList(Vector3 pos, Game game) {
		Circle c = new Circle(1, game);
		c.position = pos;
		circles.add(c);

	}

	static void renderAll(PerspectiveCamera camera) {
		for (Circle c : circles) {
			c.render(camera);
		}
	}

	static void updateAll(Dragon dragon) {
		Collection<Circle> toRemove = new LinkedList<Circle>();
		for (Circle c : circles) {
			c.update();
			if(c.testCollision(dragon.position))
			{
			  c.Hit();
			  toRemove.add(c);
			}
		}
		
		circles.removeAll(toRemove);
	}

	public void render(PerspectiveCamera camera) {
		t += 1;
		Matrix4 mat = camera.combined.cpy();
		mat.translate(position);
		mat.scale(size, size, size);
		mat.rotate(0, 0, 1, t);

		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);

		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_BLEND);

		shaderCircleTexture.begin();
		textureCircle.bind();

		// render body
		shaderCircleTexture.setUniformMatrix("u_worldView", mat);
		mesh.render(Game.shaderMain, GL20.GL_TRIANGLE_FAN);
		shaderCircleTexture.end();
	}

	public void update() {

	}

	public boolean testCollision(Vector3 pos) {
		if (pos.tmp().sub(position).len2() < size * size) {
			return true;
		}
		return false;
	}

}
