package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Textures {
	Texture crateT;
	Texture crateTarget;
	Texture brickT;
	Texture metal;
	Texture ballT;
	Texture detT;
	
	public Textures() {
		crateT = new Texture(Gdx.files.internal("crate.jpg"));
		crateTarget = new Texture(Gdx.files.internal("crate_no_timer.jpg"));
		brickT = new Texture(Gdx.files.internal("brick043.gif"));
		metal = new Texture(Gdx.files.internal("metal.jpg"));
		ballT = new Texture(Gdx.files.internal("ball.png"));
		detT = new Texture(Gdx.files.internal("detector.png"));
	}
	
	public void dispose() {
		crateT.dispose();
		crateTarget.dispose();
		brickT.dispose();
		metal.dispose();
		ballT.dispose();
		detT.dispose();
	}
}
