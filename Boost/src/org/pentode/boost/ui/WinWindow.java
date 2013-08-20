package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WinWindow {
	public Window window;
	public String message = "";
	
	public WinWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Button next = new TextButton("Next level", skin);
		Button replay = new TextButton("Replay", skin);
		Button select = new TextButton("Select level", skin);
		
		Label win = new Label("Level complete!", skin);
		Label crap = new Label("", skin);
		
		int wC = 200;
		int hC = 100;
		int wW = 3 * wC;
		int hW = 2 * hC;
		int wS = Gdx.graphics.getWidth();
		int hS = Gdx.graphics.getHeight();
		
		window = new Window("", skin);
		window.defaults().spaceBottom(0);
		window.row();
		window.add(crap).minWidth(wC).minHeight(hC).bottom();
		window.add(win).minWidth(wC).minHeight(hC).bottom();
		window.row();
		window.add(next).minWidth(wC).minHeight(hC).bottom();
		window.add(replay).minWidth(wC).minHeight(hC).bottom();
		window.add(select).minWidth(wC).minHeight(hC).bottom();
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
}
