package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {

	Body body;
	
	 public Ball(World world) {
		   BodyDef ballDef;	   
		   FixtureDef fixtureDef;
		   Fixture fixture; 
		   
		   ballDef = new BodyDef();
		   ballDef.type = BodyType.DynamicBody;
		   ballDef.position.set(1, 2);
		      
		   CircleShape circle;
		   circle = new CircleShape();
		   circle.setRadius(0.2f);
		  
		   fixtureDef = new FixtureDef();
		
		   body  = world.createBody(ballDef);
		
		   fixtureDef.shape = circle;
		   fixtureDef.density = 0.5f; 
		   fixtureDef.friction = 0.4f;
		   fixtureDef.restitution = 0.8f;
		
		   fixture = body.createFixture(fixtureDef);
		   circle.dispose(); 
	   }
	 
	 public void reset(float posX, float posY) {
		   body.setTransform(posX, posY, 0);
		   body.setLinearVelocity(new Vector2(0,0));
		   body.setAngularVelocity(0);
		   body.setAwake(true);
	  }
}
