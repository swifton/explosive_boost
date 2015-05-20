package org.pentode.boost;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	Sprite spriteO;
	Sprite beam;
	Sprite greenBeam;
	static float BOX_TO_WORLD;
	static float cellSize;
	int dir;

	boolean on = true;
	
	public Detector(int x, int y, int d, Body tD, Textures textures, float BTW) {
		dir = d;
		BOX_TO_WORLD = BTW;
		cellSize = BTW / 5;
		
		toDetect = tD;
		
		p1 = new Vector2((float) x / 5 - 0.1f, (float) y / 5 - 0.1f);
		if (d == 0) p3 = new Vector2(p1.x, 50);
		if (d == 1) p3 = new Vector2(50, p1.y);
		if (d == 2) p3 = new Vector2(p1.x, -1);
		if (d == 3) p3 = new Vector2(-1, p1.y);
		p2 = new Vector2(p3.x, p3.y);
		
	    sprite = new Sprite(textures.detT, 0, 0, 64, 64);
	    sprite.setSize(cellSize, cellSize);
	    sprite.setPosition(p1.x * BOX_TO_WORLD - cellSize/2, p1.y * BOX_TO_WORLD - cellSize/2);
	    
	    spriteO = new Sprite(textures.detO, 0, 0, 64, 64);
	    spriteO.setSize(cellSize, cellSize);
	    spriteO.setPosition(p1.x * BOX_TO_WORLD - cellSize/2, p1.y * BOX_TO_WORLD - cellSize/2);
	    
	    beam = new Sprite(textures.beam, 0, 0, 2, 2);
	    greenBeam = new Sprite(textures.greenBeam, 0, 0, 2, 2);
		
		callBack = new RayCastCallback() {
			   @Override
			   public float reportRayFixture(Fixture fix, Vector2 p, Vector2 normal, float fraction) {
				   changep2(p, fix.getBody());
				   return -1f;
			   }
		};
	}
	
	 
	private void changep2(Vector2 p, Body body) {		
		if (p1.dst(p) < p1.dst(p2)) {
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
  	  	if (lastBody.getUserData() == "ball") {
  	  		lastBody = null;
			return true;
		}
  	  	return false;
	}
	public void draw(SpriteBatch batch) {
	    if (dir == 0) {
	    	beam.setSize(2, (p2.y - p1.y) * BOX_TO_WORLD);
	    	beam.setPosition(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD);
	    	greenBeam.setSize(2, (p2.y - p1.y) * BOX_TO_WORLD);
	    	greenBeam.setPosition(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD);
	    }
	    
	    if (dir == 1) {
	    	beam.setSize((p2.x - p1.x) * BOX_TO_WORLD, 2);
	    	beam.setPosition(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD);
	    	greenBeam.setSize((p2.x - p1.x) * BOX_TO_WORLD, 2);
	    	greenBeam.setPosition(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD);
	    }
	    
	    if (dir == 2) {
	    	beam.setSize(2, (p1.y - p2.y) * BOX_TO_WORLD);
	    	beam.setPosition(p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	    	greenBeam.setSize(2, (p1.y - p2.y) * BOX_TO_WORLD);
	    	greenBeam.setPosition(p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	    }
	    
	    if (dir == 3) {
	    	beam.setSize((p1.x - p2.x) * BOX_TO_WORLD, 2);
	    	beam.setPosition(p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	    	greenBeam.setSize((p1.x - p2.x) * BOX_TO_WORLD, 2);
	    	greenBeam.setPosition(p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	    }
	      
	    batch.begin();
	    
	    if (on) {
	    	sprite.draw(batch);
	    	beam.draw(batch);
	    }
	    else {
	    	spriteO.draw(batch);
	    	greenBeam.draw(batch);
	    }
  	  	batch.end();
	}
}
