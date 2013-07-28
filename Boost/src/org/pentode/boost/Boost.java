package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Boost implements ApplicationListener {
	   OrthographicCamera camera;
	   public Stage stage;
	   ShapeRenderer renderer;
	   TimeWindow timeWindow;
	   TextButton playButton;
	   boolean play = false;
	   boolean postponedReset = false;
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float BOX_TO_WORLD = 200f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   Ball ball;
	   Vector2 ballInitialPosition;
	   Bomb [] bombs;
	   Brick [] bricks;
	   Array<Explosion> explosions = new Array<Explosion>();
	   int [][] coordB;
	   int [][] coord;
	   int [][] coordBr;
	   Levels levels = new Levels();
	   
	   Vector2 p1;
	   Vector2 p2;
	   Vector2 p3;
	   Body lastBody;	   
	   RayCastCallback callBack;
	   
	   ContactListener contactListener;
	   
	   Label fuckLabel;
	   
	   Texture crateT;
	   Texture brickT;
	   Texture metal;
	   Texture ballT;
	   Sprite ballS;
	   Texture detT;
	   Texture clock;
	   Sprite detS;
	   Array<Sprite> wallSprites;
	   SpriteBatch batch;
	   float[] angles;
	   int dir;
	   BitmapFont font;
	   RotatableText text;
	   float an = 0;



	   @Override
	   public void create() {
		   //Images
		   crateT = new Texture(Gdx.files.internal("crate.jpg"));
		   brickT = new Texture(Gdx.files.internal("brick043.gif"));
		   metal = new Texture(Gdx.files.internal("metal.jpg"));
		   ballT = new Texture(Gdx.files.internal("ball.png"));
		   detT = new Texture(Gdx.files.internal("detector.png"));
		   clock = new Texture(Gdx.files.internal("clock.jpg"));
		   angles = new float[] {0, (float) Math.PI/6, (float) (Math.PI/4), (float) (Math.PI/3), (float) (2 * Math.PI/3), (float) (3*Math.PI/4), (float) (5*Math.PI/6)};

		   batch = new SpriteBatch();
		   
		  
		   
		   // Detector beam
		   callBack = new RayCastCallback() {
			   @Override
			   public float reportRayFixture(Fixture fix, Vector2 p, Vector2 normal, float fraction) {
				   fuckJava(p, fix.getBody());
				   return -1f;
			   }
		   };
		     
		   loadLevel(levels.level15);
		   
	       detS = new Sprite(detT, 0, 0, 64, 64);
	       detS.setSize(40, 40);
	       detS.setPosition(p1.x * 40 - 4, p1.y * 40 - 4);
	       detS.setRotation(90 - dir * 90);
		   
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
	       
	       
	       // Screwing around with a label
	    
	       
	       FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TickingTimebombBB.ttf"));
	       font = generator.generateFont(273);
	       font.setColor(Color.RED);
	       generator.dispose();
		   LabelStyle style = new LabelStyle();
		   style.font = font;
		   style.fontColor = Color.RED;
		   fuckLabel = new Label("12345:67890", style);
		   fuckLabel.setPosition(400, 400);
		   fuckLabel.setOrigin(0, 0);
		   fuckLabel.setWidth(1);
		   fuckLabel.setRotation(50);
		   fuckLabel.scale(2);
		   
		   text = new RotatableText("hui", font, batch);
		   //stage.addActor(fuckLabel);

		   
		   // Walls		   		 
	       int angle;
	       
	       wallSprites = new Array<Sprite>();
	       Sprite sprite;
	       
		   int k;

		   for (int i = 0; i < coord.length; i++) {
			   if (coord[i].length == 5) angle = coord[i][4];
			   else angle = 0;
			   
			   new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, world);
			   if (coord[i][0] == coord[i][2]) {
				   for (k = 0; k < (coord[i][3] - coord[i][1] + 1) * 40/123; k++) {
					   sprite = new Sprite(metal, 0, 0, 40, 123);
					   sprite.setPosition(coord[i][0] * 40 - 40, coord[i][1] * 40 - 40 + 123 * k);
					   wallSprites.add(sprite);
				   }
				   sprite = new Sprite(metal, 0, 0, 40, (coord[i][3] - coord[i][1] + 1) * 40 - k * 123);
				   sprite.setPosition(coord[i][0] * 40 - 40, coord[i][1] * 40 - 40 + 123 * k);
				   wallSprites.add(sprite);
			   }
			   
			   if (coord[i][1] == coord[i][3]) {
				   for (k = 0; k < (coord[i][2] - coord[i][0] + 1) * 40/103; k++) {
					   sprite = new Sprite(metal, 0, 0, 103, 40);
					   sprite.setPosition(coord[i][0] * 40 - 40 + 103 * k, coord[i][1] * 40 - 40);
					   sprite.setOrigin((coord[i][2] + coord[i][0] - 1) * 20 - (coord[i][0] - 1) * 40 - k * 103, 20);
					   sprite.setRotation((float) (angles[angle] * 180 / Math.PI));
					   wallSprites.add(sprite);
				   }
				   sprite = new Sprite(metal, 0, 0, (coord[i][2] - coord[i][0] + 1) * 40 - k * 103, 40);
				   sprite.setPosition(coord[i][0] * 40 - 40 + 103 * k, coord[i][1] * 40 - 40);
				   sprite.setOrigin((coord[i][2] + coord[i][0] - 1) * 20 - (coord[i][0] - 1) * 40 - k * 103, 20);
				   sprite.setRotation((float) (angles[angle] * 180 / Math.PI));
				   wallSprites.add(sprite);
			   }
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb = new Bomb(-5, -5, world, stage, timeWindow);
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow);
			   bomb.seconds = coordB[i][2];
			   bomb.centiSeconds = coordB[i][3];
			   bomb.resetCurrentTime();
			   bomb.updateLabel();
			   bombs[i] = bomb;
			   bomb.crate = new Sprite(crateT, 0, 0, 120, 120);
		   }
		   
		   // Bricks
		   Brick brick;
		   
		   bricks = new Brick[coordBr.length];
		   
		   if (coordBr != null) {
			   for (int i = 0; i < coordBr.length; i++) {
				   brick = new Brick(coordBr[i][0], coordBr[i][1], coordBr[i][2], coordBr[i][3], world);
				   brick.sprite = new Sprite(brickT, 0, 62, 64, 32);
				   brick.sprite.setSize(80, 40);
				   brick.sprite.setOrigin(40, 20);
				   bricks[i] = brick;
			   }
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
	        		pausePlay();
	        	}
	        		
			});
	        
		   ball = new Ball(world, ballInitialPosition);
		   ballS = new Sprite(ballT, 0, 0, 80, 80);
		   
		   contactListener = new ContactListener() {
			   public void beginContact(Contact contact) {

			   }
			   
			   public void endContact(Contact contact) {
				   
			   }
			   
			   public void postSolve(Contact contact, ContactImpulse impulse) {
				   
			   }
			   
			   public void preSolve(Contact contact, Manifold oldManifold) {
				   
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
			   postponedReset = true;
			   //resetLevel();
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
	      
	      camera.update();
	      batch.setProjectionMatrix(camera.combined);
	      
	      camera.apply(Gdx.gl10);
	      renderer.setProjectionMatrix(camera.combined);
	      
	      // Animation stuff

	      Bomb bomb;
	      
	      for (int i = 0; i < coordB.length; i++) {
	    	  bomb = bombs[i];
	    	  bomb.crate.setPosition(bomb.body.getPosition().x * BOX_TO_WORLD - 60, bomb.body.getPosition().y * BOX_TO_WORLD - 60);
		      bomb.crate.setRotation((float) (bomb.body.getAngle() * 180 / Math.PI));
		      batch.begin();
		      bomb.crate.draw(batch);
		      batch.end();
	      }
	      
	      Brick brick;
	      
	      for (int i = 0; i < coordBr.length; i++) {
	    	  brick = bricks[i];
	    	  brick.sprite.setPosition(brick.body.getPosition().x * BOX_TO_WORLD - 40, brick.body.getPosition().y * BOX_TO_WORLD - 20);
	    	  brick.sprite.setRotation((float) (brick.body.getAngle() * 180 / Math.PI + brick.vertical * 90));
		      batch.begin();
		      brick.sprite.draw(batch);
		      batch.end();
	      }
	      
	      for (Sprite sprite:wallSprites) {
	    	  batch.begin();
	    	  sprite.draw(batch);
	    	  batch.end();
	      }
	      
	      ballS.setPosition(ball.body.getPosition().x * BOX_TO_WORLD - 40, ball.body.getPosition().y * BOX_TO_WORLD - 40);
	      ballS.setRotation((float) (ball.body.getAngle() * 180 / Math.PI));
	      batch.begin();
    	  ballS.draw(batch);
    	  batch.end();

	      //raycast
	      p2.x= p3.x;
	      p2.y= p3.y;
	      world.rayCast(callBack , p1, p2);
	      
	      renderer.begin(ShapeType.Line);
	      renderer.setColor(Color.RED);
	      renderer.line(p1.x * BOX_TO_WORLD, p1.y * BOX_TO_WORLD, p2.x * BOX_TO_WORLD, p2.y * BOX_TO_WORLD);
	      renderer.end();
	      
	      batch.begin();
    	  detS.draw(batch);
    	  batch.end();
    	  
    	  an += 1;
    	  text.draw(200, 200, an);
    	  
	      // cleaning up explosions	     
	      for (Explosion e:explosions) {
	    	  e.cleanupDelay -= 1;
	    	  if (e.cleanupDelay == 0) {
	    		  e.dispose(world);
	    		  explosions.removeValue(e, true);
	    	  }
	      }
	      
	      //debugRenderer.render(world, debugMatrix);
		   fuckLabel.rotate(50f);

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
			    	  bombs[i].updateTime();
			    	  bombs[i].updateLabel();
		    	  }
		      }
	    	  world.step(1/60f, 6, 2);
	    	  
	    	  if (lastBody == ball.body) {
	    		  lastBody = null;
	  			   pausePlay();
	  		   }
	      }
	      
	      if (postponedReset) {
	    	  resetLevel();
	    	  postponedReset = false;
	      }
	    	  
	      Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	  
	   
	   private void loadLevel(Level level) {
		   coord = level.walls;
		   coordB = level.bombs;
		   ballInitialPosition = level.ball;
		   
		   int [][] wallBricks;
		   int k;
		   if (level.bricks != null) {
			   coordBr = level.bricks;
		   }
		   else {
			   coordBr = new int[0][];
		   }
		   if (level.brickWall != null) {
			   for (int i = 0; i < level.brickWall.length; i++) {
				   wallBricks = new int[level.brickWall[i][6]][4];
				   int x1 = level.brickWall[i][0];
				   int y1 = level.brickWall[i][1];
				   int x2 = level.brickWall[i][2];
				   int y2 = level.brickWall[i][3];
				   int stepX = level.brickWall[i][4];
				   int stepY = level.brickWall[i][5];
				   for (k = 0; k < level.brickWall[i][6]; k++) {
					   wallBricks[k] = new int[] {x1 + stepX * k, y1 + stepY * k, x2 + stepX * k, y2 + stepY * k};
				   }
				   coordBr = concat(coordBr, wallBricks);
			   }
		   }
		   
		   p1 = new Vector2((float) level.detector.x / 5 - 0.1f, (float) level.detector.y / 5 - 0.1f);
		   dir = level.detector.direction; 
		   if (dir == 0) p3 = new Vector2(p1.x, 50);
		   if (dir == 1) p3 = new Vector2(50, p1.y);
		   if (dir == 2) p3 = new Vector2(p1.x, -1);
		   if (dir == 3) p3 = new Vector2(-1, p1.y);
		   p2 = new Vector2(p3.x, p3.y);
	   }
	   
	   int[][] concat(int[][] A, int[][] B) {
		   int aLen = A.length;
		   int bLen = B.length;
		   int[][] C= new int [aLen+bLen][];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
		}
	   
	   private void resetLevel() {
		   ball.reset(ballInitialPosition.x, ballInitialPosition.y);
		   lastBody = null;
		   Bomb bomb;
		   
		   for (int j = 0; j < coordBr.length; j++) {
			   bricks[j].reset();
		   }
		   
		   for (int k = 0; k < coordB.length; k++) {
			   bomb = bombs[k];
			   if (bomb.countdownTime < 0) {
				   bomb.createBody(bomb.startX, bomb.startY, world);		   
			   }
			   
			   bomb.reset(bomb.startX, bomb.startY);
			   bomb.resetCurrentTime();
			   bomb.updateLabel();
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

