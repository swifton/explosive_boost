package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Explosion {
	int numRays = 20;
	Body [] bullets = new Body[numRays];
	int cleanupDelay = 25;

	public Explosion(float blastPower, Vector2 center, World world) {
		   
		   float DEGTORAD = (float)Math.PI/180;
		   
		   for (int i = 0; i < numRays; i++) {
			      float angle = (i / (float)numRays) * 360 * DEGTORAD;
			      Vector2 rayDir = new Vector2((float)Math.sin(angle),(float)Math.cos(angle));
			  
			      BodyDef bd = new BodyDef();
			      bd.type = BodyType.DynamicBody;;
			      bd.fixedRotation = true; // rotation not necessary
			      bd.bullet = true; // prevent tunneling at high speed
			      bd.linearDamping = 10; // drag due to moving through air
			      bd.gravityScale = 0; // ignore gravity
			      bd.position.set(center); // start at blast center
			      bd.linearVelocity.set(new Vector2(blastPower * rayDir.x, blastPower * rayDir.y));
			      Body body = world.createBody(bd);
			  
			      CircleShape circle = new CircleShape();
			      circle.setRadius(0.05f); // very small
			  
			      FixtureDef fd = new FixtureDef();
			      fd.shape = circle;
			      fd.density = 60 / (float)numRays; // very high - shared across all particles
			      fd.friction = 0; // friction not necessary
			      fd.restitution = 0.99f; // high restitution to reflect off obstacles
			      fd.filter.groupIndex = -1; // particles should not collide with each other
			      Fixture fixture = body.createFixture(fd);
			      
			      bullets[i] = body;
			  }
	   }

	public void dispose(World world) {
		for (int k = 0; k < bullets.length; k++) {
			   world.destroyBody(bullets[k]);
		   }
	}
}
