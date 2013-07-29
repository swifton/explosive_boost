package org.pentode.boost;

import com.badlogic.gdx.math.Vector2;

public class Level {
	int [][] walls;
	int [][] bombs;
	int [][] bricks;
	int [][] brickWall; // see details in loadLevel
	Vector2 ball;
	DetCoord detector;
}
