package com.chris.spacedragon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Game implements ApplicationListener {
	private PerspectiveCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	public static ShaderProgram shaderMain;
	public static Terrain  terrain;
	
	@Override
	public void create() {
		
		terrain = new Terrain();
		String vertexShader = "attribute vec4 a_position;    \n"
				+ "attribute vec4 a_color;\n" + "attribute vec2 a_texCoord0;\n"
				+ "uniform mat4 u_worldView;\n" + "varying vec4 v_color;"
				+ "varying vec2 v_texCoords;"
				+ "void main()                  \n"
				+ "{                            \n"
				+ "   v_color = vec4(1, 0.5, 1, 1); \n"
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
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n"
				+ "}";
		shaderMain = new ShaderProgram(vertexShader, fragmentShader);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new PerspectiveCamera(75f, w, h);
		camera.position.set(5, 5, 5);
		camera.lookAt(0, 0, 0);
		camera.up.set(0, 1, 0);
		camera.update();
		//camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);

		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Terrain.render(camera);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
