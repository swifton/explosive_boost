package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;

public class Levels {
	Level level1 = new Level();
	Level level2 = new Level();
	Level level3 = new Level();
	Level level4 = new Level();
	Level level5 = new Level();
	Level level6 = new Level();
	Level level7 = new Level();
	Level level8 = new Level();
	Level level9 = new Level();
	Level level10 = new Level();
	Level level11 = new Level();
	Level level12 = new Level();
	Level level13 = new Level();

	public Levels() {
		level1.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 10, 43, 30}};
		level1.bombs = new int[][]{{3, 3, 5, 0}};
		level1.ball = new Vector2(5f, 1f);
		level1.detector = new Detector(43, 1, 0);
		
		level2.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 10}};
		level2.bombs = new int[][]{{24, 3, 2, 0}};
		level2.ball = new Vector2(5f, 4f);
		level2.detector = new Detector(43, 30, 2);
		
		level3.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {1, 15, 15, 15}};
		level3.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 2, 0}};
		level3.ball = new Vector2(2f, 3.3f);
		level3.detector = new Detector(43, 30, 2);
		
		level4.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {15, 1, 15, 10}};
		level4.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 3, 0}};
		level4.ball = new Vector2(2f, 1f);
		level4.detector = new Detector(43, 30, 2);
		
		level5.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 7}, {43, 15, 43, 30}, {25, 7, 43, 7}, {0, 7, 15, 7}, {-1, 10, 6, 10, -1}};
		level5.bombs = new int[][]{{3, 3, 1, 0}};
		level5.ball = new Vector2(0.4f, 2.5f);
		level5.detector = new Detector(43, 15, 2);
		
		level6.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 30, 30}, {43, 1, 43, 30}, {33, 2, 41, 2, 13}, {35, 8, 48, 8, 1}};
		level6.bombs = new int[][]{{40, 20, 5, 0}};
		level6.ball = new Vector2(2f, 0.4f);
		level6.detector = new Detector(30, 30, 1);
		
		level7.walls = new int[][]{{1, 1, 43, 1}, {1, 30, 43, 30}, {43, 10, 43, 30}, {15, 10, 43, 10}, {35, 14, 43, 14}};
		level7.bombs = new int[][]{{40, 3, 2, 0}, {37, 3, 2, 30}};
		level7.ball = new Vector2(7.3f, 2.2f);
		level7.detector = new Detector(43, 10, 2);
		
		level8.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 30, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}, {1, 5, 15, 5}};
		level8.bombs = new int[][]{{35, 20, 2, 50}};
		level8.ball = new Vector2(2f, 0.4f);
		level8.detector = new Detector(43, 1, 3);
	}

}
