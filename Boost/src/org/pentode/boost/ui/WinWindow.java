package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WinWindow {
	public Window window;
	public String message = "";
	Label timeLabel;
	int hC = 100;
	int wC = 200;
	Label previousTime;
	Label congr;
	Label lastTime;
	
	public WinWindow(Stage stage, BitmapFont digits) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Button next = new TextButton("Next level", skin);
		Button replay = new TextButton("Replay", skin);
		Button select = new TextButton("Select level", skin);
		
		Label win = new Label("Level complete!", skin);
		Label crap = new Label("", skin);
		congr = new Label("Best time!", skin);
		lastTime = new Label("Prevoius time:", skin);
		
		LabelStyle style = new LabelStyle();
		style.font = digits;
		style.fontColor = Color.RED;
		timeLabel = new Label("", style);
		previousTime = new Label("", style);
		
		int wW = 3 * wC;
		int hW = 3 * hC;
		int wS = Gdx.graphics.getWidth();
		int hS = Gdx.graphics.getHeight();
		
		window = new Window("", skin);
		window.defaults().spaceBottom(0);
		window.row();
		window.add(crap).minWidth(wC).minHeight(hC).bottom();
		window.add(win).minWidth(wC).minHeight(hC).bottom();
		window.add(timeLabel).minWidth(wC).minHeight(hC).bottom().align(Align.center);
		window.row();
		window.add(next).minWidth(wC).minHeight(hC).bottom();
		window.add(replay).minWidth(wC).minHeight(hC).bottom();
		window.add(select).minWidth(wC).minHeight(hC).bottom();
		window.row();
		window.add(congr).minWidth(wC).minHeight(hC).bottom();
		window.add(lastTime).minWidth(wC).minHeight(hC).bottom();
		window.add(previousTime).minWidth(wC).minHeight(hC).bottom();
		window.setVisible(false);
		window.setMovable(false);
		window.setWidth(wW);
		window.setHeight(hW);
		window.setPosition((wS - wW) / 2, (hS - hW) / 2);
		
		stage.addActor(window);
		
		next.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
				message = "next";
		    	return true;
		    }
		});
		
		replay.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
				message = "replay";
		    	return true;
		    }
		});
		
		select.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
				message = "select";
		    	return true;
		    }
		});
	}
	
	public void congratulate(boolean record) {
		if (record) {
			congr.setVisible(true);
			lastTime.setVisible(true);
			previousTime.setVisible(true);
		}
		else {
			congr.setVisible(false);
			lastTime.setVisible(false);
			previousTime.setVisible(false);
		}
	}
	
	public void setTime(int time) {
		timeLabel.setText(timeString(time));
	}
	
	public void setPrevoiusTime(int time) {
		previousTime.setText(timeString(time));
	}
	
	private String timeString(int time) {
		int seconds = (int) Math.floor(time / 60);
		int centiSeconds = time % 60 * 5 / 3;
		String sec = Integer.toString(seconds);
		if (seconds < 10) sec = "0" + sec;
		String cen = Integer.toString(centiSeconds);
		if (centiSeconds < 10) cen = "0" + cen;
		return sec + ":" + cen;
	}
}
