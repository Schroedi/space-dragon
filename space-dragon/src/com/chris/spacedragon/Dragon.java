package com.chris.spacedragon;

import java.util.Set;

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
	public Boolean leftKeyDown = false;
	public long lastLeftKeyDown;
	public Boolean rightKeyDown = false;
	public long lastRightKeyDown;
	public Vector3 ModelAxis;
	public Vector3 ModelAxisUp;
	public float leftWingAngle;
	public float rightWingAngle;

	public Mesh[] Models = new Mesh[15];
	public Mesh[] WingParts = new Mesh[4];
	public Texture[] WingTextures = new Texture[4];
	public Texture[] Textures = new Texture[15];

	public Matrix4[] Wingpos = new Matrix4[4];

	public static float WingDist = 1;
	public static float FRa = -5; // forward accel with wing swinging
	public static Vector3 GRAV = new Vector3(0, -9.81f, 0); // basic Downward //
															// accel
	public static float TopUPa = 2; // upward acceleration with wing at the //
									// // top
	public static float DownUPa = 1f; // upward acceleration with wing at // //
										// the bottom
	public static float SwUPa = 20; // upward acceleration with wing swinging
	public static float WingMovePerSec = 0.8f; // wing movement per // //
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

		Models[0] = ObjLoader.loadObj(Gdx.files.internal("data/models/head.obj").read());
		Models[1] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine1.obj").read());
		Models[2] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine2.obj").read());
		Models[3] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine3.obj").read());
		Models[4] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine4.obj").read());
		Models[5] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine5.obj").read());
		Models[6] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine6.obj").read());
		Models[7] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine7.obj").read());
		Models[8] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine8.obj").read());
		Models[9] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine9.obj").read());
		Models[10] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine10.obj").read());
		Models[11] = ObjLoader.loadObj(Gdx.files.internal("data/models/spine11.obj").read());
		Models[12] = ObjLoader.loadObj(Gdx.files.internal("data/models/schwanz.obj").read());

		Textures[0] = new Texture(Gdx.files.internal("data/textures/kopfx.png"));
		Textures[1] = new Texture(Gdx.files.internal("data/textures/wirbel_1x.png"));
		Textures[2] = new Texture(Gdx.files.internal("data/textures/wirbel_2x.png"));
		Textures[3] = new Texture(Gdx.files.internal("data/textures/wirbel_3x.png"));
		Textures[4] = new Texture(Gdx.files.internal("data/textures/wirbel_4x.png"));
		Textures[5] = new Texture(Gdx.files.internal("data/textures/wirbel_5x.png"));
		Textures[6] = new Texture(Gdx.files.internal("data/textures/wirbel_6x.png"));
		Textures[7] = new Texture(Gdx.files.internal("data/textures/wirbel_7x.png"));
		Textures[8] = new Texture(Gdx.files.internal("data/textures/wirbel_8x.png"));
		Textures[9] = new Texture(Gdx.files.internal("data/textures/wirbel_9x.png"));
		Textures[10] = new Texture(Gdx.files.internal("data/textures/wirbel_10x.png"));
		Textures[11] = new Texture(Gdx.files.internal("data/textures/wirbel_11x.png"));
		Textures[12] = new Texture(Gdx.files.internal("data/textures/schwanzr.png"));

		WingParts[0] = ObjLoader.loadObj(Gdx.files.internal("data/models/wing1.obj").read());
		WingParts[1] = ObjLoader.loadObj(Gdx.files.internal("data/models/wing2.obj").read());
		WingParts[2] = ObjLoader.loadObj(Gdx.files.internal("data/models/wing3.obj").read());
		WingParts[3] = ObjLoader.loadObj(Gdx.files.internal("data/models/wing4.obj").read());
		WingTextures[0] = new Texture(Gdx.files.internal("data/textures/wing_1r.png"));
		WingTextures[1] = new Texture(Gdx.files.internal("data/textures/wing_2r.png"));
		WingTextures[2] = new Texture(Gdx.files.internal("data/textures/wing_3r.png"));
		WingTextures[3] = new Texture(Gdx.files.internal("data/textures/wing_4r.png"));
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

		meshWing = ObjLoader.loadObj(Gdx.files.internal("data/models/wing.obj").read());

		rightWingDown = 1.0f;
		leftWingDown = 1.0f;

		String vertexShader = Gdx.files.internal("data/shader/dragon.vsh").readString();
		String fragmentShader = Gdx.files.internal("data/shader/dragon.fsh").readString();
		shaderDragon = new ShaderProgram(vertexShader, fragmentShader);
		// System.out.println(shaderDragon.getLog());

		String[] args = shaderDragon.getAttributes();
		VertexAttributes va = meshBody.getVertexAttributes();

		ModelAxis = new Vector3(0, 0, -1);
		ModelAxisUp = new Vector3(0, 1, 0);
		orientation.idt();
		rotationspeed.idt();

		this.bitmapFont = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
		this.spriteBatch = new SpriteBatch();

		for (int j = 0; j < 4; j++) {
			WingTramsforms[j] = new Matrix4();
		}
	}

	Matrix4[] WingTramsforms = new Matrix4[4];
	final Vector3[] WingPositions = new Vector3[] { new Vector3(-0.171f, 0.271f, -0.197f), new Vector3(-0.912f, 0.441f, 0.324f),
			new Vector3(-1.582f, 0.628f, 0.363f), new Vector3(-1.606f, 0.634f, 0.384f) };

	final Quaternion[] WingAxisRot = new Quaternion[] { new Quaternion().setEulerAngles(0, 0, 0),
			new Quaternion().setEulerAngles(11.787f, 2.729f, 0f), new Quaternion().setEulerAngles(-32.609f, -9.044f, 0f),
			new Quaternion().setEulerAngles(40f, -5.762f, 0f) };

	Vector3 WingAxis = new Vector3();
	final float[] WingGlideAngle = new float[] { 7.5f, -6.5f, 6.5f, 20.5f };
	final float[] WingStartAngle = new float[] { -13f, -3f, 17f, 13f };
	float[] Wingrad = new float[4];
	boolean leftGlide = false;
	boolean rightGlide = false;

	public void render(PerspectiveCamera camera) {
		Matrix4 mat = camera.combined.cpy();
		mat.translate(position);
		mat.rotate(orientation);
		Matrix4 shadowmat = mat.cpy();
		Matrix4 wing = mat.cpy();
		shaderDragon.begin();

		Matrix4 matWorldView = camera.view.cpy();
		matWorldView.translate(position);
		matWorldView.rotate(orientation);
		// render body
		shaderDragon.setUniformMatrix("u_worldView", mat);
		shaderDragon.setUniformMatrix("u_realView", camera.view);
		shaderDragon.setUniformMatrix("u_realWorldView", matWorldView);
		shaderDragon.setUniformf("sunDir", 0, 1, 0);
		// meshBody.render(shaderDragon, GL20.GL_TRIANGLES);

		// render shadow
		// shadowmat.translate(0, -position.y + 0.01f, 0);
		// shadowmat.scale(1, 0, 1);
		// shadowmat.rotate(orientation);
		// shaderDragon.setUniformMatrix("u_worldView", shadowmat);

		for (int i = 0; i < 13; i++) {
			Textures[i].bind();

			Models[i].render(shaderDragon, GL20.GL_TRIANGLES);
		}

		float pi = (float) Math.PI;
		float x = leftWingDown / 2 * (leftKeyDown ? 1.0f : 0.0f) + (0.5f + (1 - leftWingDown) / 2.0f) * (leftKeyDown ? 0.0f : 1.0f);
		Wingrad[0] = (float) (108 * Math.sin(2 * pi * x - 0.75 * 2 * pi) + 32) / 2f;
		Wingrad[1] = (float) (104 * Math.sin(2 * pi * x) + 3) / 2.0f;
		Wingrad[2] = (float) (74.5 * Math.sin(2 * pi * x - 0.5 * 2 * pi) - 7.5) / 2f;
		Wingrad[3] = (float) (67.5 * Math.sin(2 * pi * x - 0.125 * 2 * pi) + 8.5) / 2f;
		float incline = leftWingAngle - x;
		leftGlide = Math.abs(incline) == 0 && Math.abs(0.75 - x) < 0.01f && leftKeyDown;
		leftWingAngle = x;
		wing.set(mat);
		for (int i = 0; i < 4; i++) {
			WingTramsforms[i].idt();
			WingAxis.set(0, 0, -1);
			WingAxisRot[i].transform(WingAxis);

			WingTramsforms[i].translate(WingPositions[i]);
			WingTramsforms[i].rotate(WingAxis, WingStartAngle[i]);
			if (leftGlide)
				WingTramsforms[i].rotate(WingAxis, WingGlideAngle[i]);
			else
				WingTramsforms[i].rotate(WingAxis, Wingrad[i]);
			WingTramsforms[i].translate(WingPositions[i].tmp().mul(-1));
			wing.mul(WingTramsforms[i]);
			shaderDragon.setUniformMatrix("u_worldView", wing);
			WingTextures[i].bind();
			WingParts[i].render(shaderDragon, GL20.GL_TRIANGLES);
		}

		wing.set(mat);
		wing.scale(-1, 1, 1);

		x = rightWingDown / 2 * (rightKeyDown ? 1.0f : 0.0f) + (0.5f + (1 - rightWingDown) / 2.0f) * (rightKeyDown ? 0.0f : 1.0f);
		Wingrad[0] = (float) (108 * Math.sin(2 * pi * x - 0.75 * 2 * pi) + 32) / 2f;
		Wingrad[1] = (float) (104 * Math.sin(2 * pi * x) + 3) / 2.0f;
		Wingrad[2] = (float) (74.5 * Math.sin(2 * pi * x - 0.5 * 2 * pi) - 7.5) / 2f;
		Wingrad[3] = (float) (67.5 * Math.sin(2 * pi * x - 0.125 * 2 * pi) + 8.5) / 2f;
		incline = rightWingAngle - x;
		rightGlide = Math.abs(incline) == 0 && Math.abs(0.75 - x) < 0.01f && rightKeyDown;
		rightWingAngle = x;
		for (int i = 0; i < 4; i++) {
			WingTramsforms[i].idt();
			WingAxis.set(0, 0, -1);
			WingAxisRot[i].transform(WingAxis);

			WingTramsforms[i].translate(WingPositions[i]);
			WingTramsforms[i].rotate(WingAxis, WingStartAngle[i]);
			if (rightGlide)
				WingTramsforms[i].rotate(WingAxis, WingGlideAngle[i]);
			else
				WingTramsforms[i].rotate(WingAxis, Wingrad[i]);
			WingTramsforms[i].translate(WingPositions[i].tmp().mul(-1));
			wing.mul(WingTramsforms[i]);
			shaderDragon.setUniformMatrix("u_worldView", wing);
			WingTextures[i].bind();
			WingParts[i].render(shaderDragon, GL20.GL_TRIANGLES);
		}
		shaderDragon.end();

		this.spriteBatch.begin();
		bitmapFont.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		bitmapFont.draw(this.spriteBatch, "Points: "+this.points, 10, 60);
		bitmapFont.draw(this.spriteBatch,"Speed: "+((int)(this.speed.len()*200)), 10, 80);
		bitmapFont.draw(this.spriteBatch,"use x and c to flap your wings, arrow keys to navigate (big flaps make you faster)", 10, 20);
		this.spriteBatch.end();
	}

	// updates position of dragon

	float leftWingMin = 0;
	float rightWingMin = 0;
	float leftWingMinAc = 0;
	float rightWingMinAc = 0;
	Quaternion rollientation = new Quaternion().idt();
	Quaternion calcientation = new Quaternion().idt();

	public void update(long dt) {
		float timestep = dt / 1000f;

		float rot = 0f;
		if (Gdx.input.isKeyPressed(Keys.X)) {
			if (!leftKeyDown){
				leftWingMin = Math.min(leftWingDown, 0.5f);
				leftWingMinAc= leftWingDown;
			}
			if (leftWingDown < 1 - leftWingMin) {
				leftWingDown += WingMovePerSec * timestep;
				speed.z -= 0.05* timestep*(1-(leftWingMinAc*leftWingMinAc*1.5));
				// speed.y += 0.05;
				rot += 1.0;
			} else if (leftWingDown >= 1 + leftWingMin && leftWingDown <= 1.5f) {
				leftWingDown += WingMovePerSec * timestep;

			} else if (leftWingDown >= 1 - leftWingMin && leftWingDown < 1.5f - leftWingMin) {
				leftWingDown = 1 + leftWingMin;
			}

			leftKeyDown = true;
		} else {
			if (leftKeyDown)
				leftWingDown = leftWingDown - (leftWingDown > 1 ? 2 * (leftWingDown - 1) : 0);

			if (leftWingDown > 0) {
				leftWingDown -= WingMovePerSec * timestep;
			}
			if (leftWingDown < 0)
				leftWingDown = 0;
			leftKeyDown = false;

		}

		if (Gdx.input.isKeyPressed(Keys.C)) {

			if (!rightKeyDown){
				rightWingMin = Math.min(rightWingDown, 0.5f);
				rightWingMinAc = rightWingDown;
			}
			if (rightWingDown < 1 - rightWingMin) {
				rightWingDown += WingMovePerSec * timestep;
				speed.z -= 0.05* timestep*(1-(rightWingDown*rightWingDown*1.5f));
				// speed.y += 0.05;
				rot -= 1.0;
			} else if (rightWingDown >= 1 + rightWingMin && rightWingDown <= 1.5f) {
				rightWingDown += WingMovePerSec * timestep;

			} else if (rightWingDown >= 1 - rightWingMin && rightWingDown < 1.5f - rightWingMin) {
				rightWingDown = 1 + rightWingMin;
			}

			rightKeyDown = true;
		} else {
			if (rightKeyDown)
				rightWingDown = rightWingDown - (rightWingDown > 1 ? 2 * (rightWingDown - 1) : 0);

			if (rightWingDown > 0) {
				rightWingDown -= WingMovePerSec * timestep;
			}
			if (rightWingDown < 0)
				rightWingDown = 0;
			rightKeyDown = false;

		}

		rotationspeed.setFromAxis(0, 0, -1, rot);
		calcientation.mul(rotationspeed).nor();
		// orientation.mul(rotationspeed).nor();
		// Vector3 v = new Vector3(0,1,0);
		// orientation.transform(v);
		// System.out.println(v);
		// Quaternion q =
		// (rollientation.cpy().conjugate().mul(orientation.cpy()));

		// calcientation.mul(rollientation);
		// long timestep = dt;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			calcientation.mul(new Quaternion(new Vector3(0, 1, 0), -50 * timestep));
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			calcientation.mul(new Quaternion(new Vector3(0, 1, 0), 50 * timestep));

		}

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			calcientation.mul(new Quaternion(new Vector3(1, 0, 0), -50 * timestep));

		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			calcientation.mul(new Quaternion(new Vector3(1, 0, 0), 50 * timestep));
		}
		// calcientation.mul(rollientation.cpy().conjugate());

		Vector3 v2 = new Vector3(0, 0, -1);
		orientation.transform(v2);
		float f2 = v2.dot(0, 1, 0);

		orientation.set(calcientation);
		Vector3 temp = speed.tmp();
		calcientation.transform(temp);
		//System.out.println(temp.len());

		position.add(temp);
		// apply gravity;

		speed.y -= (0.9 - (leftGlide ? 1f*f2 : 0) - (rightGlide ? 1f*f2 : 0)) * timestep * 0.01f;
		// speed.z-=- f2*30f* timestep;

		Vector3 v = new Vector3(1, 0, 0);
		orientation.transform(v);

		float f = v.dot(0, 1, 0);
		if (f < -0.001)
			calcientation.mul(new Quaternion().setFromAxis(0, 0, -1, -.2f));
		if (f > 0.001)
			calcientation.mul(new Quaternion().setFromAxis(0, 0, -1, .2f));
		
		speed.lerp(new Vector3(0, 0, 0), 0.001f);

	}

	public void setPoints(int points) {
		this.points = Integer.toString(points);
	}
}