package org.pentode.boost;

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
	Window window;
	String message = "";
	
	public WinWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Button next = new TextButton("Next level", skin);
		Button replay = new TextButton("Replay", skin);
		Button select = new TextButton("Select level", skin);
		
		Label win = new Label("Level complete!", skin);
		Label crap = new Label("", skin);
		
		window = new Window("", skin);
		window.setPosition(400, 400);
		window.defaults().spaceBottom(0);
		window.row();
		window.add(crap).minWidth(200).minHeight(100).bottom();
		window.add(win).minWidth(200).minHeight(100).bottom();
		window.row();
		window.add(next).minWidth(200).minHeight(100).bottom();
		window.add(replay).minWidth(200).minHeight(100).bottom();
		window.add(select).minWidth(200).minHeight(100).bottom();
		window.setVisible(false);
		window.setHeight(200);
		window.setWidth(600);
		
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
