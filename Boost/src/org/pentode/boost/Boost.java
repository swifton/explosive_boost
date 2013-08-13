package org.pentode.boost;

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
	   
	   static final float BOX_TO_WORLD = 200f;
	   Box2DDebugRenderer debugRenderer;
	   Matrix4 debugMatrix;

	   SpriteBatch batch;
	   
	   LevelSelect levelSelect;
	   
	   Game game;

	   @Override
	   public void create() {
		   batch = new SpriteBatch();
		   camera = new OrthographicCamera();

		   stage = new Stage();
		   Gdx.input.setInputProcessor(stage);
		   
		   game = new Game(stage, batch);
		   levelSelect = new LevelSelect(stage);
		   
		   debugRenderer = new Box2DDebugRenderer();

		   debugMatrix = new Matrix4(camera.combined);
		   debugMatrix.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1f);
	   }	  

	   @Override
	   public void render() {	
		   listenLevels();
		   
		   Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		   Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
	      
		   camera.update();
		   batch.setProjectionMatrix(camera.combined);
		   camera.apply(Gdx.gl10);
		   game.renderer.setProjectionMatrix(camera.combined);
		   
		   game.render();

		   stage.draw();
		   debugRenderer.render(game.world, debugMatrix);
	    	  
		   Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
	   }
	   
	   private void listenLevels() {
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

