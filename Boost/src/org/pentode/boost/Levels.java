package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;

public class Levels {
	Level level1 = new Level();
	Level level2 = new Level();
	Level level3 = new Level();
	Level level4 = new Level();
	
	public Levels() {
		level1.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {1, 15, 15, 15}};
		level1.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 2, 0}};
		level1.ball = new Vector2(2f, 3.3f);
		level1.detector = new Detector(43, 30, 2);
		
		level2.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {15, 1, 15, 10}};
		level2.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 3, 0}};
		level2.ball = new Vector2(2f, 1f);
		level2.detector = new Detector(43, 30, 2);
	}

}
