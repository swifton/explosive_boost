package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	Sound explosionSound;
	Sound ballSound;
	Sound detectorSound;
	Sound brickSound;
	
	public Sounds() {
		   explosionSound= Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
		   ballSound= Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
		   detectorSound= Gdx.audio.newSound(Gdx.files.internal("detector.wav"));
		   brickSound= Gdx.audio.newSound(Gdx.files.internal("brick.wav"));
	}
}
