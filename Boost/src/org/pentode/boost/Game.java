package org.pentode.boost;

import org.pentode.boost.objects.Ball;
import org.pentode.boost.objects.Bomb;
import org.pentode.boost.objects.Brick;
import org.pentode.boost.objects.Explosion;
import org.pentode.boost.objects.Wall;
import org.pentode.boost.sprites.BrickSprite;
import org.pentode.boost.ui.Buttons;
import org.pentode.boost.ui.Windows;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Game {
	float BTW;
	float cellSize;
	float buttonSize;
	
	World world = new World(new Vector2(0, -10), true);
	
	int [][] coordB;
	int [][] coord;
	int [][] coordBr;
	int detX, detY, detDir; 
	Vector2 ballInitialPosition;
	
	Ball ball;
	Bomb [] bombs;
	Brick [] bricks;
	Array<BrickSprite> brickSprites = new Array<BrickSprite>();
	Wall [] walls;
	Detector detector;
	Array<Explosion> explosions = new Array<Explosion>();

	Levels levels = new Levels();
	int levelNum = 1;

	Textures textures = new Textures();
	Sounds sounds = new Sounds();
	Fonts fonts;
	
	SoundListener soundListener;
	ShapeRenderer renderer;
	Stage stage; 
	SpriteBatch batch;
	
	Windows windows;
	Buttons buttons;

	int timeToWin = -1;
	
	boolean waiting = false;
	boolean complete = false;
	boolean play = false;
	boolean paused = false;
	boolean visible = false;

	Sprite drag;
	int toDropX;
	int toDropY;
	QueryCallback AABBCallback;
	boolean droppable;
	
	public Game(Stage s, SpriteBatch b) {
		stage = s;
		batch = b;
		renderer = new ShapeRenderer();

		soundListener = new SoundListener(sounds.ballSound);
		
		float h = Gdx.graphics.getHeight();
		BTW = h/6 - 1; 
		cellSize = BTW / 5;
		
		windows = new Windows(stage);
		fonts = new Fonts(cellSize);
		buttons = new Buttons(stage, cellSize);
		setButtonListeners();
		
		drag = new Sprite(textures.crateTarget, 28, 26, 443, 444);
		drag.setSize(cellSize * 3, cellSize * 3);
	}
	
	public void loadLevel() {
		loadLevelCoordinates(levels.list[levelNum - 1]);
		createSprites();
		createBodies();
		detector = new Detector(detX, detY, detDir, ball.body, textures.detT, BTW);
		resetLevel();
	}
	
	private void loadLevelCoordinates(Level level) {
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
	   
	   private void createSprites() {
		   if (brickSprites.size < coordBr.length) {
			   BrickSprite[] addition = new BrickSprite[coordBr.length - brickSprites.size];
			   for (int i = 0; i < coordBr.length - brickSprites.size; i++) {
				   addition[i] = new BrickSprite(textures.brickT, cellSize);
			   }
			   brickSprites.addAll(addition);
		   }
	   }
	   
	   private void createBodies() {
		   ball = new Ball(world, ballInitialPosition, textures.ballT, BTW);
		   
		// Walls		   		 
	       int angle;
	       Wall wall;
	       walls = new Wall [coord.length];

		   for (int i = 0; i < coord.length; i++) {
			   if (coord[i].length == 5) angle = coord[i][4];
			   else angle = 0;
			   wall = new Wall(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, world, BTW);
			   wall.createSprites(coord[i][0], coord[i][1], coord[i][2], coord[i][3], angle, textures.metal);
			   walls[i] = wall;
		   }
		   
		   // Bombs
		   bombs = new Bomb[coordB.length];
		   Bomb bomb;
		   
		   for (int i = 0; i < coordB.length; i++) {
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, windows.timeWindow, coordB[i][2], coordB[i][3], batch, BTW, fonts.bombDigits);
			   bombs[i] = bomb;
			   bomb.createSprite(textures.crateT);
			   bomb.sound = sounds.explosionSound;
		   }
		   
		   // Bricks
		   Brick brick;
		   
		   bricks = new Brick[coordBr.length];
		   
		   if (coordBr != null) {
			   for (int i = 0; i < coordBr.length; i++) {
				   brick = new Brick(coordBr[i][0], coordBr[i][1], coordBr[i][2], coordBr[i][3], world, BTW);
				   brick.sprite = brickSprites.get(i).sprite;
				   bricks[i] = brick;
			   }
		   }
	   }
	   
	   public void setVisible(boolean v) {
		   visible = v;
		   buttons.setVisible(v, false);
		   if (!v) windows.setVisible(false);
	   }
	   
	   public void render() {
		   if (!visible) return;
		   
		   renderSprites();
		   
		   for (Explosion e:explosions) e.draw(batch);
		   cleanupExplosions();
		   
		   detector.draw(batch, renderer);
		   
		   if (!paused && detector.detect(world)) {
			   timeToWin = 100;
			   detector.on = false;
			   sounds.detectorSound.play();
		   }
		   
		   if (!play) dragBomb();
		   
		   if (play && !paused) {
			   for (int i = 0; i < bombs.length; i++) bombs[i].countDown(explosions);
			   world.step(1/60f, 6, 2);
			   timeToWin -= 1;
			   if (timeToWin == 0) complete = true;
		   }
	   }
	   
	   private void dragBomb() {
		   for (Bomb bomb:bombs) {
			   int [] d = bomb.drag();
			   if (d[0] == 0) continue;
			   drag.setPosition(d[1], d[2]);
			   
			   if (d[3] == 1) drag.setColor(Color.WHITE);
			   else drag.setColor(Color.RED);
			   
			   batch.begin();
			   drag.draw(batch);
			   batch.end();
		   }
	   }
	   
	   private void renderSprites() {
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
		   
		   for (int j = 0; j < bricks.length; j++) bricks[j].createBody(world);
		   
		   for (int k = 0; k < bombs.length; k++) {
			   bombs[k].createBody(world);
			   bombs[k].resetCurrentTime();
		   }
		   
		   for (int k = 0; k < walls.length; k++) walls[k].createBody(world);
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
	   
	   private void setButtonListeners() {
	       buttons.playButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		pausePlay();
	        	}
	        		
			});

		   buttons.selectButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		setVisible(false);
	        		waiting = true;
	        	}
	        		
			});
	   
		   buttons.helpButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		if (!play){
	        			windows.helpWindow.setVisible(true);
	        		}
	        	}
	        		
			});
	  
		   buttons.pauseButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		world.step(1/60f, 6, 2);
	        		detector.detect(world);
	        		paused = true;
	        	}
	        		
			});
	   }

	   public void pausePlay() {
		   paused = false;
		   buttons.playButton.setText("Play");
		   play = !play;
		   resetLevel();
		   detector.on = true;

		   if(play) {
			   windows.setVisible(false);
			   buttons.playButton.setText("Stop");
			   for (int k = 0; k < bombs.length; k++) {
				   bombs[k].countdownTime = (int) Math.floor(((float)bombs[k].seconds + (float)bombs[k].centiSeconds/100)*60);
				   timeToWin = -1;
			   }
		   }
		   
		   world.setContactListener(soundListener.contactListener);
		   for (Bomb bomb:bombs) bomb.play = play; 
		}
}
