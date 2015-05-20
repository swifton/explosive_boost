package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class LevelSelect {
	public int levelNum = -1;
	     
	private Skin skin;
	TextButtonStyle tbs;
	public Table container;
	LabelStyle labelStyle;
	Preferences prefs = Gdx.app.getPreferences("My Preferences");
	Array<Label> times;
	LabelStyle lS;
	
	float density;
	int wid = 5;
	int hei = 3;
	
	BitmapFont mainFont;
	BitmapFont digits;

	public LevelSelect(Stage stage, int levelsNumber) {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		density = Gdx.graphics.getDensity();
		
		createFonts((int) (40 * density), (int) (60 * density));
		
		skin.add("top", skin.newDrawable("default-round", Color.BLUE), Drawable.class);
		skin.add("default-font", mainFont);

		
    	lS = new LabelStyle();
		lS.font = mainFont;
        
		container = new Table();
		stage.addActor(container);
		container.setFillParent(true);
		
		labelStyle = new LabelStyle();
		labelStyle.font = digits;
		labelStyle.background = skin.newDrawable("default-round", Color.BLACK);
		labelStyle.fontColor = Color.RED;
		times = new Array<Label>();

		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		
		float dpHeight = Gdx.graphics.getHeight() / density;
		float dpWidth = Gdx.graphics.getWidth() / density;
		
		if (dpWidth < 1280f / 1.5) wid = 4;
		if (dpWidth < 1280f / 3) wid = 3;
		if (dpHeight < 800f / 1.5) hei = 2;
		
		createButtons(levelsNumber, scroll);
		
		container.add(scroll).expand().fill();
	}
	
	private void createButtons(int totalLevelsNumber, PagedScrollPane scroll) {
		Button button;
		float h = Gdx.graphics.getHeight() / (density * 160);
		
		//if (h < 3f) {
		//	wid = 2;
		//	hei = 2;
		//}
		
		int pages = (int) Math.ceil((float) totalLevelsNumber / (wid * hei));

		int c = 1;
		for (int l = 0; l < pages; l++) {
			Table levels = new Table().pad(50);
			levels.defaults().pad(20, 40, 20, 40);
			for (int y = 0; y < hei; y++) {
				levels.row();
				for (int x = 0; x < wid; x++) {
					button = getLevelButton(c++);
					levels.add(button).expand().fill();
					if (c - 1 > totalLevelsNumber) button.setVisible(false);
				}
			}
			scroll.addPage(levels);
		}
	}
	

    public Button getLevelButton(int level) {
        Button button = new Button(skin);
    	//Button button = new Button(tbs);

        ButtonStyle style = button.getStyle();
        style.up =  style.down = null;
            
        // Create the label to show the level number
        Label label = new Label(Integer.toString(level), lS);
        //label.setFontScale(2f);
        label.setAlignment(Align.center);  
        
        Label timeLabel = new Label("00:00", labelStyle);
        timeLabel.setAlignment(Align.bottom);
        times.add(timeLabel);
        
        Table labels = new Table(skin);
        labels.add(label);
        labels.row();
        labels.add(timeLabel);
            
        // Stack the image and the label at the top of our button
        button.stack(new Image(skin.getDrawable("top")), labels).expand().fill();
            
        button.row();
            
        button.setName(Integer.toString(level));
        button.addListener(levelClickListener);    
        return button;
    }
    
    public void setVisible(boolean visible) {
    	container.setVisible(visible);
    	for (int i = 0; i < times.size; i++) {
    		int t = prefs.getInteger(Integer.toString(i + 1)) ;
        	if (t != 0) {
        		times.get(i).setText(timeString(t));
        		times.get(i).setVisible(visible);
        	}
        	else {
        		times.get(i).setVisible(false);
        	}
    	}
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
    
    public ClickListener levelClickListener = new ClickListener() {
    	@Override
    	public void clicked (InputEvent event, float x, float y) {
    		//System.out.println("Click: " + event.getListenerActor().getName());
    		container.setVisible(false);
    		levelNum = Integer.parseInt(event.getListenerActor().getName());
    	}	
    };
    
	private void createFonts(int heightOfDigits, int heightOfLetters) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DS-DIGI.TTF"));
		digits = generator.generateFont(generator.scaleForPixelHeight(heightOfDigits), "0123456789:", false);
	    digits.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	    
	    generator = new FreeTypeFontGenerator(Gdx.files.internal("SourceSansPro-Regular.otf"));
		mainFont = generator.generateFont(generator.scaleForPixelHeight(heightOfLetters), "1234567890", false);
	    generator.dispose();
	}
}