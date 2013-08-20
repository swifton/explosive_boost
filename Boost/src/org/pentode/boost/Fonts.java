package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {
	BitmapFont bombDigits;
	
	public Fonts(float cellSize) {
		createDigits(cellSize);
	}
	
	private void createDigits(float cellSize) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TickingTimebombBB.ttf"));
		bombDigits = generator.generateFont(generator.scaleForPixelHeight((int) (cellSize * 1.4)), "0123456789:", false);
		bombDigits.setColor(Color.RED);
		bombDigits.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	}
	
	public void dispose() {
		bombDigits.dispose();
	}
}
