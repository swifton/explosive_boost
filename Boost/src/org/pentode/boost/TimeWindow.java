package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TimeWindow {
	Stage stagee;
	Window window;
	Label label;
	Bomb bomb;
	
	public TimeWindow(Stage stage) {
		stagee = stage;
		//stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Gdx.input.setInputProcessor(stage);
		Button close = new TextButton("OK", skin);
		Button plus = new TextButton("+", skin);
		Button minus = new TextButton("-", skin);
		label = new Label("", skin);
		
		Table t = new Table();
		t.row();
		t.add(plus).minWidth(200).minHeight(100);
		t.row();
		t.add(label).minHeight(50);
		t.row();
		t.add(minus).minWidth(200).minHeight(100);
		
		SplitPane splitPane = new SplitPane(t, close, false, skin, "default-horizontal");

		
		window = new Window("Dialog", skin);
		window.setPosition(400, 400);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(splitPane);
		window.pack();
		stage.addActor(window);
		
		close.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
				bomb = null;
		    	return true;
		    }
		});
		
		plus.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	bomb.givenCountdownTime += 10;
	    		label.setText((CharSequence) Integer.toString(bomb.givenCountdownTime));
		    	return true;
		    }
		});
		
		minus.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.givenCountdownTime > 0) {
		    		bomb.givenCountdownTime -= 10;
		    	}
	    		label.setText((CharSequence) Integer.toString(bomb.givenCountdownTime));
		    	return true;
		    }
		});
	}
}
