package org.pentode.boost;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Boost implements ApplicationListener {
	   OrthographicCamera camera;
	   public Stage stage;
	   boolean postponedReset = false;
	   
	   static float BOX_TO_WORLD;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;

	   SpriteBatch batch;
	   
	   LevelSelect levelSelect;
	   StartScreen startScreen;
	   
	   Game game;

	   @Override
	   public void create() {
		   batch = new SpriteBatch();
		   camera = new OrthographicCamera();

		   stage = new Stage();
		   Gdx.input.setInputProcessor(stage);
		   
		   game = new Game(stage, batch);
		   levelSelect = new LevelSelect(stage);
		   startScreen = new StartScreen(stage);
		   
		   debugRenderer = new Box2DDebugRenderer();

		   levelSelect.container.setVisible(false);
		   
		   Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		   Label label = new Label("Xyu", skin);
		   stage.addActor(label);
	   }	  

	   @Override
	   public void render() {	
		   listenLevels();
		   
		   Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		   Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		   camera.apply(Gdx.gl10);

	      
		   camera.update();
		   batch.setProjectionMatrix(camera.combined);
		   
		   game.renderer.setProjectionMatrix(camera.combined);
		   
		   //debugRenderer.render(game.world, debugMatrix);
		   
		   game.render();

		   stage.draw();		   
	    	
		   //Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	   
	   private void listenLevels() {
		   
		   if (startScreen.message == "start") {
			   game.waiting = true;
			   startScreen.message  = "";
		   }
		   
		   if (levelSelect.levelNum != -1) {
			   game.levelNum = levelSelect.levelNum;
			   levelSelect.levelNum = -1;
			   game.loadLevel();
			   game.setVisible(true);
			   game.waiting = false;
		   }
		   
		   if (game.waiting) {
			   levelSelect.container.setVisible(true);
			   game.setVisible(false);
			   game.waiting = false;
		   }
		   
		   if (game.winWindow.message == "next") {
			   game.levelNum += 1;

			   if(game.levelNum > game.levels.list.length) {
				   game.levelNum = 1;
			   }
			   
			   game.loadLevel();
		   }
		   
		   if (game.winWindow.message == "select") {
			   game.setVisible(false);
			   levelSelect.container.setVisible(true);
		   }
		   
		   if (game.winWindow.message != "") {
			   game.winWindow.message = "";
			   game.pausePlay();
		   }
		   
		   if (game.complete) {
			   game.winWindow.window.setVisible(true);
			   game.complete = false;
			   game.timeToWin = -1;
		   }
		   
	   }
   
	   @Override
	   public void dispose() {
		   game.font.dispose();
		   game.sounds.dispose();
		   game.textures.dispose();
	   }

	   @Override
	   public void resize(int width, int height) {
		   float w = Gdx.graphics.getWidth();
		   float h = Gdx.graphics.getHeight();
		   if (h/w > 30/45) System.out.println("WTF?!");
		   
		   BOX_TO_WORLD = h/6 - 1;
		   game.BTW = BOX_TO_WORLD; 
		   game.cellSize = game.BTW / 5;
		   game.buttonSize = w - game.cellSize * 43;
		   if (game.buttonSize < 80) game.buttonSize = 80;
		   if (3 * game.buttonSize > h) game.buttonSize = h/3;
		   game.resizeButtons();
		   game.createDigits();

		   System.out.println(w);
		   System.out.println(h);
		   camera.setToOrtho(false, w, h);
		   
		   debugMatrix = new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }

	   @Override
	   public void pause() {
	   }

	   @Override
	   public void resume() {
	   }
}

