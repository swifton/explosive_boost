package org.pentode.boost;

import org.pentode.boost.ui.LevelSelect;
import org.pentode.boost.ui.StartScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

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
		   levelSelect = new LevelSelect(stage, game.fonts.bombDigits);
		   startScreen = new StartScreen(stage);
		   
		   debugRenderer = new Box2DDebugRenderer();

		   levelSelect.setVisible(false);
	   }	  

	   @Override
	   public void render() {	
		   listenLevels();
		   
		   Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		   Gdx.gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);
		   Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		   camera.apply(Gdx.gl10);

	      
		   camera.update();
		   batch.setProjectionMatrix(camera.combined);
		   		   
		   //debugRenderer.render(game.world, debugMatrix);
		   
		   game.render();

		   stage.draw();	
	       stage.act(Gdx.graphics.getDeltaTime());

	    	
		   Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	   
	   private void listenLevels() {
		   if (startScreen.message == "start") {
			   levelSelect.setVisible(true);
			   startScreen.message  = "";
		   }
		   
		   if (levelSelect.levelNum != -1) {
			   game.levelNum = levelSelect.levelNum;
			   levelSelect.levelNum = -1;
			   game.setVisible(true);
			   game.loadLevel();
			   game.waiting = false;
		   }
		   
		   if (game.waiting) {
			   levelSelect.setVisible(true);
			   if (game.play) game.buttons.playButton.toggle();
			   game.setVisible(false); 
			   game.waiting = false;
		   }
		   
		   if (game.windows.winWindow.message == "next") {
			   game.levelNum += 1;

			   if(game.levelNum > game.levels.list.length) {
				   game.levelNum = 1;
			   }
			   
			   game.loadLevel();
		   }
		   
		   if (game.windows.winWindow.message == "select") {
			   game.setVisible(false);
			   levelSelect.setVisible(true);
		   }
		   
		   if (game.windows.winWindow.message == "replay") {
				game.buttons.setTime(game.prefs.getInteger(Integer.toString(game.levelNum)));
		   }
		   
		   if (game.windows.winWindow.message != "") {
			   game.windows.winWindow.message = "";
			   if (game.play) game.buttons.playButton.toggle();
		   }
		   
		   if (game.complete) {
			   game.windows.winWindow.window.setVisible(true);
			   game.complete = false;
			   game.timeToWin = -1;
		   }
		   
	   }
   
	   @Override
	   public void dispose() {
		   game.fonts.dispose();
		   game.sounds.dispose();
		   game.textures.dispose();
		   game.batch.dispose();
		   startScreen.font.dispose();
	   }

	   @Override
	   public void resize(int width, int height) {
		   float w = Gdx.graphics.getWidth();
		   float h = Gdx.graphics.getHeight();
		   
		   BOX_TO_WORLD = h/6 - 1;
		   game.BTW = BOX_TO_WORLD; 
		   game.cellSize = game.BTW / 5;

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

