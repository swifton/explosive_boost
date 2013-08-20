package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HelpWindow {
	Window window;
	
	public HelpWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Button close = new TextButton("Close", skin);
		Label hint1 = new Label("Press the Play button and see what happens", skin);
		Label hint2 = new Label("Wooden crates with timers are bombs", skin);
		Label hint3 = new Label("Drag a bomb to change its initial position", skin);
		Label hint4 = new Label("Touch a bomb to set the countdown timer", skin);
		Label hint5 = new Label("The goal is to make the ball cross the red laser beam", skin);
		hint1.setAlignment(Align.center);
		hint2.setAlignment(Align.center);
		hint3.setAlignment(Align.center);
		hint4.setAlignment(Align.center);
		hint5.setAlignment(Align.center);
		
		int lW = 400;
		int lH = 50;
		int bW = lW;
		int bH = 100;
		int wW = lW;
		int hW = lH * 5 + bH;
		int wS = Gdx.graphics.getWidth();
		int hS = Gdx.graphics.getHeight();
		
		window = new Window("", skin);
		window.defaults().spaceBottom(0);
		window.add(hint1).minWidth(lW).minHeight(lH);
		window.row();
		window.add(hint2).minWidth(lW).minHeight(lH);
		window.row();
		window.add(hint3).minWidth(lW).minHeight(lH);
		window.row();
		window.add(hint4).minWidth(lW).minHeight(lH);
		window.row();
		window.add(hint5).minWidth(lW).minHeight(lH);
		window.row();
		window.add(close).minWidth(bW).minHeight(bH);
		window.setSize(wW, hW);
		window.setMovable(false);
		stage.addActor(window);
		window.setVisible(false);
		window.setPosition((wS - wW) / 2, (hS - hW) / 2);
				
		close.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
		    	return true;
		    }
		});
	}
	
	public void setVisible(boolean visible) {
		window.setVisible(visible);
	}
}
