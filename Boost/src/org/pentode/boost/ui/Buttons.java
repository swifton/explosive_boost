package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Buttons {
	public TextButton playButton;
	public TextButton selectButton;
	public TextButton helpButton;
	public TextButton pauseButton;
	Label totalTime;
	Label bestTime;
	Table timeRecord;
	
	public Buttons(Stage stage, float cellSize, BitmapFont digits) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		playButton = new TextButton("Play", skin);
		selectButton = new TextButton("Select level", skin);
		helpButton = new TextButton("Help", skin);
		pauseButton = new TextButton("Pause", skin);
		bestTime = new Label("Best time:", skin);
		bestTime.setVisible(false);
		
		LabelStyle style = new LabelStyle();
		style.font = digits;
		style.fontColor = Color.RED;
		totalTime = new Label("", style);
		timeRecord = new Table(skin);
		timeRecord.add(bestTime);
		timeRecord.row();
		timeRecord.add(totalTime);
		
		playButton.setVisible(false);
		selectButton.setVisible(false);
		helpButton.setVisible(false);
		pauseButton.setVisible(false);
		
		stage.addActor(playButton);
		stage.addActor(selectButton);
		stage.addActor(helpButton);
	    stage.addActor(pauseButton);
	    stage.addActor(timeRecord);
	    
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
		   
		   timeRecord.setX(w - size/2);		  
		   timeRecord.setY(h - 3.5f * size - 5);
		   
		   pauseButton.setX(w - size);
		   pauseButton.setY(h - 4 * size - 5);
		   pauseButton.setWidth(size);
		   pauseButton.setHeight(size);
	}
	
	public void setVisible(boolean visible, boolean pauseVisible) {
		   playButton.setVisible(visible);
		   selectButton.setVisible(visible);
		   helpButton.setVisible(visible);
		   totalTime.setVisible(visible);
		   bestTime.setVisible(visible);
		   pauseButton.setVisible(pauseVisible);
	}
	
	public void setTime(int time) {
		if (time == 0) {
			timeRecord.setVisible(false);
			return;
		}
		timeRecord.setVisible(true);
		int seconds = (int) Math.floor(time / 60);
		int centiSeconds = time % 60 * 5 / 3;
		String sec = Integer.toString(seconds);
		if (seconds < 10) sec = "0" + sec;
		String cen = Integer.toString(centiSeconds);
		if (centiSeconds < 10) cen = "0" + cen;
		totalTime.setText(sec + ":" + cen);
	}
}
