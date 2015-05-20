package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HelpWindow {
	Window window;	
	BitmapFont mainFont;
	Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	float density = Gdx.graphics.getDensity();
	
	public HelpWindow(Stage stage) {
		int width = Gdx.graphics.getWidth();
		int dpWidth = (int) (width / density);
		
		if (dpWidth > 800) createFonts((int) (45 * density));
		if ((dpWidth <= 800) && (dpWidth >= 400)) createFonts((int) (20 * density));
		if (dpWidth < 400) createFonts((int) (10 * density));
		
	    LabelStyle s = new LabelStyle();
	    s.font = mainFont;
	    
	    TextButtonStyle st = skin.get(TextButtonStyle.class);
	    st.font = mainFont;
	    st.fontColor = Color.BLACK;	    
		
		Button close = new TextButton("Close", st);
		Label hint1 = new Label("Press the Play button and see what happens", s);
		Label hint2 = new Label("Wooden crates with timers are bombs", s);
		Label hint3 = new Label("Drag a bomb to change its initial position", s);
		Label hint4 = new Label("Tap a bomb to set the countdown timer", s);
		Label hint5 = new Label("The goal is to make the ball cross the red laser beam", s);
	
		hint1.setAlignment(Align.center);
		hint2.setAlignment(Align.center);
		hint3.setAlignment(Align.center);
		hint4.setAlignment(Align.center);
		hint5.setAlignment(Align.center);
		
		int lW = (int) hint5.getWidth() + 50;
		int lH = 50;
		int bH = 100;
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
		window.add(close).minWidth(lW).minHeight(bH);
		window.pack();
		window.setMovable(false);
		stage.addActor(window);
		window.setVisible(false);
		window.setPosition((wS - window.getWidth()) / 2, (hS - window.getHeight()) / 2);
				
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
	
	private void createFonts(int heightOfLetters) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SourceSansPro-Regular.otf"));
		mainFont = generator.generateFont(generator.scaleForPixelHeight(heightOfLetters));
	    generator.dispose();
	}
}
