package org.pentode.boost;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class Game {
	TimeWindow timeWindow;
	WinWindow winWindow;
	HelpWindow helpWindow;
	private boolean visible = false;
	
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
	Wall [] walls;
	
	BitmapFont font;

	Levels levels = new Levels();
	
	Detector detector;
	
	Textures textures = new Textures();
	Sounds sounds = new Sounds();
	
	ContactListener contactListener;
	ShapeRenderer renderer;

	Array<Explosion> explosions = new Array<Explosion>();
	
	boolean play = false;
	boolean paused = false;
	TextButton playButton;
	TextButton selectButton;
	TextButton helpButton;
	TextButton pauseButton;
	int timeToWin = -1;
	
	int levelNum = 1;
	boolean waiting = false;
	boolean complete = false;
	
	Stage stage; 
	SpriteBatch batch;
	
	Sprite drag;
	int toDropX;
	int toDropY;
	QueryCallback AABBCallback;
	boolean droppable;
	
	public Game(Stage s, SpriteBatch b) {
		stage = s;
		batch = b;
		renderer = new ShapeRenderer();
		
		timeWindow = new TimeWindow(stage);
		winWindow = new WinWindow(stage);
		helpWindow = new HelpWindow(stage);
		
		createContactListener();
		createPlayButton(stage);
		createSelectButton(stage);
		createHelpButton(stage);
		createPauseButton(stage);
	}
	
	public void createDigits() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TickingTimebombBB.ttf"));
	    font = generator.generateFont(generator.scaleForPixelHeight((int) (cellSize * 1.4)), "0123456789:", false);
	    font.setColor(Color.RED);
	    font.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	}
	
	public void loadLevel() {
		loadLevelCoordinates(levels.list[levelNum - 1]);
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
			   bomb = new Bomb(coordB[i][0], coordB[i][1], world, stage, timeWindow, coordB[i][2], coordB[i][3], batch, BTW, font);
			   bombs[i] = bomb;
			   bomb.crate = new Sprite(textures.crateT, 28, 26, 443, 444);
			   bomb.crate.setSize(cellSize * 3, cellSize * 3);
			   bomb.crate.setOrigin(cellSize * 3/2, cellSize * 3/2);
			   bomb.sound = sounds.explosionSound;
		   }
		   
		   // Bricks
		   Brick brick;
		   
		   bricks = new Brick[coordBr.length];
		   
		   if (coordBr != null) {
			   for (int i = 0; i < coordBr.length; i++) {
				   brick = new Brick(coordBr[i][0], coordBr[i][1], coordBr[i][2], coordBr[i][3], world, BTW);
				   brick.sprite = new Sprite(textures.brickT, 0, 62, 64, 32);
				   brick.sprite.setSize(cellSize * 2, cellSize);
				   brick.sprite.setOrigin(cellSize, cellSize / 2);
				   bricks[i] = brick;
			   }
		   }
	   }
	   
	   private void createContactListener() {
		   contactListener = new ContactListener() {
               public void beginContact(Contact contact) {
            	   if (contact.getFixtureB().getBody().getUserData() == "ball") {
            		   sounds.ballSound.play();
            	   }
            	   
            	   if (contact.getFixtureB().getBody().getUserData() == "brick") {
            		   //sounds.brickSound.play();
            	   }
               }
               
               public void endContact(Contact contact) {}
               public void postSolve(Contact contact, ContactImpulse impulse) {}
               public void preSolve(Contact contact, Manifold oldManifold) {}
		   };
	   }
	   
	   public void setVisible(boolean v) {
		   visible = v;
		   playButton.setVisible(v);
		   selectButton.setVisible(v);
		   helpButton.setVisible(v);
		   pauseButton.setVisible(false);
		   
		   if (!v) {
			   timeWindow.window.setVisible(false);
			   helpWindow.window.setVisible(false);
			   winWindow.window.setVisible(false);
		   }
	   }
	   
	   public void render() {
		   if (!visible) return;
		   
		   renderSprites();
		   
		   for (Explosion e:explosions) {
			   e.draw(batch);
		   }
		   
		   detector.draw(batch, renderer);
		   
		   cleanupExplosions();
		   
		   if (detector.detect(world)) {
			   timeToWin = 100;
			   detector.on = false;
			   sounds.detectorSound.play();
		   }
		   
		   if (!play) {
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
		   
		   if (play && !paused) {
			   for (int i = 0; i < bombs.length; i++) bombs[i].countDown(explosions);
			   world.step(1/60f, 6, 2);
			   timeToWin -= 1;
			   if (timeToWin == 0) complete = true;
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
		   
		   playButton.setVisible(visible);
	       stage.addActor(playButton);
	        
	       playButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		pausePlay();
	        	}
	        		
			});
	   }
	   
	   private void createSelectButton(Stage stage) {
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   selectButton = new TextButton("Select level", skin);
		   
		   selectButton.setVisible(visible);
	       stage.addActor(selectButton);
	        
	       selectButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		setVisible(false);
	        		waiting = true;
	        	}
	        		
			});
	   }
	   
	   private void createHelpButton(Stage stage) {
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   helpButton = new TextButton("Help", skin);
		   
		   helpButton.setVisible(visible);
	       stage.addActor(helpButton);
	        
	       helpButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		if (!play){
	        			helpWindow.window.setVisible(true);
	        		}
	        	}
	        		
			});
	   }
	   
	   private void createPauseButton(Stage stage) {
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   pauseButton = new TextButton("Help", skin);
		   
		   pauseButton.setVisible(false);
	       stage.addActor(pauseButton);
	        
	       pauseButton.addListener(new ChangeListener() {
	        	@Override
				public void changed (ChangeEvent event, Actor actor) {
	        		if (paused) world.step(1/60f, 6, 2);
	        		paused = true;
	        	}
	        		
			});
	   }
	   
	   public void resizeButtons() {
		   float w = Gdx.graphics.getWidth();
		   float h = Gdx.graphics.getHeight();
		   float size = buttonSize;
		   
		   playButton.setX(w - size);
		   playButton.setY(h - size - 5);
		   playButton.setWidth(size);
		   playButton.setHeight(size);
		   
		   selectButton.setX(w - size);
		   selectButton.setY(h - 2 * size - 5);
		   selectButton.setWidth(size);
		   selectButton.setHeight(size);
		   
		   helpButton.setX(w - size);
		   helpButton.setY(h - 3 * size - 5);
		   helpButton.setWidth(size);
		   helpButton.setHeight(size);
		   
		   pauseButton.setX(w - size);
		   pauseButton.setY(h - 4 * size - 5);
		   pauseButton.setWidth(size);
		   pauseButton.setHeight(size);
	   }
	   
	   public void pausePlay() {
		   paused = false;
		   playButton.setText("Play");
		   play = !play;
		   resetLevel();
		   detector.on = true;

		   if(play) {
			   timeWindow.window.setVisible(false);
			   helpWindow.window.setVisible(false);
			   playButton.setText("Stop");
			   for (int k = 0; k < bombs.length; k++) {
				   bombs[k].countdownTime = (int) Math.floor(((float)bombs[k].seconds + (float)bombs[k].centiSeconds/100)*60);
				   timeToWin = -1;
			   }
		   }
		   
		   world.setContactListener(contactListener);
		   for (Bomb bomb:bombs) bomb.play = play; 
		}
}
