package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
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
	   ShapeRenderer renderer;
	   TimeWindow timeWindow;
	   TextButton playButton;
	   boolean play = false;
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float BOX_TO_WORLD = 200f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   Ball ball;
	   Vector2 ballInitialPosition;
	   Bomb [] bombs;
	   Array<Explosion> explosions = new Array<Explosion>();
	   int [][] coordB;
	   int [][] coord;
	   Levels levels = new Levels();
	   
	   Vector2 p1;
	   Vector2 p2;
	   Vector2 p3;
	   Body lastBody;	   
	   RayCastCallback callBack;
	   
	   ContactListener contactListener;
	   QueryCallback AABBCallback;

	   @Override
	   public void create() {
		   callBack = new RayCastCallback() {
			   @Override
			   public float reportRayFixture(Fixture fix, Vector2 p, Vector2 normal, float fraction) {
				   fuckJava(p, fix.getBody());
				   return -1f;
			   }
		   };
		     
		   loadLevel(levels.level4);
		   
		   // gdx stuff
		   Gdx.app.log("MyTag", "my informative message");
		   stage = new Stage();
		   camera = new OrthographicCamera();
		   float w = Gdx.graphics.getWidth();
		   float h = Gdx.graphics.getHeight();
		   camera.setToOrtho(false, w, h);
		   Gdx.input.setInputProcessor(stage);
		   renderer = new ShapeRenderer();
		   debugRenderer = new Box2DDebugRenderer();

	       timeWindow = new TimeWindow(stage);
	       timeWindow.window.setVisible(false);

		   
		   // Walls		   		 
	       float angle;
	       
		   for (int i = 0; i < coord.length; i++) {
			   if (coord[i].length == 5) angle = coord[i][4];
			   else angle = 0;
			   
			   new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, world);
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow);
			   bomb.seconds = coordB[i][2];
			   bomb.centiSeconds = coordB[i][3];
			   bomb.givenCountdownTime = (int) Math.floor( ((float)bomb.seconds + (float)bomb.centiSeconds/100)*60);
			   bombs[i] = bomb;
			   
		   }
		   
		   // Play Button
		   
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   playButton = new TextButton("Play", skin);
		   playButton.setX(1785f);
		   playButton.setY(1070f);
		   playButton.setWidth(130f);
		   playButton.setHeight(130f);
	       stage.addActor(playButton);
	        
	       playButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
				//	System.out.println("Clicked! Is checked: " + button.isChecked());
	        		pausePlay();
	        	}
	        		
			});
	        
		   ball = new Ball(world, ballInitialPosition);
		   
		   contactListener = new ContactListener() {
			   public void beginContact(Contact contact) {
				   if (!play) {
					   
				   }
			   }
			   
			   public void endContact(Contact contact) {
				   
			   }
			   
			   public void postSolve(Contact contact, ContactImpulse impulse) {
				   
			   }
			   
			   public void preSolve(Contact contact, Manifold oldManifold) {
				   
			   }
		   };
		   
		   AABBCallback = new QueryCallback() {
			   public boolean reportFixture(Fixture fixture) {
				   
				   return true;
			   }
		   };
		   
		   world.setContactListener(contactListener);
		   
		   debugMatrix=new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }
	   
	   private void fuckJava(Vector2 p, Body body) {
		   if ((Math.abs(p.x - p1.x) < Math.abs(p2.x - p1.x)) || (Math.abs(p.y - p1.y) < Math.abs(p2.y - p1.y))) {
			   p2.x = p.x;
			   p2.y = p.y;
			   lastBody = body;
		   }
	   }
  
	   private void pausePlay() {
		   playButton.setText("Stop");
		   if(play) {
			   resetLevel();
			   playButton.setText("Play");
		   }			   
		   else {
			   for (int k = 0; k < coordB.length; k++) {
				   bombs[k].countdownTime = (int) Math.floor( ((float)bombs[k].seconds + (float)bombs[k].centiSeconds/100)*60);
			   }
		   }
		   play = !play;
		   
		   for (Bomb bomb:bombs) bomb.play = play; 
		}

	   @Override
	   public void render() {
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
	      
	      camera.apply(Gdx.gl10);
	      renderer.setProjectionMatrix(camera.combined);

	      //raycast
	      p2.x= p3.x;
	      p2.y= p3.y;
	      world.rayCast(callBack , p1, p2);
	      
	      renderer.begin(ShapeType.Line);
	      renderer.setColor(Color.RED);
	      renderer.line(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD, p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	      renderer.end();
	      
	       
	      
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
	      
	      if (!play) {
	    	  world.QueryAABB(AABBCallback, 0f, 0f, 8.6f, 6f);
	      }
	      
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
	    	  
	    	  if (lastBody == ball.body) {
	    		  lastBody = null;
	  			   pausePlay();
	  		   }
	      }
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	  
	   
	   private void loadLevel(Level level) {
		   coord = level.walls;
		   coordB = level.bombs;
		   ballInitialPosition = level.ball;
		   p1 = new Vector2((float) level.detector.x / 5 - 0.1f, (float) level.detector.y / 5 - 0.1f);
		   if (level.detector.direction == 0) p3 = new Vector2(p1.x, 50);
		   if (level.detector.direction == 1) p3 = new Vector2(50, p1.y);
		   if (level.detector.direction == 2) p3 = new Vector2(p1.x, -1);
		   if (level.detector.direction == 3) p3 = new Vector2(-1, p1.y);
		   p2 = new Vector2(p3.x, p3.y);
	   }
	   
	   private void resetLevel() {
		   ball.reset(ballInitialPosition.x, ballInitialPosition.y);
		   lastBody = null;
		   Bomb bomb;
		   
		   for (int k = 0; k < coordB.length; k++) {
			   bomb = bombs[k];
			   if (bomb.countdownTime <= 0) {
				   bomb.createBody(bomb.startX, bomb.startY, world);		   
			   }
			   
			   bomb.reset(bomb.startX, bomb.startY);
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

