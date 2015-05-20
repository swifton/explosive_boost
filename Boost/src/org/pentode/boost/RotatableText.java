package org.pentode.boost;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class RotatableText {
	public String text;
	BitmapFont font; 
	SpriteBatch batch;

	public RotatableText(String t, SpriteBatch b, float cellSize, BitmapFont f) {
		font = f;
		text = t;
		batch = b;
	 }
	
	public void draw(float x, float y, float angle, float cellSize) {
		Matrix4 matrix = new Matrix4();
		matrix.setToTranslation(x, y, 0);
		matrix.rotate(new Vector3(0, 0, 1), angle);
	    batch.setTransformMatrix(matrix);
	    batch.begin();
	    font.draw(batch, text, 0, 0);
	    //batch.draw(digit[0], 0, -cellSize, cellSize * 150/235, cellSize);
	    batch.end();
	    matrix.setToRotation(new Vector3(0, 0, 1), 0);
	    batch.setTransformMatrix(matrix);
	}

}
