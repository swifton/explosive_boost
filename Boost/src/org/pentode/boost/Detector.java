package org.pentode.boost;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

public class Detector {
	RayCastCallback callBack;
	Body toDetect;
	Body lastBody;
	Vector2 p1, p2, p3;
	Sprite sprite;
	static final float BOX_TO_WORLD = 200f;
	boolean on = true;

	
	public Detector(int x, int y, int d, Body tD, Texture texture) {
		toDetect = tD;
		
		p1 = new Vector2((float) x / 5 - 0.1f, (float) y / 5 - 0.1f);
		if (d == 0) p3 = new Vector2(p1.x, 50);
		if (d == 1) p3 = new Vector2(50, p1.y);
		if (d == 2) p3 = new Vector2(p1.x, -1);
		if (d == 3) p3 = new Vector2(-1, p1.y);
		p2 = new Vector2(p3.x, p3.y);
		
	    sprite = new Sprite(texture, 0, 0, 64, 64);
	    sprite.setSize(40, 40);
	    sprite.setPosition(p1.x * BOX_TO_WORLD - 20, p1.y * BOX_TO_WORLD - 20);
	    sprite.setRotation(90 - d * 90);
		
		callBack = new RayCastCallback() {
			   @Override
			   public float reportRayFixture(Fixture fix, Vector2 p, Vector2 normal, float fraction) {
				   fuckJava(p, fix.getBody());
				   return -1f;
			   }
		};
	}
	
	 
	private void fuckJava(Vector2 p, Body body) {
		if ((Math.abs(p.x - p1.x) < Math.abs(p2.x - p1.x)) || (Math.abs(p.y - p1.y) < Math.abs(p2.y - p1.y))) {
			p2.x = p.x;
			p2.y = p.y;
			lastBody = body;
		}
	}
	
	public boolean detect(World world) {
		p2.x= p3.x;
	    p2.y= p3.y;
	    world.rayCast(callBack , p1, p2);
	    if (!on) return false;
  	  	if (lastBody == toDetect) {
  	  		lastBody = null;
			return true;
		}
  	  	return false;
	}
	public void draw(SpriteBatch batch, ShapeRenderer renderer) {
		renderer.begin(ShapeType.Line);
	    renderer.setColor(Color.RED);
	    renderer.line(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD, p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	    renderer.end();
	      
	    batch.begin();
	    sprite.draw(batch);
  	  	batch.end();
	}
}
