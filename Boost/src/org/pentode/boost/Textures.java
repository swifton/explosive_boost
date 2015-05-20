package org.pentode.boost;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Textures {
	Texture crateT;
	Texture mineT;
	Texture crateTarget;
	Texture brickT;
	Texture metal;
	Texture ballT;
	Texture detT;
	Texture detO;
	Texture wallEnding;
	public Texture play;
	public Texture stop;
	public Texture droplet;
	public Texture beam;
	public Texture greenBeam;
	
	public Textures() {
		crateT = new Texture(Gdx.files.internal("crate.jpg"));
		mineT = new Texture(Gdx.files.internal("mine.png"));
		crateTarget = new Texture(Gdx.files.internal("crate_no_timer.png"));
		brickT = new Texture(Gdx.files.internal("brick.png"));
		metal = new Texture(Gdx.files.internal("wall.png"));
		ballT = new Texture(Gdx.files.internal("ball.png"));
		detT = new Texture(Gdx.files.internal("detector.png"));
		detO = new Texture(Gdx.files.internal("detector3.png"));
		wallEnding = new Texture(Gdx.files.internal("wallending.png"));
		play = new Texture(Gdx.files.internal("play.png"));
		stop = new Texture(Gdx.files.internal("stop.png"));
		droplet = new Texture(Gdx.files.internal("droplet.png"));
		beam = new Texture(Gdx.files.internal("beam.png"));
		greenBeam = new Texture(Gdx.files.internal("beam-green.png"));
	}
	
	public void dispose() {
		crateT.dispose();
		mineT.dispose();
		crateTarget.dispose();
		brickT.dispose();
		metal.dispose();
		ballT.dispose();
		detT.dispose();
		wallEnding.dispose();
		play.dispose();
		stop.dispose();
		droplet.dispose();
		beam.dispose();
		greenBeam.dispose();
	}
}
