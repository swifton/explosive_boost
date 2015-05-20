package org.pentode.boost.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Brick extends SolidBody {
	//public Body body;
	public float centerX;
	public float centerY;
	public float width;
	public float height;
	public Sprite sprite;
	int vertical;
	int x1, x2, y1, y2;
	static float BOX_TO_WORLD;
	static float cellSize;
	
	public Brick(int x11, int y11, int x22, int y22, World world, float BTW) {
		BOX_TO_WORLD = BTW;
		cellSize = BTW / 5;
		
		x1 = x11;
		x2 = x22;
		y1 = y11;
		y2 = y22;
		
		centerX = (x1 + x2 - 1)*0.1f;
		centerY = (y1 + y2 - 1)*0.1f;
		width = (x2 - x1 + 1) * 0.1f;
		height = (y2 - y1 + 1) * 0.1f;
		
		if (x2 == x1) vertical = 1;
		else vertical = 0;
		
		createBody(world, new float[] {centerX, centerY, 0.5f, 0.7f, 0.01f, width, height}, true, "box", "brick");
	}
	
	public void reset() {
		body.setTransform(centerX, centerY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
	
	public void draw(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x * BOX_TO_WORLD - cellSize, body.getPosition().y * BOX_TO_WORLD - cellSize/2);
  	  	sprite.setRotation((float) (body.getAngle() * 180 / Math.PI + vertical * 90));
	    batch.begin();
	    sprite.draw(batch);
	    batch.end();
	}
}
