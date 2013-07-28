package org.pentode.boost;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class RotatableLabel extends Label {
	BitmapFontCache cach;

	public RotatableLabel(CharSequence text, LabelStyle style) {
		super(text, style);
		cach = new BitmapFontCache(style.font, style.font.usesIntegerPositions());
	}
	
	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		LabelStyle style = this.getStyle();
		validate();
		Color color = getColor();
		if (style.background != null) {
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
		cach.setColor(style.fontColor == null ? color : Color.tmp.set(color).mul(style.fontColor));
		cach.setPosition(getX(), getY());
		cach.draw(batch, color.a * parentAlpha);
	}
}
