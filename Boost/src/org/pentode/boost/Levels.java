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
	Level level14 = new Level();
	Level level15 = new Level();
	Level [] list = new Level[]{level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12, level13, level14, level15};
	

	public Levels() {
		level1.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 10, 43, 30}};
		level1.bombs = new int[][]{{3, 3, 5, 0}};
		level1.ball = new Vector2(5f, 1f);
		level1.detector = new DetCoord(43, 1, 0);
		
		level2.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 15, 30}, {28, 30, 43, 30}, {43, 0, 43, 30}};
		level2.bombs = new int[][]{{22, 3, 0, 80}};
		level2.ball = new Vector2(4.3f, 5f);
		level2.detector = new DetCoord(15, 30, 1);		
		
		level3.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}};
		level3.bombs = new int[][]{{24, 3, 2, 0}};
		level3.ball = new Vector2(5f, 2f);
		level3.detector = new DetCoord(43, 30, 2);
		
		level4.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 7}, {43, 15, 43, 30}, {25, 7, 43, 7}, {0, 7, 15, 7}, {-1, 10, 6, 10, 5}};
		level4.bombs = new int[][]{{3, 3, 1, 0}};
		level4.ball = new Vector2(0.4f, 2.4f);
		level4.detector = new DetCoord(43, 15, 2);
		
		level5.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 30, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}, {5, 5, 10, 5}, {1, 4, 5, 4}};
		level5.bombs = new int[][]{{35, 20, 2, 50}};
		level5.ball = new Vector2(1f, 0.4f);
		level5.detector = new DetCoord(43, 1, 3);
		
		level6.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {1, 15, 15, 15}};
		level6.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 2, 0}};
		level6.ball = new Vector2(2f, 3.3f);
		level6.detector = new DetCoord(43, 30, 2);
		
		level7.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {15, 1, 15, 10}};
		level7.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 3, 0}};
		level7.ball = new Vector2(2f, 1f);
		level7.detector = new DetCoord(43, 30, 2);
		
		level8.walls = new int[][]{{1, 1, 43, 1}, {1, 30, 43, 30}, {43, 10, 43, 30}, {15, 10, 43, 10}, {35, 14, 43, 14}};
		level8.bombs = new int[][]{{40, 3, 2, 0}, {37, 3, 2, 30}};
		level8.ball = new Vector2(7.3f, 2.2f);
		level8.detector = new DetCoord(43, 10, 2);
		
		level9.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {1, 10, 10, 10}, {14, 10, 33, 10}, {10, 7, 14, 7}, {10, 20, 43, 20}, {36, 3, 43, 3, 1}};
		level9.bombs = new int[][]{{36, 22, 1, 0}, {40, 22, 2, 0}};
		level9.ball = new Vector2(2.2f, 1f);
		level9.detector = new DetCoord(43, 20, 0);
		
		level10.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 20}, {30, 20, 43, 20}, {0, 10, 15, 10}, {10, 12, 16, 12, 1}};
		level10.bombs = new int[][]{{8, 3, 1, 0}, {33, 15, 0, 50}};
		level10.brickWall = new int [][] {{30, 29, 31, 29, 0, -1, 9}, {30, 2, 31, 2, 0, 1, 15}};
		level10.ball = new Vector2(1.2f, 2.2f);
		level10.detector = new DetCoord(43, 30, 2);
		
		level11.walls = new int[][] {{1, 1, 1, 30}, {1, 1, 35, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}};
		level11.bombs = new int[][] {{10, 10, 5, 0}};
		level11.bricks = new int[][] {{25, 2, 26, 2}};
		level11.ball = new Vector2(0.4f, 0.4f);
		level11.detector = new DetCoord(43, 2, 3);
		
		level12.walls = new int[][]{{1, 30, 43, 30}, {1, 1, 1, 30}, {1, 1, 15, 1}, {28, 1, 43, 1}, {43, 1, 43, 30}, {28, 15, 28, 21}, {31, 15, 31, 21}};
		level12.bombs = new int[][]{{29, 23, 0, 70}};
		level12.brickWall = new int [][] {{29, 2, 30, 2, 0, 1, 15}};
		level12.ball = new Vector2(5.8f, 3.4f);
		level12.detector = new DetCoord(15, 1, 1);	
		
		level13.walls = new int[][]{{1, 1, 1, 30}, {1, 1, 43, 1}, {10, 30, 43, 30}, {43, 0, 43, 30}, {35, 3, 43, 3, 1}};
		level13.bombs = new int[][]{{5, 26, 2, 70}, {5, 23, 0, 20}};
		level13.brickWall = new int [][] {{3, 20, 4, 20, 2, 0, 17}};
		level13.ball = new Vector2(4f, 0.4f);
		level13.detector = new DetCoord(1, 30, 1);
		
		level14.walls = new int[][]{{1, 1, 1, 10}, {1, 20, 1, 30}, {1, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}, {35, 25, 43, 25}};
		level14.bombs = new int[][]{{3, 3, 1, 0}, {6, 3, 3, 0}};
		level14.brickWall = new int [][] {{9, 2, 9, 3, 0, 2, 14}, {12, 2, 12, 3, 0, 2, 14}, {15, 2, 15, 3, 0, 2, 14}, {21, 2, 21, 3, 0, 2, 14}, {24, 2, 24, 3, 0, 2, 14}, {27, 2, 27, 3, 0, 2, 14}};
		level14.ball = new Vector2(7f, 5.4f);
		level14.detector = new DetCoord(1, 20, 2);
		
		level15.walls = new int[][]{{1, 1, 1, 30}, {10, 1, 43, 1}, {1, 30, 43, 30}, {43, 1, 43, 30}, {10, 15, 10, 30}, {10, 1, 10, 5}, {6, 5, 10, 5}, {15, 5, 43, 5}};
		level15.bombs = new int[][]{{3, 3, 5, 0}, {3, 6, 3, 0}, {3, 9, 2, 0}, {3, 15, 1, 0}};
		level15.brickWall = new int [][] {{9, 6, 10, 6, 0, 1, 9}, {19, 6, 20, 6, 0, 1, 16}, {21, 6, 22, 6, 0, 1, 7}, {33, 6, 34, 6, 0, 1, 16}};
		level15.ball = new Vector2(5.5f, 2f);
		level15.detector = new DetCoord(1, 1, 1);
	}

}
