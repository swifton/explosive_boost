package org.pentode.boost;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Windows {
	TimeWindow timeWindow;
	WinWindow winWindow;
	HelpWindow helpWindow;
	
	public Windows(Stage stage) {
		timeWindow = new TimeWindow(stage);
		winWindow = new WinWindow(stage);
		helpWindow = new HelpWindow(stage);
	}
	
	public void setVisible(boolean visible) {
		timeWindow.window.setVisible(visible);
		helpWindow.window.setVisible(visible);
		winWindow.window.setVisible(visible);
	}
}
