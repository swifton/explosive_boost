package org.pentode.boost.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SolidBody {
	public Body body;
	
	public void createBody(World world, float[] dimensions, Boolean isDynamic, String shape, String name) {
		// dimensions = [positionX, positionY, density, friction, restitution, width/radius, height]
		
		BodyDef bodyDef = new BodyDef();   
		FixtureDef fixtureDef = new FixtureDef();
		
		bodyDef.position.set(new Vector2(dimensions[0], dimensions[1]));
		

		if (isDynamic) {
			bodyDef.type = BodyType.DynamicBody;
			fixtureDef.density = dimensions[2]; 
			fixtureDef.friction = dimensions[3];
			fixtureDef.restitution = dimensions[4];
		}
		else {
			
		}
		
		PolygonShape box = new PolygonShape();
		CircleShape circle = new CircleShape();
		
		if (shape == "box") {
			box.setAsBox(dimensions[5], dimensions[6]);
			fixtureDef.shape = box;
		}
		else if (shape == "circle") {			
			circle.setRadius(dimensions[5]);
			fixtureDef.shape = circle;
		}
		else {
			System.out.println("You fucked up the shape property of some body.");
		}
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(name);

		circle.dispose();
		box.dispose();
			
	}
}
