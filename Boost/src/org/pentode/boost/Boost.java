package org.pentode.boost;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	   boolean play = false;
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float WORLD_TO_BOX = 0.02f;
	   static final float BOX_TO_WORLD = 50f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   BodyDef bodyDef;	   
	   Body body;
	   FixtureDef fixtureDef;
	   Fixture fixture; 

	   Array<Body> walls;
	   
	   @Override
	   public void create() {
		   camera = new OrthographicCamera();
		   camera.setToOrtho(false, 800, 480);
		   
		   walls = new Array<Body>();
		   float [][] coord = {{16, 7, 0.1f, 7}, {0.1f, 7, 0.1f, 7}, {0, 0.1f, camera.viewportWidth, 0.1f}};
		  
		   debugRenderer = new Box2DDebugRenderer();
		   bodyDef = new BodyDef();
      
		   CircleShape circle;
		   circle = new CircleShape();
		   circle.setRadius(0.2f);
		  
		   fixtureDef = new FixtureDef();
		   bodyDef.type = BodyType.DynamicBody;
		   bodyDef.position.set(1, 2);
		
		   body = world.createBody(bodyDef);
		
		   fixtureDef.shape = circle;
		   fixtureDef.density = 0.5f; 
		   fixtureDef.friction = 0.4f;
		   fixtureDef.restitution = 0.8f;
		
		   fixture = body.createFixture(fixtureDef);
		   
		   circle.dispose(); 
		   
		   Body wallBody; 
		   BodyDef wallBodyDef; 
		 
		   for (int i = 0; i < coord.length; i++){
			   wallBodyDef =new BodyDef();
			   wallBodyDef.position.set(new Vector2(coord[i][0], coord[i][1]));
			   PolygonShape wallBox;
			   wallBox = new PolygonShape();  
			   wallBody = world.createBody(wallBodyDef);  
			  
			   wallBox.setAsBox(coord[i][2], coord[i][3]);
			   wallBody.createFixture(wallBox, 0.0f); 
			   wallBox.dispose();
			  
			   walls.add(wallBody);
		   }
		
		   debugMatrix=new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }
	   
	   @Override
	   public void render() {
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      
	      // process user input
	      if(Gdx.input.isTouched()) {
	    	  if(play) {
	    		  resetLevel();
	    	  }
	    	  play = !play;
	    	  
	    	  body.applyForceToCenter(new Vector2(1, 1));
	      }
	      
	      debugRenderer.render(world, debugMatrix);
	      if (play) {
	    	  world.step(1/60f, 6, 2);
	      }
	   }
	   
	   private void resetLevel() {
		   body = world.createBody(bodyDef);
		   fixture = body.createFixture(fixtureDef);
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
