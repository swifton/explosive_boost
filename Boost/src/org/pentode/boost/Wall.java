package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
	Body body;

	public Wall (int x1, int y1, int x2, int y2, float angle, World world) {
		float bodyX = (x1 + x2 - 1)*0.1f;
		float bodyY = (y1 + y2 - 1)*0.1f;
		
		BodyDef wallBodyDef; 
		   
		wallBodyDef =new BodyDef();
		wallBodyDef.position.set(new Vector2(bodyX, bodyY));
		PolygonShape wallBox;
		wallBox = new PolygonShape();
		wallBox.setAsBox((x2 - x1 + 1) * 0.1f, (y2 - y1 + 1) * 0.1f);
		body = world.createBody(wallBodyDef);  
		
		body.createFixture(wallBox, 0.0f); 
		wallBox.dispose();
		body.setTransform(bodyX, bodyY, angle);
	}
}
