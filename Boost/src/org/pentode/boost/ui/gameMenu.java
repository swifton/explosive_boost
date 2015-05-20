package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class gameMenu {
	Window window;	
	BitmapFont mainFont;
	Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	float density = Gdx.graphics.getDensity();
	
	public gameMenu(Stage stage) {
		int width = Gdx.graphics.getWidth();
		int dpWidth = (int) (width / density);
		
		if (dpWidth > 800) createFonts((int) (45 * density));
		if ((dpWidth <= 800) && (dpWidth >= 400)) createFonts((int) (20 * density));
		if (dpWidth < 400) createFonts((int) (10 * density));
	    
	    TextButtonStyle st = skin.get(TextButtonStyle.class);
	    st.font = mainFont;
	    st.fontColor = Color.BLACK;	    
		
		Button resume = new TextButton("Resume", st);
		Button mainMenu = new TextButton("Main Menu", st);
		Button select = new TextButton("Select Level", st);
		Button skip = new TextButton("Skip Level", st);
			
		//int lW = (int) hint5.getWidth() + 50;
		int lH = 50;
		int bH = 100;
		int wS = Gdx.graphics.getWidth();
		int hS = Gdx.graphics.getHeight();
		
		window = new Window("", skin);
		window.defaults().spaceBottom(0);
		
		window.add(skip).minWidth(100).minHeight(100);
		
		window.pack();
		window.setMovable(false);
		stage.addActor(window);
		window.setVisible(false);
		window.setPosition((wS - window.getWidth()) / 2, (hS - window.getHeight()) / 2);
				
		resume.addListener(new ClickListener() {
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
