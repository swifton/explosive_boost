package org.pentode.boost.ui;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Windows {
	public TimeWindow timeWindow;
	public WinWindow winWindow;
	public HelpWindow helpWindow;
	
	public Windows(Stage stage, BitmapFont digits) {
		timeWindow = new TimeWindow(stage);
		winWindow = new WinWindow(stage);
		helpWindow = new HelpWindow(stage);
	}
	
	public void setVisible(boolean visible) {
		timeWindow.window.setVisible(visible);
		helpWindow.setVisible(visible);
		winWindow.window.setVisible(visible);
	}
}
