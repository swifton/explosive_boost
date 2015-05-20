package org.pentode.boost.ui;

import org.pentode.boost.objects.Bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TimeWindow {
	public Window window;
	Label labelSec;
	Label labelCen;
	public Label time;
	public Bomb bomb;
	
	BitmapFont mainFont;
	BitmapFont digits;
	
	public TimeWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		float density = Gdx.graphics.getDensity();
		int maxWindowWidth = (int) ((800 / 1.5) * density);
		int maxWindowHeigth = (int) ((500 / 1.5) * density);
		
		int windowWidth = Math.min(Gdx.graphics.getWidth(), maxWindowWidth);
		int windowHeight = Math.min(Gdx.graphics.getHeight(), maxWindowHeigth);
		
		int hPlus, wPlus, hClock, wClose;
		
		hPlus = (int) (windowHeight / 5);
		hClock = (int) (3 * windowHeight / 5);
		wPlus = (int) (3 * windowWidth / 8);
		wClose = (int) (windowWidth / 4);

		createFonts((int) (Math.min(hClock, wPlus) * 4/5), (int) (Math.min((int) windowHeight / 8, (int) windowWidth / 5)));
	    
	    LabelStyle style = new LabelStyle();
		style.font = digits;
		style.fontColor = Color.RED;
		time = new Label("", style);
		

	    
	    TextButtonStyle s = skin.get(TextButtonStyle.class);
	    s.font = mainFont;
	    s.fontColor = Color.BLACK;
		
		Button close = new TextButton("OK", s);
		Button plusSec = new TextButton("+", s);
		Button minusSec = new TextButton("-", s);
		Button plusCen = new TextButton("+", s);
		Button minusCen = new TextButton("-", s);
		
		SplitPane plus = new SplitPane(plusSec, plusCen, false, skin, "default-horizontal");
		SplitPane minus = new SplitPane(minusSec, minusCen, false, skin, "default-horizontal");

		window = new Window("", skin);
		
		window.defaults().spaceBottom(0);
		window.defaults().maxSize(windowWidth, windowHeight);
		window.defaults().center();
		
		window.row();
		window.add(plus).width(wPlus * 2).height(hPlus).bottom();
		window.row();
		window.add(time);
		window.add(close).width(wClose).height(hClock);
		window.row();
		window.add(minus).width(wPlus * 2).height(hPlus).top();
		window.pack();
		
		window.setMovable(false);
		stage.addActor(window);
		window.setVisible(false);
		window.setPosition((Gdx.graphics.getWidth() - windowWidth) / 2, (Gdx.graphics.getHeight() - windowHeight) / 2);
		
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
	
	private void createFonts(int heightOfDigits, int heightOfLetters) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DS-DIGI.TTF"));
		digits = generator.generateFont(generator.scaleForPixelHeight(heightOfDigits), "0123456789:", false);
	    digits.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	    
	    generator = new FreeTypeFontGenerator(Gdx.files.internal("SourceSansPro-Regular.otf"));
		mainFont = generator.generateFont(generator.scaleForPixelHeight(heightOfLetters), "+-OK", false);
	    generator.dispose();
	}

}

