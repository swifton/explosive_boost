package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Fonts {
	BitmapFont bombDigits;
	BitmapFont main;
	BitmapFont smallMain;
	BitmapFont title;
	TextButtonStyle bStyle;
	
	public Fonts(float cellSize) {
		createDigits(cellSize);
		createMainFont(cellSize);
		createTitleFont(cellSize);
		
		//TextureRegionDrawable dr = new TextureRegionDrawable(new TextureRegion(new Texture("Button2h.png"), 409, 278));
		//bStyle = new TextButtonStyle(dr, dr, dr);
		//bStyle.font = main;
		//bStyle.fontColor = Color.BLACK;
	}
	
	private void createDigits(float cellSize) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DS-DIGI.TTF"));
		bombDigits = generator.generateFont(generator.scaleForPixelHeight((int) (cellSize * 1.3)), "0123456789:", false);
		bombDigits.setColor(Color.RED);
		bombDigits.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	}
	
	private void createMainFont(float cellSize) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SourceSansPro-Regular.otf"));
		main = generator.generateFont(generator.scaleForPixelHeight((int) (cellSize * 2.7)));
		smallMain = generator.generateFont(generator.scaleForPixelHeight((int) (cellSize * 1.5)));
		main.setColor(Color.BLACK);
	    generator.dispose();
	}

	private void createTitleFont(float cellSize) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("firecat.medium.ttf"));
		title = generator.generateFont(generator.scaleForPixelHeight((int) (Gdx.graphics.getHeight()/6)), "explosiv bt", false);   
		title.setColor(Color.RED);
	    generator.dispose();
	}
	
	public void dispose() {
		bombDigits.dispose();
		main.dispose();
		title.dispose();
	}
}
