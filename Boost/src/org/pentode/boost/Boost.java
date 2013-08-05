package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	   boolean postponedReset = false;
	   
	   World world = new World(new Vector2(0, -10), true); 
	   static final float BOX_TO_WORLD = 200f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;
	    
	   Ball ball;
	   Vector2 ballInitialPosition;
	   Bomb [] bombs;
	   Brick [] bricks;
	   Wall [] walls;
	   Array<Explosion> explosions = new Array<Explosion>();
	   int [][] coordB;
	   int [][] coord;
	   int [][] coordBr;
	   int detX, detY, detDir; 
	   
	   Levels levels = new Levels();
	   
	   Vector2 p1;
	   Vector2 p2;
	   Vector2 p3;
	   Body lastBody;	   
	   
	   ContactListener contactListener;
	   
	   Label fuckLabel;
	   
	   Texture crateT;
	   Texture brickT;
	   Texture metal;
	   Texture ballT;
	   Texture detT;
	   Sprite detS;

	   SpriteBatch batch;
	   int dir;
	   BitmapFont font;
	   RotatableText text;
	   float an = 0;
	   Detector detector;
	   int timeToWin = -1;

	   @Override
	   public void create() {
		   loadLevel(levels.level13);
		   
		   loadTextures();
		   batch = new SpriteBatch();
		   camera = new OrthographicCamera();

		   stage = new Stage();
		   Gdx.input.setInputProcessor(stage);
		   
		   renderer = new ShapeRenderer();
		   debugRenderer = new Box2DDebugRenderer();
		   
		   timeWindow = new TimeWindow(stage);
		   createBodies();
		   createPlayButton();
		   detector = new Detector(detX, detY, detDir, ball.body, detT);

		   debugMatrix = new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);

		   //effect.free();
	   }
	  
	   private void createPlayButton() {
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
	   }
	   
	   private void createBodies() {
		   ball = new Ball(world, ballInitialPosition, ballT);
		   
		// Walls		   		 
	       int angle;
	       Wall wall;
	       walls = new Wall [coord.length];

		   for (int i = 0; i < coord.length; i++) {
			   if (coord[i].length == 5) angle = coord[i][4];
			   else angle = 0;
			   wall = new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, world);
			   wall.createSprites(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, metal);
			   walls[i] = wall;
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow, coordB[i][2], coordB[i][3], batch);
			   bombs[i] = bomb;
			   bomb.crate = new Sprite(crateT, 28, 26, 443, 444);
			   bomb.crate.setSize(120, 120);
			   bomb.crate.setOrigin(60, 60);
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
	   }
	   
	   private void loadTextures() {
		   crateT = new Texture(Gdx.files.internal("crate.jpg"));
		   brickT = new Texture(Gdx.files.internal("brick043.gif"));
		   metal = new Texture(Gdx.files.internal("metal.jpg"));
		   ballT = new Texture(Gdx.files.internal("ball.png"));
		   detT = new Texture(Gdx.files.internal("detector.png"));
	   }
	   
	   private void pausePlay() {
		   playButton.setText("Stop");
		   play = !play;
		   resetLevel();
		   if(!play) {
			   world.clearForces();
			   playButton.setText("Play");
		   }			   
		   else {
			   for (int k = 0; k < coordB.length; k++) {
				   bombs[k].countdownTime = (int) Math.floor(((float)bombs[k].seconds + (float)bombs[k].centiSeconds/100)*60);
				   timeToWin = -1;
				   detector.on = true;
			   }
		   }
		   
		   
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
		   
		   for (Explosion e:explosions) {
			   e.draw(batch);
		   }
	      
		   renderSprites();
		   detector.draw(batch, renderer);
		   stage.draw();
		   debugRenderer.render(world, debugMatrix);
	  	  
		   cleanupExplosions();
		   
		   if (detector.detect(world)) {
			   timeToWin = 50;
			   Gdx.app.log("Xyu", Integer.toString(timeToWin));
			   detector.on = false;
		   }

		   if (play) {
			   for (int i = 0; i < coordB.length; i++) bombs[i].countDown(explosions);
			   world.step(1/60f, 6, 2);
			   timeToWin -= 1;
			   if (timeToWin == 0) pausePlay();
		   }
	    	  
		   Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	   
	   private void cleanupExplosions() {
		   for (Explosion e:explosions) {
		    	  e.cleanupDelay -= 1;
		    	  if (!play) e.cleanupDelay = 0;
		    	  if (e.cleanupDelay == 0) {
		    		  e.dispose(world);
		    		  explosions.removeValue(e, true);
		    	  }
		      }
	   }
	   
	   private void renderSprites() {
		   for (int i = 0; i < coordB.length; i++) bombs[i].draw(batch);
		   for (int i = 0; i < coordBr.length; i++) bricks[i].draw(batch);
		   for (int i = 0; i < coord.length; i++) walls[i].draw(batch);
		   ball.draw(batch);
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
		   
		   detX = level.detector.x;
		   detY = level.detector.y;
		   detDir = level.detector.direction;
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
		   //for (Explosion e:explosions) {
			//   e.cleanupDelay = 0;
		   //}
		   cleanupExplosions();
		   
		   world.dispose();
		   world = new World(new Vector2(0, -10), true);
		   //ball.reset(ballInitialPosition.x, ballInitialPosition.y);
		   ball.createBody(world);
		   lastBody = null;
		   detector.toDetect = ball.body;
		   Bomb bomb;
		   
		   for (int j = 0; j < coordBr.length; j++) {
			   bricks[j].createBody(world);
			//   bricks[j].reset();
		   }
		   
		   for (int k = 0; k < coordB.length; k++) {
			   bomb = bombs[k];
			   bomb.createBody(world);
			   if (bomb.countdownTime < 0) {
				//   bomb.createBody(bomb.startX, bomb.startY, world);		   
			   }
			   
			   //bomb.reset(bomb.startX, bomb.startY);
			   bomb.resetCurrentTime();
			   bomb.updateLabel();
		   }
		   
		   for (int k = 0; k < walls.length; k++) {
			   walls[k].createBody(world);
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

