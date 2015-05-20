package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WinWindow {
	public Window window;
	public String message = "";
	Label timeLabel;
	Label previousTime;
	Label congr;
	Label lastTime;
	
	BitmapFont mainFont;
	BitmapFont digits;
	
	public WinWindow(Stage stage) {
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		float density = Gdx.graphics.getDensity();
		int maxWindowWidth = (int) (600 * density);
		int maxWindowHeigth = (int) (200 * density);
		
		int windowWidth = Math.min(Gdx.graphics.getWidth(), maxWindowWidth);
		int windowHeight = Math.min(Gdx.graphics.getHeight(), maxWindowHeigth);
		
		int hButton, wButton;
		
		wButton = (int) (windowWidth / 3);
		hButton = (int) (windowHeight / 3);
		
		createFonts((int) (hButton * 3 / 4), (int) hButton / 2, wButton);
	    
	    TextButtonStyle s = skin.get(TextButtonStyle.class);
	    s.font = mainFont;
	    s.fontColor = Color.BLACK;
	    
	    Button next = new TextButton("Next level", s);
		Button replay = new TextButton("Replay", s);
		Button select = new TextButton("Select level", s);
		
		LabelStyle st = new LabelStyle();
		st.font = mainFont;
		
		Label win = new Label("Level complete!", st);
		win.setAlignment(Align.center);
		Label blank = new Label("", st);
		congr = new Label("Best time!", st);
		congr.setAlignment(Align.center);
		lastTime = new Label("Prevoius time:", st);
		lastTime.setAlignment(Align.center);
		
		LabelStyle style = new LabelStyle();
		style.font = digits;
		style.fontColor = Color.RED;
		timeLabel = new Label("", style);
		timeLabel.setAlignment(Align.center);
		previousTime = new Label("", style);
		previousTime.setAlignment(Align.center);
		
		window = new Window("", skin);
		window.defaults().spaceBottom(0);
		window.defaults().center();
		
		window.row();
		window.add(blank).minWidth(wButton).minHeight(hButton);
		window.add(win).minWidth(wButton).minHeight(hButton);
		window.add(timeLabel).minWidth(wButton).minHeight(hButton);
		window.row();
		window.add(next).minWidth(wButton).minHeight(hButton);
		window.add(replay).minWidth(wButton).minHeight(hButton);
		window.add(select).minWidth(wButton).minHeight(hButton);
		window.row();
		window.add(congr).minWidth(wButton).minHeight(hButton);
		window.add(lastTime).minWidth(wButton).minHeight(hButton);
		window.add(previousTime).minWidth(wButton).minHeight(hButton);
		
		window.setVisible(false);
		window.setMovable(false);
		//window.setWidth(windowWidth);
		//window.setHeight(windowHeight);
		window.pack();
		window.setPosition((Gdx.graphics.getWidth() - windowWidth) / 2, (Gdx.graphics.getHeight() - windowHeight) / 2);
		
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
	
	private void createFonts(int heightOfDigits, int heightOfLetters, int wButton) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SourceSansPro-Regular.otf"));
		// bad hack - just made everything 2 times smaller to squeeze into the given space
		mainFont = generator.generateFont(generator.scaleForPixelHeight(heightOfLetters));
		float tmp = testFontSize(mainFont, wButton);
		if (tmp > 1) mainFont = generator.generateFont(generator.scaleForPixelHeight((int) (heightOfLetters / tmp)));
	    generator.dispose();
	    
	    generator = new FreeTypeFontGenerator(Gdx.files.internal("DS-DIGI.TTF"));
		digits = generator.generateFont(generator.scaleForPixelHeight(heightOfDigits), "0123456789:", false);
	    digits.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	}
	
	private float testFontSize(BitmapFont font, int desiredWidth) {
		LabelStyle sty = new LabelStyle();
		sty.font = font;
		Label tmp = new Label("Level complete!", sty);
		return tmp.getWidth() / desiredWidth;
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
