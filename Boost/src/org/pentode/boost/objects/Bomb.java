package org.pentode.boost.objects;

import org.pentode.boost.RotatableText;
import org.pentode.boost.ui.TimeWindow;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Bomb extends Movable {
	Body body;
	public int seconds;
	public int centiSeconds;
	int currentSeconds;
	int currentCentiSeconds;
	public int countdownTime;
	TimeWindow timeWindow;
	Sprite crate;
	RotatableText time;
	public Sound sound;
	
	public Bomb(int x, int y, World wrld, Stage stage, TimeWindow w, int sec, int cen, SpriteBatch batch, float BTW, BitmapFont font) {
		super(x, y, BTW, stage, wrld);
		seconds = sec;
		centiSeconds = cen;
		timeWindow = w;

		createBody(world);
		resetCurrentTime();
		time = new RotatableText("00:00", batch, cellSize, font);	
		
		AABBCallback = new QueryCallback() {
			public boolean reportFixture(Fixture fixture) {
				if (fixture.getBody() != body) {
					droppable = false;
					return false;
				}
				return true;
			}
		};
	}
	  
	public void createBody(World wrld) {
		world = wrld;
		BodyDef bodyDef = new BodyDef();	   
		   
		FixtureDef fixtureDef;
		  
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(startX, startY));
		   
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.29f, 0.29f);
		   
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.3f;
		   
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		shape.dispose();
		body.setUserData("bomb");
		  

	}
	
	public void createSprite(Texture texture) {
		crate = new Sprite(texture, 28, 26, 443, 444);
		crate.setSize(cellSize * 3, cellSize * 3);
		crate.setOrigin(cellSize * 3/2, cellSize * 3/2);
	}
	
	public int getCoordX() {
		return (int) Math.floor((startX + 0.10001f) / 0.2f);
	}
	
	public int getCoordY() {
		return (int) Math.floor((startY + 0.10001f) / 0.2f);
	}
	
	public void tu() {
	    timeWindow.time.setText(time());
	    timeWindow.window.setVisible(true);
	    passBomb();
	}
	
	public void align() {
		body.setTransform(new Vector2(startX, startY), 0);
		alignImage();
	}
	
	public void alignImage() {sourceImage.setBounds(startX * BTWORLD - cellSize * 1.5f, startY * BTWORLD - cellSize * 1.5f, cellSize * 3, cellSize * 3);}

	private void passBomb() {timeWindow.bomb = this;}

	public void reset(float posX, float posY) {
		body.setTransform(posX, posY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
	  
	public void updateTime() {
		float timeInCentiSeconds = (float) countdownTime * 100 / 60;
		currentCentiSeconds = (int) (timeInCentiSeconds % 100);
		currentSeconds = (int) Math.floor(timeInCentiSeconds / 100);
	}
	  
	public void resetCurrentTime() {
		currentCentiSeconds = centiSeconds;
		currentSeconds = seconds;
	}
	  
	public void draw(SpriteBatch batch) {
		if (body == null) return;
		crate.setPosition(body.getPosition().x * BTWORLD - cellSize * 3/2, body.getPosition().y * BTWORLD - cellSize * 3/2);
	    crate.setRotation((float) (body.getAngle() * 180 / Math.PI));
	    batch.begin();
	    crate.draw(batch);
	    batch.end();
	    drawLabel();
	}
	
	public void countDown(Array<Explosion> explosions) {
		if (countdownTime == 0) {
    		Explosion explosion = new Explosion(23, body.getPosition(), world, BTWORLD);
    		explosions.add(explosion);
    		world.destroyBody(body);
    		sound.play();
    	}
		
    	if (countdownTime > -1) {
	    	countdownTime -= 1;
	    	updateTime();
    	}
    	
    	if (countdownTime == -1) {
    		body = null;
    	}
	}
	
	public void drawLabel() {
		time.text =  time();
		
		double r = 25.9f * Math.sqrt(5) * (cellSize / 40);
		double alpha = body.getAngle() + Math.PI * 9 / 10;
		float dx = (float) (r * Math.cos(alpha));
		float dy = (float) (r * Math.sin(alpha));
		time.draw(body.getPosition().x * BTWORLD + dx, body.getPosition().y * BTWORLD + dy, (float) (body.getAngle() * 180 / Math.PI), cellSize);
	}
	
	private String time() {
		return timeToString(currentSeconds, currentCentiSeconds);
	}
	
	public String givenTime() {
		return timeToString(seconds, centiSeconds);
	}
	
	private String timeToString(int secondss, int centiSecondss) {
		String sec = Integer.toString(secondss);
		if (secondss < 10) sec = "0" + sec;
		String cen = Integer.toString(centiSecondss);
		if (centiSecondss < 10) cen = "0" + cen;
		return sec + ":" + cen;
	}	
}