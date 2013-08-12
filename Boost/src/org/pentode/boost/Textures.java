package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Textures {
	Texture crateT;
	Texture brickT;
	Texture metal;
	Texture ballT;
	Texture detT;
	
	public Textures() {
		crateT = new Texture(Gdx.files.internal("crate.jpg"));
		brickT = new Texture(Gdx.files.internal("brick043.gif"));
		metal = new Texture(Gdx.files.internal("metal.jpg"));
		ballT = new Texture(Gdx.files.internal("ball.png"));
		detT = new Texture(Gdx.files.internal("detector.png"));
	}

}
