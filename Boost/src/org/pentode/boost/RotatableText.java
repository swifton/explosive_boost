package org.pentode.boost;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class RotatableText {
	String text;
	BitmapFont font; 
	SpriteBatch batch;

	public RotatableText(String t, SpriteBatch b) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TickingTimebombBB.ttf"));
	    font = generator.generateFont(57);//273);
	    font.setColor(Color.RED);
	    font.setFixedWidthGlyphs("0123456789");
	    generator.dispose();
	    
		text = t;
		batch = b;
	 }
	
	public void draw(float x, float y, float angle) {
		Matrix4 matrix = new Matrix4();
		matrix.setToTranslation(x, y, 0);
		matrix.rotate(new Vector3(0, 0, 1), angle);
	    batch.setTransformMatrix(matrix);
	    batch.begin();
	    font.draw(batch, text, 0, 0);
	    batch.end();
	    matrix.setToRotation(new Vector3(0, 0, 1), 0);
	    batch.setTransformMatrix(matrix);
	}

}
