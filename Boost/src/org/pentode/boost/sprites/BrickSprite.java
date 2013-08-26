package org.pentode.boost.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BrickSprite {
	public Sprite sprite;
	public BrickSprite(Texture texture, float cellSize) {
		
		//sprite = new Sprite(texture, 0, 62, 64, 32);
		sprite = new Sprite(texture, 0, 0, 433, 215);
		sprite.setSize(cellSize * 2, cellSize);
		sprite.setOrigin(cellSize, cellSize / 2);
	}
}
