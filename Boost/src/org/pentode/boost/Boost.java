package org.pentode.boost;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Boost implements ApplicationListener {
	   OrthographicCamera camera;
	   MyInputProcessor inputProcessor = new MyInputProcessor();

	   boolean play = false;
	   
	   Vector2 ballInitialPosition = new Vector2(1, 2);
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float WORLD_TO_BOX = 0.02f;
	   static final float BOX_TO_WORLD = 50f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   BodyDef ballDef;	   
	   Body ball;
	   FixtureDef fixtureDef;
	   Fixture fixture; 

	   Array<Body> walls;
	   Body [] bombs;
	   float [][] coordB = {{5, 5}, {7, 5}};
	   
	   @Override
	   public void create() {
		   camera = new OrthographicCamera();
		   camera.setToOrtho(false, 800, 480);
		   Gdx.input.setInputProcessor(inputProcessor);
		   
		   walls = new Array<Body>();
		   bombs = new Body[coordB.length];
		   
		   float [][] coord = {{16, 7, 0.1f, 7}, {0.1f, 7, 0.1f, 7}, {0, 0.1f, camera.viewportWidth, 0.1f}};
		  
		   debugRenderer = new Box2DDebugRenderer();
		   
		   createBall();
		   
		   Body wallBody; 
		   BodyDef wallBodyDef; 
		 
		   for (int i = 0; i < coord.length; i++) {
			   wallBodyDef =new BodyDef();
			   wallBodyDef.position.set(new Vector2(coord[i][0], coord[i][1]));
			   PolygonShape wallBox;
			   wallBox = new PolygonShape();
			   wallBox.setAsBox(coord[i][2], coord[i][3]);
			   wallBody = world.createBody(wallBodyDef);  
			  
			   wallBody.createFixture(wallBox, 0.0f); 
			   wallBox.dispose();
			  
			   walls.add(wallBody);
		   }
		   
		   BodyDef bombDef;	   
		   Body bomb;
		   FixtureDef bombFixtureDef;
		   Fixture bombFixture;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bombDef = new BodyDef();
			   bombDef.type = BodyType.DynamicBody;
			   bombDef.position.set(new Vector2(coordB[i][0], coordB[i][1]));
			   
			   PolygonShape bombBox;
			   bombBox = new PolygonShape();
			   bombBox.setAsBox(0.5f, 0.5f);
			   
			   bombFixtureDef = new FixtureDef();
			   bombFixtureDef.shape = bombBox;
			   bombFixtureDef.density = 0.5f; 
			   bombFixtureDef.friction = 0.4f;
			   bombFixtureDef.restitution = 0.3f;
			   
			   bomb = world.createBody(bombDef);
			   bombFixture = bomb.createFixture(bombFixtureDef);
			   bombBox.dispose(); 
			   
			   bombs[i] = bomb;
		   }
		   
		
		   debugMatrix=new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }
	   
	   private void createBall() {
		   ballDef = new BodyDef();
		   ballDef.type = BodyType.DynamicBody;
		   ballDef.position.set(1, 2);
		      
		   CircleShape circle;
		   circle = new CircleShape();
		   circle.setRadius(0.2f);
		  
		   fixtureDef = new FixtureDef();
		
		   ball = world.createBody(ballDef);
		
		   fixtureDef.shape = circle;
		   fixtureDef.density = 0.5f; 
		   fixtureDef.friction = 0.4f;
		   fixtureDef.restitution = 0.8f;
		
		   fixture = ball.createFixture(fixtureDef);
		   circle.dispose(); 
	   }
	   
	   private void explode(float blastPower, Vector2 center) {
		   int numRays = 20;
		   float DEGTORAD = (float)Math.PI/180;
		   for (int i = 0; i < numRays; i++) {
			      float angle = (i / (float)numRays) * 360 * DEGTORAD;
			      Vector2 rayDir = new Vector2((float)Math.sin(angle),(float)Math.cos(angle));
			  
			      BodyDef bd = new BodyDef();
			      bd.type = BodyType.DynamicBody;;
			      bd.fixedRotation = true; // rotation not necessary
			      bd.bullet = true; // prevent tunneling at high speed
			      bd.linearDamping = 10; // drag due to moving through air
			      bd.gravityScale = 0; // ignore gravity
			      bd.position.set(center); // start at blast center
			      bd.linearVelocity.set(new Vector2(blastPower * rayDir.x, blastPower * rayDir.y));
			      Body body = world.createBody(bd);
			  
			      CircleShape circle = new CircleShape();
			      circle.setRadius(0.05f); // very small
			  
			      FixtureDef fd = new FixtureDef();
			      fd.shape = circle;
			      fd.density = 60 / (float)numRays; // very high - shared across all particles
			      fd.friction = 0; // friction not necessary
			      fd.restitution = 0.99f; // high restitution to reflect off obstacles
			      fd.filter.groupIndex = -1; // particles should not collide with each other
			      Fixture fixture;
			      fixture = body.createFixture(fd);
			  }
	   }
	   
	   @Override
	   public void render() {
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      
	      // process user input
	      if(Gdx.input.isTouched()) {
	    	  
	    	  explode(20, new Vector2(6, 5));
	    	  ball.applyForceToCenter(new Vector2(1, 1));
	      }
	      
	      debugRenderer.render(world, debugMatrix);
	      if (play) {
	    	  world.step(1/60f, 6, 2);
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
	   
	   private void resetLevel() {
		   ball.setTransform(ballInitialPosition, 0);
		   ball.setLinearVelocity(new Vector2(0,0));
		   ball.setAngularVelocity(0);
		   ball.setAwake(true);
		   Body bomb;
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = bombs[i];
			   bomb.setTransform(coordB[i][0], coordB[i][1], 0);
			   bomb.setLinearVelocity(new Vector2(0,0));
			   bomb.setAngularVelocity(0);
			   bomb.setAwake(true);
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
