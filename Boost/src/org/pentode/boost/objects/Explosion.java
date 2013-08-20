package org.pentode.boost.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Explosion {
	int numRays = 20;
	Body [] bullets = new Body[numRays];
	public int cleanupDelay = 25;
	PooledEffect [] particles = new PooledEffect[numRays];
	float BOX_TO_WORLD;


	public Explosion(float blastPower, Vector2 center, World world, float BTW) {
		BOX_TO_WORLD = BTW;
	
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
			body.createFixture(fd);
			    
			bullets[i] = body;
		}
		   
		createParticles();
	}
	
	private void createParticles() {
		ParticleEffect explosionEffect = new ParticleEffect();
		ParticleEffectPool pool;
		
		explosionEffect.load(Gdx.files.internal("effects/explosion.pp"), Gdx.files.internal(""));
		pool = new ParticleEffectPool(explosionEffect, 1, 2);
		
		PooledEffect effect;
		for (int i = 0; i < numRays; i++) {
			effect = pool.obtain();
			particles[i] = effect;
		}
	}
	
	public void draw(SpriteBatch batch) {
		PooledEffect effect;
		for (int i = 0; i < numRays; i++) {
			effect = particles[i];
			Vector2 v = bullets[i].getPosition();
			effect.setPosition(v.x * BOX_TO_WORLD, v.y * BOX_TO_WORLD);
			
			batch.begin();
			effect.draw(batch, 0.01f);
			batch.end();
		}
	}

	public void dispose(World world) {
		if (bullets[0].getWorld() != world) return;
		for (int k = 0; k < bullets.length; k++) {
			world.destroyBody(bullets[k]);
		   }
	}
}
