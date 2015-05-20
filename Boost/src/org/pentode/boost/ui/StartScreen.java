package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class StartScreen {
	Label name;
	Button play;
	public String message = "";
	public BitmapFont font;
	TextButtonStyle bStyle;
	
	public StartScreen(Stage stage, BitmapFont main, BitmapFont title) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DS-DIGI.TTF"));
	    //font = generator.generateFont(generator.scaleForPixelHeight((int)(h/7)), "explosivbt ", false);
	    //generator.dispose();
	    
	    LabelStyle style = new LabelStyle();
		style.font = title;
		style.fontColor = Color.RED;
		
		name = new Label("explosive boost", style);
		name.setPosition((w - name.getWidth()) / 2, 2 * h / 3);
		
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		//skin.add("main", main);
		//play = new TextButton("Play", skin);
		//TextureRegionDrawable dr = new TextureRegionDrawable(new TextureRegion(new Texture("crate_no_timer.png")));
		//bStyle = new TextButtonStyle(dr, dr, dr);
		//bStyle.font = main;
		//bStyle.fontColor = Color.BLACK;
		//play = new TextButton("Play", bStyle);
		
		Drawable dr = skin.newDrawable("default-round");
		bStyle = new TextButtonStyle(dr, dr, dr);
		bStyle.font = main;
		bStyle.fontColor = Color.BLACK;
		play = new TextButton("Play", bStyle);


		stage.addActor(play);
	        
	    play.addListener(new ChangeListener() {
	    	@Override
	    	public void changed (ChangeEvent event, Actor actor) {
	    		hide();
	    		message = "start";
	        }
		});
	    
	    play.setPosition(w/2 - 100, h/4);
	    //play.setSize(200, 100);
		
	    stage.addActor(play);
		stage.addActor(name);
	}
	
	private void hide() {
		play.setVisible(false);
		name.setVisible(false);
	}
}
