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
		wallBodyDef.position.set(new Vector2(x, y));
		PolygonShape wallBox;
		wallBox = new PolygonShape();
		wallBox.setAsBox(w, h);
		body = world.createBody(wallBodyDef);  
		
		body.createFixture(wallBox, 0.0f); 
		wallBox.dispose();
		
		
	}
}
