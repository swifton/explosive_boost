package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Boost implements ApplicationListener {
	   OrthographicCamera camera;
	   public Stage stage;

	   boolean play = false;
	   
	   Vector2 ballInitialPosition = new Vector2(2, 3.3f);
	   
	   TimeWindow timeWindow;
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float BOX_TO_WORLD = 200f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   Ball ball;

	   Bomb [] bombs;
	   float [][] coordB = {{5f, 5f, 100}, {5f, 5.4f, 300}};
	   
	   Array<Explosion> explosions = new Array<Explosion>();
	   
	   @Override
	   public void create() {
		   
		   // gdx stuff
		   Gdx.app.log("MyTag", "my informative message");
		   stage = new Stage();
		   camera = new OrthographicCamera();
		    float w = Gdx.graphics.getWidth();
		    float h = Gdx.graphics.getHeight();
		   camera.setToOrtho(false, w, h);
		   Gdx.input.setInputProcessor(stage);
		   
	        timeWindow = new TimeWindow(stage);
			timeWindow.window.setVisible(false);
		   
		   //box2d stuff

		   debugRenderer = new Box2DDebugRenderer();
		   
		   // It's the ball. What's difficult?
		   ball = new Ball(world, ballInitialPosition);
		   
		   // Walls
		   float [][] coord = {{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}, {1, 15, 15, 15}};
		   		 
		   for (int i = 0; i < coord.length; i++) {
			   new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], world);
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow);
			   bomb.givenCountdownTime = (int) coordB[i][2];
			   bomb.countdownTime = bomb.givenCountdownTime;
			   bombs[i] = bomb;
			   
		   }
		   
		   // Play Button
		   
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   final TextButton button = new TextButton("Play", skin);
	        button.setX(1785f);
	        button.setY(1070f);
	        button.setWidth(130f);
	        button.setHeight(130f);
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
	        		 else {
	        			   for (int k = 0; k < coordB.length; k++) {
 	        				   bombs[k].countdownTime = bombs[k].givenCountdownTime;
	        			   }
	        		 }
	   		    	  play = !play;
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
		    	  if (bombs[i].countdownTime == 0) {
		    		  Explosion explosion = new Explosion(23, bombs[i].body.getPosition(), world);
		    		  explosions.add(explosion);
		    		  world.destroyBody(bombs[i].body);
		    	  }
		    	  if (bombs[i].countdownTime > -1) {
			    	  bombs[i].countdownTime -= 1;
		    	  }
		      }
	    	  world.step(1/60f, 6, 2);
	      }
	   }
	   
	   private void resetLevel() {
		   ball.reset(ballInitialPosition.x, ballInitialPosition.y);
		   
		   for (int k = 0; k < coordB.length; k++) {
			   if (bombs[k].countdownTime <= 0) {
				   bombs[k].createBody(bombs[k].startX, bombs[k].startY, world);		   
			   }
			   
			   bombs[k].reset(bombs[k].startX, bombs[k].startY);
			   bombs[k].countdownTime = bombs[k].givenCountdownTime;
		   }
	   }
	   
	   @Override
	   public void dispose() {

	   }

	   @Override
	   public void resize(int width, int height) {
		    float w = Gdx.graphics.getWidth();
		    float h = Gdx.graphics.getHeight();
		   camera.setToOrtho(false, w, h);
	   }

	   @Override
	   public void pause() {
	   }

	   @Override
	   public void resume() {
	   }
}

