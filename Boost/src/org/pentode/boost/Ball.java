package org.pentode.boost;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
	static float BOX_TO_WORLD;
	static float cellSize;
	Sprite sprite;
	Body body;
	Vector2 initialPos;
	
	public Ball(World world, Vector2 ballInitialPosition, Texture texture, float BTW) {
		BOX_TO_WORLD = BTW;
		cellSize = BTW / 5;
		
		sprite = new Sprite(texture, 0, 0, 80, 80);
	    sprite.setSize(cellSize * 2, cellSize * 2);
	    sprite.setOrigin(cellSize, cellSize);

		initialPos = new Vector2(ballInitialPosition.x, ballInitialPosition.y);
		 
		createBody(world);
	}
	
	public void createBody(World world) {
		BodyDef ballDef;	   
		FixtureDef fixtureDef;
		 
		ballDef = new BodyDef();
		ballDef.type = BodyType.DynamicBody;
		ballDef.position.set(initialPos.x, initialPos.y);
		      
		CircleShape circle;
		circle = new CircleShape();
		circle.setRadius(0.2f);
		  
		fixtureDef = new FixtureDef();
		
		body  = world.createBody(ballDef);
		body.setUserData("ball");

		
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.8f;
		
		body.createFixture(fixtureDef);
		circle.dispose(); 
	}
	 
	public void reset(float posX, float posY) {
		body.setTransform(posX, posY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
	 
	public void draw(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x * BOX_TO_WORLD - cellSize, body.getPosition().y * BOX_TO_WORLD - cellSize);
	    sprite.setRotation((float) (body.getAngle() * 180 / Math.PI));
	    batch.begin();
	    sprite.draw(batch);
	    batch.end();
	}
}
