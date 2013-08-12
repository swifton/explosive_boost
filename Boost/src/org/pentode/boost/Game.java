package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Game {
	TimeWindow timeWindow;
	
	World world = new World(new Vector2(0, -10), true);
	
	int [][] coordB;
	int [][] coord;
	int [][] coordBr;
	int detX, detY, detDir; 
	Vector2 ballInitialPosition;
	
	Ball ball;
	Bomb [] bombs;
	Brick [] bricks;
	Wall [] walls;

	Levels levels = new Levels();
	
	Detector detector;
	
	Textures textures = new Textures();
	Sounds sounds = new Sounds();
	
	ContactListener contactListener;

	Array<Explosion> explosions = new Array<Explosion>();
	
	boolean play = false;
	TextButton playButton;
	int timeToWin = -1;

	
	public Game(Stage stage, SpriteBatch batch) {
		
		timeWindow = new TimeWindow(stage);

		loadLevel(levels.level13);
		createBodies(stage, batch);
		
		detector = new Detector(detX, detY, detDir, ball.body, textures.detT);
		createContactListener();
		createPlayButton(stage);
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
	   
	   private void createBodies(Stage stage, SpriteBatch batch) {
		   ball = new Ball(world, ballInitialPosition, textures.ballT);
		   
		// Walls		   		 
	       int angle;
	       Wall wall;
	       walls = new Wall [coord.length];

		   for (int i = 0; i < coord.length; i++) {
			   if (coord[i].length == 5) angle = coord[i][4];
			   else angle = 0;
			   wall = new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, world);
			   wall.createSprites(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, textures.metal);
			   walls[i] = wall;
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow, coordB[i][2], coordB[i][3], batch);
			   bombs[i] = bomb;
			   bomb.crate = new Sprite(textures.crateT, 28, 26, 443, 444);
			   bomb.crate.setSize(120, 120);
			   bomb.crate.setOrigin(60, 60);
			   bomb.sound = sounds.explosionSound;
		   }
		   
		   // Bricks
		   Brick brick;
		   
		   bricks = new Brick[coordBr.length];
		   
		   if (coordBr != null) {
			   for (int i = 0; i < coordBr.length; i++) {
				   brick = new Brick(coordBr[i][0], coordBr[i][1], coordBr[i][2], coordBr[i][3], world);
				   brick.sprite = new Sprite(textures.brickT, 0, 62, 64, 32);
				   brick.sprite.setSize(80, 40);
				   brick.sprite.setOrigin(40, 20);
				   bricks[i] = brick;
			   }
		   }
	   }
	   
	   private void createContactListener() {
		   contactListener = new ContactListener() {
               public void beginContact(Contact contact) {
            	   if (contact.getFixtureB().getBody() == ball.body) {
            		   sounds.ballSound.play();
            	   }
            	   
            	   if (contact.getFixtureB().getBody().getUserData() == "brick") {
            		   sounds.brickSound.play();
            	   }
               }
               
               public void endContact(Contact contact) {}
               public void postSolve(Contact contact, ContactImpulse impulse) {}
               public void preSolve(Contact contact, Manifold oldManifold) {}
		   };
	   }
	   
	   public void render(SpriteBatch batch) {
		   renderSprites(batch);
		   
		   for (Explosion e:explosions) {
			   e.draw(batch);
		   }
		   
		   cleanupExplosions();
		   
		   if (detector.detect(world)) {
			   timeToWin = 100;
			   Gdx.app.log("Xyu", Integer.toString(timeToWin));
			   detector.on = false;
			   sounds.detectorSound.play();
		   }
		   
		   if (play) {
			   for (int i = 0; i < bombs.length; i++) bombs[i].countDown(explosions);
			   world.step(1/60f, 6, 2);
			   timeToWin -= 1;
			   if (timeToWin == 0) pausePlay();
		   }
	   }
	   
	   private void renderSprites(SpriteBatch batch) {
		   for (int i = 0; i < bombs.length; i++) bombs[i].draw(batch);
		   for (int i = 0; i < bricks.length; i++) bricks[i].draw(batch);
		   for (int i = 0; i < walls.length; i++) walls[i].draw(batch);
		   ball.draw(batch);
	   }
	   
	   public void resetLevel() {
		   cleanupExplosions();
		   
		   world.dispose();
		   world = new World(new Vector2(0, -10), true);
		   ball.createBody(world);
		   detector.toDetect = ball.body;
		   Bomb bomb;
		   
		   for (int j = 0; j < bricks.length; j++) {
			   bricks[j].createBody(world);
		   }
		   
		   for (int k = 0; k < bombs.length; k++) {
			   bomb = bombs[k];
			   bomb.createBody(world);
			   if (bomb.countdownTime < 0) {		   
			   }
			   
			   bomb.resetCurrentTime();
			   bomb.updateLabel();
		   }
		   
		   for (int k = 0; k < walls.length; k++) {
			   walls[k].createBody(world);
		   }
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
	   
	   private void createPlayButton(Stage stage) {
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
	   
	   public void pausePlay() {
		   playButton.setText("Stop");
		   play = !play;
		   resetLevel();
		   if(!play) {
			   world.clearForces();
			   playButton.setText("Play");
		   }			   
		   else {
			   for (int k = 0; k < bombs.length; k++) {
				   bombs[k].countdownTime = (int) Math.floor(((float)bombs[k].seconds + (float)bombs[k].centiSeconds/100)*60);
				   timeToWin = -1;
				   detector.on = true;
			   }
		   }
		   
		   world.setContactListener(contactListener);
		   for (Bomb bomb:bombs) bomb.play = play; 
		}
}