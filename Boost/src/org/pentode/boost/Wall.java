package org.pentode.boost;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
	Body body;
	float [] angles;
	Sprite[] sprites;
	int x1, x2, y1, y2, angle;
	
	static float BOX_TO_WORLD;
	static float cellSize;

	public Wall (int x11, int y11, int x22, int y22, int a, World world, float BTW) {
		BOX_TO_WORLD = BTW;
		cellSize = BTW / 5;
		
		x1 = x11;
		x2 = x22;
		y1 = y11;
		y2 = y22;
		angle = a;
		
		angles = new float[] {0, (float) Math.PI/6, (float) (Math.PI/4), (float) (Math.PI/3), (float) (2 * Math.PI/3), (float) (3*Math.PI/4), (float) (5*Math.PI/6)};
		
		createBody(world);
	}
	
	public void createBody(World world) {
		float bodyX = (x1 + x2 - 1)*0.1f + 0.005f;
		float bodyY = (y1 + y2 - 1)*0.1f + 0.005f;
		BodyDef wallBodyDef; 
		   
		wallBodyDef =new BodyDef();
		wallBodyDef.position.set(new Vector2(bodyX, bodyY));
		PolygonShape wallBox;
		wallBox = new PolygonShape();
		wallBox.setAsBox((x2 - x1 + 1) * 0.1f - 0.005f, (y2 - y1 + 1) * 0.1f - 0.005f);
		body = world.createBody(wallBodyDef);  
		
		body.createFixture(wallBox, 0.0f); 
		wallBox.dispose();
		body.setTransform(bodyX, bodyY, angles[angle]);
	}
	
	public void createSprites(int x1, int y1, int x2, int y2, int angle, Texture metal) {
		int k;

		if (x1 == x2) {
			int s = (int) Math.floor((y2 - y1 + 1) * cellSize/123);
			sprites = new Sprite[s + 1];
			for (k = 0; k < s; k++) {
				createOneSprite(cellSize, 123, x1 * cellSize - cellSize, y1 * cellSize - cellSize + 123 * k, k, metal, 0, 0, 0);
			}
			createOneSprite(cellSize, (y2 - y1 + 1) * cellSize - k * 123, x1 * cellSize - cellSize, y1 * cellSize - cellSize + 123 * k, k, metal, 0, 0, 0);
		}
		   
		if (y1 == y2) {
			int s = (int) Math.floor((x2 - x1 + 1) * cellSize/103);
			sprites = new Sprite[s + 1];
			for (k = 0; k < s; k++) {
				createOneSprite(103, cellSize, x1 * cellSize - cellSize + 103 * k, y1 * cellSize - cellSize, k, metal, (x2 + x1 - 1) * cellSize/2 - (x1 - 1) * cellSize - k * 103, cellSize/2, angle);
			}
			createOneSprite((x2 - x1 + 1) * cellSize - k * 103, cellSize, x1 * cellSize - cellSize + 103 * k, y1 * cellSize - cellSize, k, metal, (x2 + x1 - 1) * cellSize/2 - (x1 - 1) * cellSize - k * 103, cellSize/2, angle);
		}
	}
	
	private void createOneSprite(float wid, float hei, float posX, float posY, int k, Texture metal, float orX, float orY, int angle) { 
		Sprite sprite;
		sprite = new Sprite(metal, 0, 0, (int) wid, (int) hei);
		sprite.setPosition(posX, posY);
		sprite.setOrigin(orX, orY);
		sprite.setRotation((float) (angles[angle] * 180 / Math.PI));
		sprites[k] = sprite;
	}
	
	public void draw(SpriteBatch batch) {
		for (int k = 0; k < sprites.length; k++) {
			batch.begin();
		    sprites[k].draw(batch);
		    batch.end();
		}
	}
}
