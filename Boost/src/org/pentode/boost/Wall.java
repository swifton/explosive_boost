package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
	Body body;

	public Wall (float x, float y, float w, float h, World world) {
		BodyDef wallBodyDef; 
		   
		wallBodyDef =new BodyDef();
		wallBodyDef.position.set(new Vector2((x + w - 1f)*0.1f, (y + h - 1f)*0.1f));
		PolygonShape wallBox;
		wallBox = new PolygonShape();
		wallBox.setAsBox((w - x + 1) * 0.1f, (h - y + 1) * 0.1f);
		body = world.createBody(wallBodyDef);  
		
		body.createFixture(wallBox, 0.0f); 
		wallBox.dispose();
		
		
	}
}
