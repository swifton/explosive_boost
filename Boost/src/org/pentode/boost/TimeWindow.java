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
	Label labelSec;
	Label labelCen;
	Bomb bomb;
	
	public TimeWindow(Stage stage) {
		stagee = stage;
		//stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Gdx.input.setInputProcessor(stage);
		Button close = new TextButton("OK", skin);
		Button plusSec = new TextButton("+", skin);
		Button minusSec = new TextButton("-", skin);
		Button plusCen = new TextButton("+", skin);
		Button minusCen = new TextButton("-", skin);
		labelSec = new Label("", skin);
		labelCen = new Label("", skin);
		
		Table t = new Table();
		t.row();
		t.add(plusSec).minWidth(100).minHeight(100);
		t.add(plusCen).minWidth(100).minHeight(100);
		t.row();
		t.add(labelSec).minHeight(50);
		t.add(labelCen).minHeight(50);
		t.row();
		t.add(minusSec).minWidth(100).minHeight(100);
		t.add(minusCen).minWidth(100).minHeight(100);
		
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
		
		plusSec.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.seconds < 99) bomb.seconds += 1;
	    		labelSec.setText((CharSequence) Integer.toString(bomb.seconds));
		    	return true;
		    }
		});
		
		minusSec.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.seconds > 0) bomb.seconds -= 1;
	    		labelSec.setText((CharSequence) Integer.toString(bomb.seconds));
		    	return true;
		    }
		});
		
		plusCen.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.centiSeconds < 99) bomb.centiSeconds += 10;
	    		labelCen.setText((CharSequence) Integer.toString(bomb.centiSeconds));
		    	return true;
		    }
		});
		
		minusCen.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.centiSeconds > 0) bomb.centiSeconds -= 10;
	    		labelCen.setText((CharSequence) Integer.toString(bomb.centiSeconds));
		    	return true;
		    }
		});
	}
}
