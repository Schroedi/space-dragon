package com.chris.spacedragon.client;

import com.chris.spacedragon.Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(840, 480);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new Game();
	}
}