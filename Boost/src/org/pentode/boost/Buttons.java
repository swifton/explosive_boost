package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Buttons {
	TextButton playButton;
	TextButton selectButton;
	TextButton helpButton;
	TextButton pauseButton;
	
	public Buttons(Stage stage, float cellSize) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		playButton = new TextButton("Play", skin);
		selectButton = new TextButton("Select level", skin);
		helpButton = new TextButton("Help", skin);
		pauseButton = new TextButton("Pause", skin);
		
		playButton.setVisible(false);
		selectButton.setVisible(false);
		helpButton.setVisible(false);
		pauseButton.setVisible(false);
		
		stage.addActor(playButton);
		stage.addActor(selectButton);
		stage.addActor(helpButton);
	    stage.addActor(pauseButton);
	    
	    resize(cellSize);
	}
	
	private void resize(float cellSize) {
		   float w = Gdx.graphics.getWidth();
		   float h = Gdx.graphics.getHeight();
		   float size = w - cellSize * 43;
		   if (size < 80) size = 80;
		   if (3 * size > h) size = h/3;
		   
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
	
	public void setVisible(boolean visible, boolean pauseVisible) {
		   playButton.setVisible(visible);
		   selectButton.setVisible(visible);
		   helpButton.setVisible(visible);
		   pauseButton.setVisible(pauseVisible);
	}
}
