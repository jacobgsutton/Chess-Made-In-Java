//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Knight object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class Knight extends Piece
{
	private static final int[][] MOVEMENTS = {{1,2},{-1,2},{1,-2},{-1,-2},{2,1},{2,-1},{-2,1},{-2,-1}}; //(right 1 up 2, left 1 up 2, right 1 down 2, left 1 down 2, right 2 up 1, right 2 down 1, left 2 up 1, left 2 down 1)
	public Knight(char letter, int sRow, int number, String c)
	{
		name = "knight_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 1;
		if(c.contains("dark"))
			pieceID = 7;
		setMovements(MOVEMENTS);
	}
}