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
	static final float BOX_TO_WORLD = 200f;
	Sprite sprite;
	Body body;
	
	 public Ball(World world, Vector2 ballInitialPosition, Texture texture) {
		 sprite = new Sprite(texture, 0, 0, 80, 80);
		 
		 BodyDef ballDef;	   
		 FixtureDef fixtureDef;
		 
		 ballDef = new BodyDef();
		 ballDef.type = BodyType.DynamicBody;
		 ballDef.position.set(ballInitialPosition.x, ballInitialPosition.y);
		      
		 CircleShape circle;
		 circle = new CircleShape();
		 circle.setRadius(0.2f);
		  
		 fixtureDef = new FixtureDef();
		
		 body  = world.createBody(ballDef);
		
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
		 sprite.setPosition(body.getPosition().x * BOX_TO_WORLD - 40, body.getPosition().y * BOX_TO_WORLD - 40);
	     sprite.setRotation((float) (body.getAngle() * 180 / Math.PI));
	     batch.begin();
	     sprite.draw(batch);
	     batch.end();
	 }
}
