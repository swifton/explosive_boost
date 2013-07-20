package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Brick {
	Body body;
	float centerX;
	float centerY;
	
	public Brick(int x1, int y1, int x2, int y2, World world) {
		centerX = (x1 + x2 - 1)*0.1f;
		centerY = (y1 + y2 - 1)*0.1f;
		
		BodyDef def = new BodyDef(); 
		FixtureDef fixtureDef = new FixtureDef();

		def.type = BodyType.DynamicBody;
		def.position.set(new Vector2(centerX, centerY));
		
		PolygonShape box = new PolygonShape();
		box.setAsBox((x2 - x1 + 1) * 0.1f, (y2 - y1 + 1) * 0.1f);
		
		fixtureDef.shape = box;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.01f;

		body = world.createBody(def);
		body.createFixture(fixtureDef);

		box.dispose();
	}
	
	public void reset() {
		body.setTransform(centerX, centerY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
}
