package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Bomb {
	Body body;
	int givenCountdownTime;
	int countdownTime;
	
	  public Bomb(float x, float y, World world) {
		   createBody(x, y, world);
	   }
	  
	  public void createBody(float x, float y, World world) {
		  BodyDef bombDef;	   
		   
		   FixtureDef bombFixtureDef;
		   Fixture bombFixture;
		   
		   bombDef = new BodyDef();
		   bombDef.type = BodyType.DynamicBody;
		   bombDef.position.set(new Vector2(x, y));
		   
		   PolygonShape bombBox;
		   bombBox = new PolygonShape();
		   bombBox.setAsBox(0.5f, 0.5f);
		   
		   bombFixtureDef = new FixtureDef();
		   bombFixtureDef.shape = bombBox;
		   bombFixtureDef.density = 0.5f; 
		   bombFixtureDef.friction = 0.4f;
		   bombFixtureDef.restitution = 0.3f;
		   
		   body = world.createBody(bombDef);
		   bombFixture = body.createFixture(bombFixtureDef);
		   bombBox.dispose(); 
	  }
	  
	  public void reset(float posX, float posY) {
		   body.setTransform(posX, posY, 0);
		   body.setLinearVelocity(new Vector2(0,0));
		   body.setAngularVelocity(0);
		   body.setAwake(true);
	  }
}
