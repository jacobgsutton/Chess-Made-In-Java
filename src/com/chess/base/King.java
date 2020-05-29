//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: King object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class King extends Piece
{	
	//Last two transformations are for castling only
	private static final int[][] MOVEMENTS = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,1},{1,-1},{-1,-1},{2,0},{-2,0}}; //(up 1, down 1, right 1, left 1, right 1 up 1, left 1 up 1, right 1 down 1, left 1 down 1, right 2, left 2)
	public King(char letter, int sRow, int number, String c)
	{
		name = "king_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 4;
		if(c.contains("dark"))
			pieceID = 10;
		setMovements(MOVEMENTS);
	}
}
