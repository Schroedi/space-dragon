package com.chris.spacedragon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class ChaseCam extends PerspectiveCamera {

	public Vector3 lookAtPosition;
	public Quaternion lookAtOrientation;

	public void update(float dt) {
		Vector3 campos = lookAtPosition.cpy();
		campos.z += 10;
		campos.y += 5;

		position.set(campos);
		lookAt(lookAtPosition.x, lookAtPosition.y, lookAtPosition.z);
		update();
	}

	public ChaseCam(Vector3 destPos, Quaternion destOrient) {
		super(75f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		lookAtPosition = destPos;
		lookAtOrientation = destOrient;

		position.set(0, 1.5f, 1);
		lookAt(0, 0, 0);
		up.set(0, 1, 0);
		update();

	}

}
