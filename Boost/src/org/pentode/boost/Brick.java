package org.pentode.boost;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	Sprite sprite;
	int vertical;
	static final float BOX_TO_WORLD = 200f;

	
	public Brick(int x1, int y1, int x2, int y2, World world) {
		centerX = (x1 + x2 - 1)*0.1f;
		centerY = (y1 + y2 - 1)*0.1f;
		
		if (x2 == x1) vertical = 1;
		else vertical = 0;
		
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
	
	public void draw(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x * BOX_TO_WORLD - 40, body.getPosition().y * BOX_TO_WORLD - 20);
  	  	sprite.setRotation((float) (body.getAngle() * 180 / Math.PI + vertical * 90));
	    batch.begin();
	    sprite.draw(batch);
	    batch.end();
	}
}
