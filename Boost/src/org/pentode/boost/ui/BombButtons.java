package org.pentode.boost.ui;

import org.pentode.boost.objects.Bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class BombButtons {
	TextButton up;
	TextButton down;
	TextButton left;
	TextButton right;
	TextButton [] buttons = new TextButton[]{up, down, left, right};
	float cellSize;
	public Bomb bomb;
	
	public BombButtons(Stage stage, float cellS) {
		cellSize = cellS;
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		up = new TextButton("^", skin);
		down = new TextButton("v", skin);
		left = new TextButton("<", skin);
		right = new TextButton(">", skin);
		
		stage.addActor(up);
		stage.addActor(down);
		stage.addActor(left);
		stage.addActor(right);
		
		up.setVisible(false);
		down.setVisible(false);
		left.setVisible(false);
		right.setVisible(false);
		
		//for (TextButton button:buttons) {
			//stage.addActor(button);
			///button.setVisible(false);
		//}
		
		up.setWidth(cellSize * 3);
		up.setHeight(cellSize * 2);
		down.setWidth(cellSize * 3);
		down.setHeight(cellSize * 2);
		left.setWidth(cellSize * 2);
		left.setHeight(cellSize * 3);
		right.setWidth(cellSize * 2);
		right.setHeight(cellSize * 3);
		
		up.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	click(0);
		    	return true;
		    }
		});
		
		down.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	click(2);
		    	return true;
		    }
		});
		
		left.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	click(3);
		    	return true;
		    }
		});
		
		right.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		    	click(1);
		    	return true;
		    }
		});
	}
	
	private void click(int direction) {
		bomb.goDirection(direction);
    	bomb.align();
    	move();
	}
	
	public void move() {
		int x = bomb.getCoordX();
		int y = bomb.getCoordY();
		up.setX((x - 2) * cellSize);
		up.setY((y + 1) * cellSize);
		down.setX((x - 2) * cellSize);
		down.setY((y - 4) * cellSize);
		left.setX((x - 4) * cellSize);
		left.setY((y - 2) * cellSize);
		right.setX((x + 1) * cellSize);
		right.setY((y - 2) * cellSize);
	}
	
	public void setVisible(boolean visible) {
		up.setVisible(visible);
		down.setVisible(visible);
		left.setVisible(visible);
		right.setVisible(visible);
		//for (Button button:buttons) button.setVisible(visible);
	}
}
