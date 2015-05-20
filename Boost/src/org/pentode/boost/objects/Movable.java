package org.pentode.boost.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;

public class Movable extends SolidBody {
	public float startX, startY;

	DragAndDrop dragAndDrop;
	Image sourceImage;
	static float BTWORLD;
	static float cellSize;
	boolean touched;
	public boolean play = false;
	public boolean active = false;
	int toDropX;
	int toDropY;
	int currentCX;
	int currentCY;
	Stage stagee;
	boolean droppable = true;
	QueryCallback AABBCallback;
	World world;
	
	public Movable(int x, int y, float BTW, Stage stage, World wrld) {
		BTWORLD = BTW;
		cellSize = BTW / 5;
		startX = x * 0.2f - 0.1f;
		startY = y * 0.2f - 0.1f;
		currentCX = x;
		currentCY = y;
		world = wrld;
		createDragDrop(startX, startY, stage);
	}
	
	private void createDragDrop(float x, float y, Stage stage) {
	    dragAndDrop = new DragAndDrop();
	        
	    final Skin skinn = new Skin();
		skinn.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
		skinn.add("badlogic", new Texture("droplet.png"));

		sourceImage = new Image(skinn, "badlogic");
		sourceImage.setBounds(x * BTWORLD - cellSize * 1.5f, y * BTWORLD - cellSize * 1.5f, cellSize * 3, cellSize * 3);
		stage.addActor(sourceImage);
		
		sourceImage.addListener(new ClickListener() {
		    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			    if (play) return true;
			    touched = true;
			    return true;
		    }
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			   	if (play || !touched) return;
			   	active = true;
			   	tu();
			    touched = false;
			}
		});
        
		dragAndDrop.addSource(new Source(sourceImage) {
			public Payload dragStart (InputEvent event, float x, float y, int pointer) {
				if (play) return null;
				toDropX = currentCX;
				toDropY = currentCY;
				touched = false;
				Payload payload = new Payload();
				payload.setObject("crap");
				payload.setDragActor(new Label("", skinn));
				return payload;
			}
			
			public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Target target) {
				if (play) return;
				active = true;
				currentCX = toDropX;
				currentCY = toDropY;
				startX = toDropX * 0.2f - 0.1f;
				startY = toDropY * 0.2f - 0.1f;
				align();
			}
		});
	}
	
	public void align() {

	}

	public void tu() {

	}
	
	public void goDirection(int direction) {
		float endX = startX;
		float endY = startY;
		
		while (endX > 0 && endY > 0 && endX < 8.5f && endY < 6) {
			if (direction == 0) endY += 0.2f;
			if (direction == 1) endX += 0.2f;
			if (direction == 2) endY -= 0.2f;
			if (direction == 3) endX -= 0.2f;
			
			if (checkDrop(endX, endY)) {
				startX = endX;
				startY = endY;
				return;
			}
		}
	}
	
	public boolean checkDrop(float x, float y) {
		droppable = true;
		world.QueryAABB(AABBCallback, x - 0.15f, y - 0.15f, x + 0.15f, y + 0.15f);
		return droppable;
	}
	
	public int[] drag() {
		if (dragAndDrop.isDragging()) {
			int x = (int) (Math.floor(dragAndDrop.getDragActor().getX()/cellSize - 1.5f));
			int y = (int) (Math.floor(dragAndDrop.getDragActor().getY()/cellSize - 0.5f));
			//int rx = (int) (dragAndDrop.getDragActor().getX() - 1.5f * cellSize);
			//int ry = (int) (dragAndDrop.getDragActor().getY() - 0.5f * cellSize);
			int xx = x + 2;
			int yy = y + 2;
			x = (int) (x * cellSize);
			y = (int) (y * cellSize);
			int bool = 0;

			droppable = true;
			//System.out.println(world);
			//System.out.println(AABBCallback);
			world.QueryAABB(AABBCallback, x / BTWORLD + 0.15f, y / BTWORLD + 0.15f, x / BTWORLD + 0.45f, y / BTWORLD + 0.45f);
			if (droppable) {
				toDropX = xx;
				toDropY = yy;
				bool = 1;  
			}
			return new int[] {1, x, y, bool};
		}
		return new int[] {0};
	}
	

	public void disableUI() {
		sourceImage.setVisible(false);
	}
	
	public void alignImage() {sourceImage.setBounds(startX * BTWORLD - cellSize * 1.5f, startY * BTWORLD - cellSize * 1.5f, cellSize * 3, cellSize * 3);}

}
