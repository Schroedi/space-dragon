package com.chris.spacedragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Dragon {
	public Vector3 position = new Vector3();
	public Vector3 speed = new Vector3();
	public Quaternion orientation = new Quaternion();
	public Vector3 Ypr = new Vector3();
	
	public Quaternion rotationspeed = new Quaternion();
	public Quaternion Ident = new Quaternion(0, 0, 0, 1);
	// 0 = left wing up, 1 = left wing down
	public float leftWingDown;
	// 0 = right wing up, 1 = right wing down
	public float rightWingDown;

	public float[] vertsBody = new float[15];
	public float[] vertsWingUp = new float[15];
	public Mesh meshBody;
	public Mesh meshWing;

	public static ShaderProgram shaderDragon;

	public long lastUpdate;
	public Boolean leftKeyDown;
	public long lastLeftKeyDown;
	public Boolean rightKeyDown;
	public long lastRightKeyDown;
	public Vector3 ModelAxis;
	public Vector3 ModelAxisUp;
	
	public Mesh[] Models = new Mesh[15];
	public Texture[] Textures = new Texture[15];
	public static float WingDist = 1;
	public static float FRa = -5; // forward accel with wing swinging
	public static Vector3 GRAV = new Vector3(0, -9.81f, 0); // basic Downward
															// accel
	public static float TopUPa = 2; // upward acceleration with wing at the //
									// top
	public static float DownUPa = 1f; // upward acceleration with wing at //
										// the bottom
	public static float SwUPa = 20; // upward acceleration with wing swinging
	public static float WingMovePerSec = 2.0f; // wing movement per //
												// second;

	public BitmapFont bitmapFont;
	public SpriteBatch spriteBatch;
	
	public String points;
	
	// loading of assets etc
	public void create() {
		this.points = "0";
		int i = 0;

	

		/*
		 * meshBody = new Mesh(true, 3, 0, // static mesh with 4 vertices and no
		 * // indices new VertexAttribute(Usage.Position, 3,
		 * ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
		 * Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE +
		 * "0"));
		 */
		
		
		Models[0] =ObjLoader.loadObj(Gdx.files.internal("data/models/head.obj").read());
		Models[1] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine1.obj").read());
		Models[2] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine2.obj").read());
		Models[3] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine3.obj").read());
		Models[4] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine4.obj").read());
		Models[5] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine5.obj").read());
		Models[6] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine6.obj").read());
		Models[7] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine7.obj").read());
		Models[8] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine8.obj").read());
		Models[9] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine9.obj").read());
		Models[10] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine10.obj").read());
		Models[11] =ObjLoader.loadObj(Gdx.files.internal("data/models/spine11.obj").read());
		Models[12] =ObjLoader.loadObj(Gdx.files.internal("data/models/schwanz.obj").read());
		
		Textures[0]  = new Texture(Gdx.files.internal("data/textures/kopfx.png"));
		Textures[1]  = new Texture(Gdx.files.internal("data/textures/wirbel_1x.png"));
		Textures[2]  = new Texture(Gdx.files.internal("data/textures/wirbel_2x.png"));
		Textures[3]  = new Texture(Gdx.files.internal("data/textures/wirbel_3x.png"));
		Textures[4]  = new Texture(Gdx.files.internal("data/textures/wirbel_4x.png"));
		Textures[5]  = new Texture(Gdx.files.internal("data/textures/wirbel_5x.png"));
		Textures[6]  = new Texture(Gdx.files.internal("data/textures/wirbel_6x.png"));
		Textures[7]  = new Texture(Gdx.files.internal("data/textures/wirbel_7x.png"));
		Textures[8]  = new Texture(Gdx.files.internal("data/textures/wirbel_8x.png"));
		Textures[9]  = new Texture(Gdx.files.internal("data/textures/wirbel_9x.png"));
		Textures[10] = new Texture(Gdx.files.internal("data/textures/wirbel_10x.png"));
		Textures[11] = new Texture(Gdx.files.internal("data/textures/wirbel_10x.png"));
		Textures[12] = new Texture(Gdx.files.internal("data/textures/schwanzx.png"));
		
		// FileHandle handle = new FileHandle("data/models/spacedragon.obj");
		meshBody = ObjLoader.loadObj(Gdx.files.internal("data/models/body.obj").read());
		/*
		 * meshWing = new Mesh(true, 3, 0, // static mesh with 4 vertices and //
		 * no // indices new VertexAttribute(Usage.Position, 3,
		 * ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
		 * Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE +
		 * "0"));
		 * 
		 * //meshBody.setVertices(vertsBody); meshWing.setVertices(vertsWingUp);
		 */

		meshWing = ObjLoader.loadObj(Gdx.files.internal("data/models/wing.obj")
				.read());

	
		rightWingDown = 1.0f;
		leftWingDown = 1.0f;


		String vertexShader = Gdx.files.internal("data/shader/dragon.vsh").readString();
		String fragmentShader =  Gdx.files.internal("data/shader/dragon.fsh").readString();
		shaderDragon = new ShaderProgram(vertexShader, fragmentShader);
		//System.out.println(shaderDragon.getLog());

		String [] args = shaderDragon.getAttributes();
		VertexAttributes va = meshBody.getVertexAttributes();
		
		ModelAxis = new Vector3(0, 0, -1);
		ModelAxisUp = new Vector3(0, 1, 0);
		orientation.idt();
		rotationspeed.idt();
		
	//	this.bitmapFont = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"),true);
		this.spriteBatch = new SpriteBatch();
	}

	public void render(PerspectiveCamera camera) {
		Matrix4 mat = camera.combined.cpy();
		mat.translate(position);
		Matrix4 shadowmat = mat.cpy();
		mat.rotate(orientation);
		Matrix4 wing = mat.cpy();
		shaderDragon.begin();

		Matrix4 matWorldView = camera.view.cpy();
		matWorldView.translate(position);
		matWorldView.rotate(orientation);
		// render body
		shaderDragon.setUniformMatrix("u_worldView", mat);
		shaderDragon.setUniformMatrix("u_realView", camera.view);
		shaderDragon.setUniformMatrix("u_realWorldView", matWorldView);
		shaderDragon.setUniformf("sunDir", 0,1,0);
		//meshBody.render(shaderDragon, GL20.GL_TRIANGLES);

		// render shadow
		//shadowmat.translate(0, -position.y + 0.01f, 0);
		//shadowmat.scale(1, 0, 1);
		//shadowmat.rotate(orientation);
		//shaderDragon.setUniformMatrix("u_worldView", shadowmat);
		for(int i=0; i<13;i++)
		{
			Textures[i].bind();
			
			Models[i].render(shaderDragon, GL20.GL_TRIANGLES);
		}
	
		
		shaderDragon.end();
		Game.shaderMain.begin();
		// render left wing
		wing.rotate(0, 0, -1, 50.0f);
		wing.rotate(0, 0, -1, leftWingDown * -70.0f);
		Game.shaderMain.setUniformMatrix("u_worldView", wing);
		
		meshWing.render(Game.shaderMain, GL20.GL_TRIANGLES);
		Game.shaderMain.end();
		
		// render right wing
		wing = mat.cpy();
		wing.scale(-1, 1, 1);
		wing.rotate(0, 0, -1, 50.0f);
		wing.rotate(0, 0, -1, rightWingDown * -70.0f);
		shaderDragon.begin();
		shaderDragon.setUniformMatrix("u_worldView", wing);

		meshWing.render(shaderDragon, GL20.GL_TRIANGLES);

		shaderDragon.end();
		
	//	this.spriteBatch.begin();
	///	bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	//	bitmapFont.draw(this.spriteBatch, this.points, 25, 60);
	//	this.spriteBatch.end();
	}

	// updates position of dragon
	public void update(long dt) {
		float timestep = dt / 1000f;
		
		float rot = 0f;
		if (Gdx.input.isKeyPressed(Keys.X)) {
			if (leftWingDown < 1) {
				leftWingDown += WingMovePerSec * timestep;
				speed.z -= 0.1;
				speed.y += 0.1;
				rot += 1.0;
			} else if (leftWingDown >= 1) {
			}
			leftKeyDown = true;
		} else {
			if (leftWingDown > 0) {
				leftWingDown -= WingMovePerSec * timestep * 2f;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.C)) {
			rightKeyDown = true;
			if (rightWingDown < 1) {
				rightWingDown += WingMovePerSec * timestep;
				speed.z -= 0.1;
				speed.y += 0.1;
				rot += -1.0;

			} else if (rightWingDown >= 1) {
			}
			rightKeyDown = true;
		} else {
			if (rightWingDown > 0) {
				rightWingDown -= WingMovePerSec * timestep * 2f;
			}
		}
		
		rotationspeed.setFromAxis(0, 0, -1, rot);
		orientation.mul(rotationspeed);
		
		if(leftWingDown > 0.5)
		{
			speed.z += 0.005;
			speed.y -= 0.05;
		}

		if(rightWingDown > 0.5)
		{
			speed.z += 0.005;
			speed.y -= 0.05;
		}

		speed.z += 0.005;
		speed.y -= 0.05;
		
		if (speed.z < -0.5f)
			speed.z = -0.5f;
		if (speed.z > 0.0f)
			speed.z = 0.0f;

		if (speed.y < 0f)
			speed.y = 0f;
		if (speed.y > 0.5f)
			speed.y = 0.5f;
		
		

		// long timestep = dt;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			orientation.mul(new Quaternion(new Vector3(0, 1, 0), -100f * timestep));
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			orientation.mul(new Quaternion(new Vector3(0, 1, 0), 100f * timestep));

		}

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			orientation.mul(new Quaternion(new Vector3(1, 0, 0), -100f * timestep));

		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			orientation.mul(new Quaternion(new Vector3(1, 0, 0), 100f * timestep));
		}
		
		Vector3 temp = speed.tmp();
		orientation.transform(temp);
		temp.mul(timestep * 10f);
		position.add(temp);
		// apply gravity;
		position.y -= 0.9 * timestep;
		
	}
		
	public void setPoints(int points) {
		this.points = Integer.toString(points);
	}
}