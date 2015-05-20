package org.pentode.boost.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Mine extends Movable {
	Sprite mineSprite;
	public Sound sound;

	
	public Mine(int x, int y, World wrld, Stage stage, SpriteBatch batch, float BTW) {
		super(x, y, BTW, stage, wrld);
		world = wrld;
		createBody(world, new float[]{startX, startY, 0.5f, 0.4f, 0.3f, 0.3f - 0.01f}, false, "circle", "mine");
		
		AABBCallback = new QueryCallback() {
			public boolean reportFixture(Fixture fixture) {
				if (fixture.getBody() != body) {
					droppable = false;
					return false;
				}
				return true;
			}
		};
	}
/*	  
	public void createBody(World wrld) {
		world = wrld;
		BodyDef bodyDef = new BodyDef();   
		   
		FixtureDef fixtureDef;
		 
		//mineDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(startX, startY));
		   
		CircleShape circle;
		circle = new CircleShape();
		circle.setRadius(0.3f - 0.01f);
		   
		fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.3f;
		   
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		circle.dispose();
		body.setUserData("mine");
		  
		
	}
	*/
	public void createSprite(Texture texture) {
		mineSprite = new Sprite(texture, 28, 26, 443, 444);
		mineSprite.setSize(cellSize * 3, cellSize * 3);
		mineSprite.setOrigin(cellSize * 3/2, cellSize * 3/2);
	}
	
	public int getCoordX() {
		return (int) Math.floor((startX + 0.10001f) / 0.2f);
	}
	
	public int getCoordY() {
		return (int) Math.floor((startY + 0.10001f) / 0.2f);
	}	
	
	public void align() {
		body.setTransform(new Vector2(startX, startY), 0);
		alignImage();
	}
	
	public void reset(float posX, float posY) {
		body.setTransform(posX, posY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
	  
	public void draw(SpriteBatch batch) {
		if (body == null) return;
		mineSprite.setPosition(body.getPosition().x * BTWORLD - cellSize * 3/2, body.getPosition().y * BTWORLD - cellSize * 3/2);
		mineSprite.setRotation((float) (body.getAngle() * 180 / Math.PI + 45));
	    batch.begin();
	    mineSprite.draw(batch);
	    batch.end();
	}
	
	public Explosion explode() {
		if (body == null) return null;
		if (body.getUserData() != "explode") return null;
		Explosion explosion = new Explosion(12, body.getPosition(), world, BTWORLD);
		System.out.println(body);
		System.out.println(world);
		world.destroyBody(body);
		sound.play();
		body = null;
		return explosion;
	}
}
