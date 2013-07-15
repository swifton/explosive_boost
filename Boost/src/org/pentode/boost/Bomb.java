package org.pentode.boost;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

public class Bomb {
	Body body;
	int givenCountdownTime;
	int countdownTime;
	float startX, startY;
	Stage stagee;
	boolean touched;
	TimeWindow timeWindow;
	static final float BTWORLD = 200f;

	
	  public Bomb(float x, float y, World world, Stage stage, TimeWindow w) {
		  timeWindow = w;
		  stagee = stage;
		  startX = x;
		  startY = y;
		  createBody(x, y, world);
		  createDragDrop(x, y, stage);
	   }
	  
	  public void createBody(float x, float y, World world) {
		  BodyDef bombDef;	   
		   
		   FixtureDef bombFixtureDef;
		   
		   bombDef = new BodyDef();
		   bombDef.type = BodyType.DynamicBody;
		   bombDef.position.set(new Vector2(x, y));
		   
		   PolygonShape bombBox;
		   bombBox = new PolygonShape();
		   bombBox.setAsBox(0.3f, 0.3f);
		   
		   bombFixtureDef = new FixtureDef();
		   bombFixtureDef.shape = bombBox;
		   bombFixtureDef.density = 0.5f; 
		   bombFixtureDef.friction = 0.4f;
		   bombFixtureDef.restitution = 0.3f;
		   
		   body = world.createBody(bombDef);
		   body.createFixture(bombFixtureDef);
		   bombBox.dispose(); 
	  }
	  
	  private void createDragDrop(float x, float y, Stage stage) {	 
	        DragAndDrop dragAndDrop = new DragAndDrop();
	        
	        final Skin skinn = new Skin();
			skinn.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
			skinn.add("badlogic", new Texture("droplet.png"));

			final Image sourceImage = new Image(skinn, "badlogic");
			sourceImage.setBounds(x * BTWORLD - 50, y * BTWORLD - 50, 100, 100);
			stage.addActor(sourceImage);
			
			sourceImage.addListener(new ClickListener() {
			    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			    	touched = true;
			        return true;
			    }
			    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			    	if (touched) {
			    		timeWindow.label.setText((CharSequence) Integer.toString(givenCountdownTime));
			    		timeWindow.window.setVisible(true);
			    		passBomb();
			    		}
			    	touched = false;
			    }
			});
	        
			dragAndDrop.addSource(new Source(sourceImage) {
				public Payload dragStart (InputEvent event, float x, float y, int pointer) {
					touched = false;
					Payload payload = new Payload();
					payload.setObject("crap");

					payload.setDragActor(new Label("Some payload!", skinn));

					Label validLabel = new Label("Some payload!", skinn);
					validLabel.setColor(0, 1, 0, 1);
					payload.setValidDragActor(validLabel);

					Label invalidLabel = new Label("Some payload!", skinn);
					invalidLabel.setColor(1, 0, 0, 1);
					payload.setInvalidDragActor(invalidLabel);
			
					return payload;
				}
				public void dragStop(InputEvent event,
			            float x,
			            float y,
			            int pointer,
			            DragAndDrop.Target target) {
					float myx = x + sourceImage.getX();
					float myy = y + sourceImage.getY();
					startX = ((float)Math.round((myx * 5)/BTWORLD))/5 - 0.1f;
					startY = ((float)Math.round((myy * 5)/BTWORLD))/5 - 0.1f;
					myx = startX * BTWORLD;
					myy = startY * BTWORLD;
					body.setTransform(new Vector2(startX, startY), 0);
					sourceImage.setBounds(myx - 50, myy - 50, 100, 100);
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
}

