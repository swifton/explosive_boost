package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Boost implements ApplicationListener {
	   OrthographicCamera camera;
	   public Stage stage;
	   MyInputProcessor inputProcessor = new MyInputProcessor();

	   boolean play = false;
	   
	   Vector2 ballInitialPosition = new Vector2(1, 2);
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float BOX_TO_WORLD = 50f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   Ball ball;

	   Bomb [] bombs;
	   float [][] coordB = {{5, 5, 100}, {7, 5, 300}};
	   
	   Array<Explosion> explosions = new Array<Explosion>();
	   
	   @Override
	   public void create() {
		   
		   // gdx stuff
		   Gdx.app.log("MyTag", "my informative message");
		   stage = new Stage();
		   camera = new OrthographicCamera();
		   camera.setToOrtho(false, 800, 480);
		   Gdx.input.setInputProcessor(stage);
		   
		   //box2d stuff

		   debugRenderer = new Box2DDebugRenderer();
		   
		   // Ball. What's difficult?
		   ball = new Ball(world);
		   
		   // Walls
		   float [][] coord = {{16, 7, 0.1f, 7}, {0.1f, 7, 0.1f, 7}, {0, 0.1f, camera.viewportWidth, 0.1f}};
		   		 
		   for (int i = 0; i < coord.length; i++) {
			   new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], world);
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world);
			   bomb.givenCountdownTime = (int) coordB[i][2];
			   bomb.countdownTime = bomb.givenCountdownTime;
			   bombs[i] = bomb;
			   
		   }
		   
		   // Play Button
		   
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   final TextButton button = new TextButton("Play", skin);
	        button.setX(1700f);
	        button.setY(1000f);
	        button.setWidth(140f);
	        button.setHeight(140f);
	        stage.addActor(button);
	        
	        button.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
				//	System.out.println("Clicked! Is checked: " + button.isChecked());
	        		button.setText("Stop");
	        		 if(play) {
	   		    	  resetLevel();
	   		    	  button.setText("Play");
	   			   }			   
	   		    	  play = !play;
				}
			});
	        
	        // Trying to drag the button
	        
	        DragAndDrop dragAndDrop = new DragAndDrop();
	        
	        final Skin skinn = new Skin();
			skinn.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
			skinn.add("badlogic", new Texture("droplet.png"));

			Image sourceImage = new Image(skinn, "badlogic");
			sourceImage.setBounds(50, 125, 100, 100);
			stage.addActor(sourceImage);
			
			Image validTargetImage = new Image(skinn, "badlogic");
			validTargetImage.setBounds(200, 50, 100, 100);
			stage.addActor(validTargetImage);

			Image invalidTargetImage = new Image(skinn, "badlogic");
			invalidTargetImage.setBounds(200, 200, 100, 100);
			stage.addActor(invalidTargetImage);
	        
			dragAndDrop.addSource(new Source(sourceImage) {
				public Payload dragStart (InputEvent event, float x, float y, int pointer) {
					Payload payload = new Payload();
					payload.setObject("Some payload!");

					payload.setDragActor(new Label("Some payload!", skinn));

					Label validLabel = new Label("Some payload!", skinn);
					validLabel.setColor(0, 1, 0, 1);
					payload.setValidDragActor(validLabel);

					Label invalidLabel = new Label("Some payload!", skinn);
					invalidLabel.setColor(1, 0, 0, 1);
					payload.setInvalidDragActor(invalidLabel);

					return payload;
				}
			});
			
			dragAndDrop.addTarget(new Target(validTargetImage) {
				public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
					getActor().setColor(Color.GREEN);
					return true;
				}

				public void reset (Source source, Payload payload) {
					getActor().setColor(Color.WHITE);
				}

				public void drop (Source source, Payload payload, float x, float y, int pointer) {
					System.out.println("Accepted: " + payload.getObject() + " " + x + ", " + y);
				}
			});
			dragAndDrop.addTarget(new Target(invalidTargetImage) {
				public boolean drag (Source source, Payload payload, float x, float y, int pointer) {
					getActor().setColor(Color.RED);
					return false;
				}

				public void reset (Source source, Payload payload) {
					getActor().setColor(Color.WHITE);
				}

				public void drop (Source source, Payload payload, float x, float y, int pointer) {
				}
			});
	        
	        // stuff
		
		   debugMatrix=new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }
	   
	   
	   
	   @Override
	   public void render() {
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      
	      // process user input
	      if(Gdx.input.isTouched()) {
	    	  ball.body.applyForceToCenter(new Vector2(1, 1));
	      }
	      
	      // cleaning up explosions	     
	      for (Explosion e:explosions) {
	    	  e.cleanupDelay -= 1;
	    	  if (e.cleanupDelay == 0) {
	    		  e.dispose(world);
	    		  explosions.removeValue(e, true);
	    	  }
	      }
	      
	      debugRenderer.render(world, debugMatrix);
	      stage.draw();
	      
	      if (play) {
	    	// Bomb timers count down
		      
		      for (int i = 0; i < coordB.length; i++) {
		    	  bombs[i].countdownTime -= 1;
		    	  if (bombs[i].countdownTime < -1000) bombs[i].countdownTime = -1;
		    	  if (bombs[i].countdownTime == 0) {
		    		  Explosion explosion = new Explosion(20, bombs[i].body.getPosition(), world);
		    		  explosions.add(explosion);
		    		  world.destroyBody(bombs[i].body);
		    	  }
		      }
	    	  world.step(1/60f, 6, 2);
	      }
	   }
	   
	   private void resetLevel() {
		   ball.reset(ballInitialPosition.x, ballInitialPosition.y);
		   
		   for (int k = 0; k < coordB.length; k++) {
			   if (bombs[k].countdownTime <= 0) {
				   bombs[k].createBody(coordB[k][0], coordB[k][1], world);		   
			   }
			   
			   bombs[k].reset(coordB[k][0], coordB[k][1]);
			   bombs[k].countdownTime = bombs[k].givenCountdownTime;
		   }
	   }
	   
	   public class MyInputProcessor implements InputProcessor {
		   @Override
		   public boolean keyDown (int keycode) {
		      return false;
		   }

		   @Override
		   public boolean keyUp (int keycode) {
		      return false;
		   }

		   @Override
		   public boolean keyTyped (char character) {
		      return false;
		   }

		   @Override
		   public boolean touchDown (int x, int y, int pointer, int button) {
		      return false;
		   }

		   @Override
		   public boolean touchUp (int x, int y, int pointer, int button) {
			   if(play) {
		    	  resetLevel();
			   }			   
		    	  play = !play;
		      return false;
		   }

		   @Override
		   public boolean touchDragged (int x, int y, int pointer) {
			//   bombs[0].reset(x/(2*BOX_TO_WORLD), (960 - y)/(2*BOX_TO_WORLD));
		      return false;
		   }

		   @Override
		   public boolean mouseMoved (int x, int y) {
		      return false;
		   }

		   @Override
		   public boolean scrolled (int amount) {
		      return false;
		   }
		}
	   
	   @Override
	   public void dispose() {

	   }

	   @Override
	   public void resize(int width, int height) {
	   }

	   @Override
	   public void pause() {
	   }

	   @Override
	   public void resume() {
	   }
}
