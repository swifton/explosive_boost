package org.pentode.boost.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LevelSelect {
	PagedScrollPane levels;
	public int levelNum = -1;
	     
	private Skin skin;
	public Table container;

	public LevelSelect(Stage stage) {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		skin.add("top", skin.newDrawable("default-round", Color.RED), Drawable.class);
		skin.add("star-filled", skin.newDrawable("default-round", Color.YELLOW), Drawable.class);
		skin.add("star-unfilled", skin.newDrawable("default-round", Color.GRAY), Drawable.class);
        
		Gdx.input.setInputProcessor(stage);

		container = new Table();
		stage.addActor(container);
		container.setFillParent(true);

		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		for (int l = 0; l < 10; l++) {
			Table levels = new Table().pad(50);
			levels.defaults().pad(20, 40, 20, 40);
			for (int y = 0; y < 3; y++) {
				levels.row();
				for (int x = 0; x < 5; x++) {
					levels.add(getLevelButton(c++)).expand().fill();
				}
			}
			scroll.addPage(levels);
		}
		container.add(scroll).expand().fill();
	}
	

    public Button getLevelButton(int level) {
        Button button = new Button(skin);
        ButtonStyle style = button.getStyle();
        style.up =  style.down = null;
            
        // Create the label to show the level number
        Label label = new Label(Integer.toString(level), skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);      
            
        // Stack the image and the label at the top of our button
        button.stack(new Image(skin.getDrawable("top")), label).expand().fill();

        // Randomize the number of stars earned for demonstration purposes
        //int stars = MathUtils.random(-1, +3);
        ///Table starTable = new Table();
        //starTable.defaults().pad(5);
        //if (stars >= 0) {
        //    for (int star = 0; star < 3; star++) {
        //        if (stars > star) {
        //            starTable.add(new Image(skin.getDrawable("star-filled"))).width(20).height(20);
        //        } else {
        //            starTable.add(new Image(skin.getDrawable("star-unfilled"))).width(20).height(20);
        //        }
        //    }          
        //}
            
        button.row();
        //button.add(starTable).height(30);
            
        button.setName(Integer.toString(level));
        button.addListener(levelClickListener);    
        return button;
    }
    
    public ClickListener levelClickListener = new ClickListener() {
    	@Override
    	public void clicked (InputEvent event, float x, float y) {
    		//System.out.println("Click: " + event.getListenerActor().getName());
    		container.setVisible(false);
    		levelNum = Integer.parseInt(event.getListenerActor().getName());
    	}	
    };
}