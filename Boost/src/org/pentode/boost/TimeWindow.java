package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TimeWindow {
	Window window;
	Label labelSec;
	Label labelCen;
	Label time;
	Bomb bomb;
	
	public TimeWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		Gdx.input.setInputProcessor(stage);
		Button close = new TextButton("OK", skin);
		Button plusSec = new TextButton("+", skin);
		Button minusSec = new TextButton("-", skin);
		Button plusCen = new TextButton("+", skin);
		Button minusCen = new TextButton("-", skin);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TickingTimebombBB.ttf"));
	    BitmapFont font = generator.generateFont(273);
	    font.setColor(Color.RED);
	    font.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
		
		LabelStyle style = new LabelStyle();
		style.font = font;
		style.fontColor = Color.RED;
		time = new Label("", style);
		
		SplitPane plus = new SplitPane(plusSec, plusCen, false, skin, "default-horizontal");
		SplitPane minus = new SplitPane(minusSec, minusCen, false, skin, "default-horizontal");
		
		window = new Window("", skin);
		window.setPosition(400, 400);
		window.defaults().spaceBottom(0);
		window.row();
		window.add(plus).minWidth(600).minHeight(100).bottom();
		window.row();
		window.add(time);
		window.add(close).minWidth(200).minHeight(320);
		window.row();
		window.add(minus).minWidth(600).minHeight(100).top();
		window.setSize(820, 550);
		window.setMovable(false);
		stage.addActor(window);
		window.setVisible(false);
		
		close.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				window.setVisible(false);
				bomb.resetCurrentTime();
				bomb = null;
		    	return true;
		    }
		});
		
		plusSec.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.seconds < 99) bomb.seconds += 1;
	    		time.setText(bomb.givenTime());
		    	return true;
		    }
		});
		
		minusSec.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.seconds > 0) bomb.seconds -= 1;
		    	time.setText(bomb.givenTime());
		    	return true;
		    }
		});
		
		plusCen.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.centiSeconds < 90) bomb.centiSeconds += 10;
		    	else bomb.centiSeconds = 0;
		    	time.setText(bomb.givenTime());
		    	return true;
		    }
		});
		
		minusCen.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	if (bomb.centiSeconds > 0) bomb.centiSeconds -= 10;
		    	else bomb.centiSeconds = 90;
		    	time.setText(bomb.givenTime());
		    	return true;
		    }
		});
	}
}

