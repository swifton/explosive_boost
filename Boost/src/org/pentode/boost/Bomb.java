package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;

public class Bomb {
	Body body;
	int seconds;
	int centiSeconds;
	int currentSeconds;
	int currentCentiSeconds;
	int countdownTime;
	float startX, startY;
	Stage stagee;
	boolean touched;
	boolean play = false;
	TimeWindow timeWindow;
	static float BTWORLD;
	static float cellSize;
	boolean droppable = true;
	QueryCallback AABBCallback;
	World world;
	Sprite crate;
	RotatableText time;
	Sound sound;
	DragAndDrop dragAndDrop;
	
	public Bomb(int x, int y, World wrld, Stage stage, TimeWindow w, int sec, int cen, SpriteBatch batch, float BTW, BitmapFont font) {
		BTWORLD = BTW;
		cellSize = BTW / 5;
		seconds = sec;
		centiSeconds = cen;
		world = wrld;
		timeWindow = w;
		stagee = stage;
		startX = x * 0.2f - 0.1f;
		startY = y * 0.2f - 0.1f;
		createBody(world);
		createDragDrop(startX, startY, stage);
		resetCurrentTime();
		time = new RotatableText("00:00", batch, cellSize, font);		
	}
	  
	public void createBody(World wrld) {
		world = wrld;
		BodyDef bombDef;	   
		   
		FixtureDef bombFixtureDef;
		 
		bombDef = new BodyDef();
		bombDef.type = BodyType.DynamicBody;
		bombDef.position.set(new Vector2(startX, startY));
		   
		PolygonShape bombBox;
		bombBox = new PolygonShape();
		bombBox.setAsBox(0.29f, 0.29f);
		   
		bombFixtureDef = new FixtureDef();
		bombFixtureDef.shape = bombBox;
		bombFixtureDef.density = 0.5f; 
		bombFixtureDef.friction = 0.4f;
		bombFixtureDef.restitution = 0.3f;
		   
		body = world.createBody(bombDef);
		body.createFixture(bombFixtureDef);
		bombBox.dispose(); 
		  
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
	  
	private void createDragDrop(float x, float y, Stage stage) {
	    dragAndDrop = new DragAndDrop();
	        
	    final Skin skinn = new Skin();
		skinn.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
		skinn.add("badlogic", new Texture("droplet.png"));

		final Image sourceImage = new Image(skinn, "badlogic");
		sourceImage.setBounds(x * BTWORLD - cellSize * 1.5f, y * BTWORLD - cellSize * 1.5f, cellSize * 3, cellSize * 3);
		stage.addActor(sourceImage);
			
		sourceImage.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			    if (play) return true;
			    touched = true;
			    return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			   	if (play) return;
			    if (touched) {
			    	timeWindow.time.setText(time());
			    	timeWindow.window.setVisible(true);
			    	passBomb();
			    }
			    touched = false;
			}
		});
	        
		dragAndDrop.addSource(new Source(sourceImage) {
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				if (play) return null;
				touched = false;
				Payload payload = new Payload();
				payload.setObject("crap");
				payload.setDragActor(new Label("", skinn));
				return payload;
			}
			
			public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Target target) {
				if (play) return;
				droppable = true;
				
				if (x < cellSize && y < cellSize) System.out.println("Xyu");

				float myx = x + sourceImage.getX();
				float myy = y + sourceImage.getY();
				float newStartX = ((float)Math.round((myx * 5)/BTWORLD))/5 - 0.1f;
				float newStartY = ((float)Math.round((myy * 5)/BTWORLD))/5 - 0.1f;
				myx = newStartX * BTWORLD;
				myy = newStartY * BTWORLD;
					
			    world.QueryAABB(AABBCallback, newStartX - 0.1f, newStartY - 0.1f, newStartX + 0.1f, newStartY + 0.1f);
			    if (!droppable) {
			    	System.out.println("Xyu BAM");
			    	return;
			    }
			    
			    startX = newStartX;
			    startY = newStartY;
				body.setTransform(new Vector2(startX, startY), 0);
				sourceImage.setBounds(startX * BTWORLD - cellSize * 1.5f, startY * BTWORLD - cellSize * 1.5f, cellSize * 3, cellSize * 3);
			}
		});
	}

	private void passBomb() {
		timeWindow.bomb = this;
	}

	public void reset(float posX, float posY) {
		body.setTransform(posX, posY, 0);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setAwake(true);
	}
	  
	public void updateTime() {
		float timeInCentiSeconds = (float) countdownTime * 100 / 60;
		currentCentiSeconds = (int) (timeInCentiSeconds % 100);
		currentSeconds = (int) Math.floor(timeInCentiSeconds / 100);
	}
	  
	public void resetCurrentTime() {
		currentCentiSeconds = centiSeconds;
		currentSeconds = seconds;
	}
	  
	public void draw(SpriteBatch batch) {
		if (body == null) return;
		crate.setPosition(body.getPosition().x * BTWORLD - cellSize * 3/2, body.getPosition().y * BTWORLD - cellSize * 3/2);
	    crate.setRotation((float) (body.getAngle() * 180 / Math.PI));
	    batch.begin();
	    crate.draw(batch);
	    batch.end();
	    drawLabel();
	}
	  
	public void countDown(Array<Explosion> explosions) {
		if (countdownTime == 0) {
    		Explosion explosion = new Explosion(23, body.getPosition(), world, BTWORLD);
    		explosions.add(explosion);
    		world.destroyBody(body);
    		sound.play();
    	}
		
    	if (countdownTime > -1) {
	    	countdownTime -= 1;
	    	updateTime();
    	}
    	
    	if (countdownTime == -1) {
    		body = null;
    	}
	}
	
	public void drawLabel() {
		time.text =  time();
		
		double r = 26.3f * Math.sqrt(5) * (cellSize / 40);
		double alpha = body.getAngle() + Math.PI * 10 / 11;
		float dx = (float) (r * Math.cos(alpha));
		float dy = (float) (r * Math.sin(alpha));
		time.draw(body.getPosition().x * BTWORLD + dx, body.getPosition().y * BTWORLD + dy, (float) (body.getAngle() * 180 / Math.PI));
	}
	
	private String time() {
		String sec = Integer.toString(currentSeconds);
		if (currentSeconds < 10) sec = "0" + sec;
		String cen = Integer.toString(currentCentiSeconds);
		if (currentCentiSeconds < 10) cen = "0" + cen;
		return sec + ":" + cen;
	}
	
	public String givenTime() {
		String sec = Integer.toString(seconds);
		if (seconds < 10) sec = "0" + sec;
		String cen = Integer.toString(centiSeconds);
		if (centiSeconds < 10) cen = "0" + cen;
		return sec + ":" + cen;
	}
	
	public int[] drag() {
		if (dragAndDrop.isDragging()) {
			int x = (int) (Math.floor(dragAndDrop.getDragActor().getX()/cellSize - 1.5f) * cellSize);
			int y = (int) (Math.floor(dragAndDrop.getDragActor().getY()/cellSize - 1.5f) * cellSize);
			int bool = 0;

			droppable = true;
			world.QueryAABB(AABBCallback, x / BTWORLD + 0.2f, y / BTWORLD + 0.2f, x / BTWORLD + 0.45f, y / BTWORLD + 0.45f);
			if (droppable) {
				int toDropX = x;
				int toDropY = y;
				bool = 1;  
			}
			return new int[] {1, x, y, bool};
		}
		return new int[] {0};
	}
}

