//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Queen object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class Queen extends Piece
{
	private static final int[][] MOVEMENTS = new int[56][2]; //Queens can move in a range of 7 in any direction vertically, horizontally, and diagonally 
															 //There is no one place on the board that the queen can use all 56 "possible" moves
	public Queen(char letter, int sRow, int number, String c)									
	{																	
		name = "queen_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 3;
		if(c.contains("dark"))
			pieceID = 9;
		generateMovements();
		setMovements(MOVEMENTS);
	}
	
	private void generateMovements()
	{
		int i2 = 0;
		for(int i = 0; i < 7; i++)
		{
			MOVEMENTS[i][1] = i+1;
			MOVEMENTS[7 + i][1] = -i-1;
			MOVEMENTS[14 + i][0] = i+1;
			MOVEMENTS[21 + i][0] = -i-1;
			i2 = -1-i;
			for(int j = 0; j < 2; j++)
			{
				MOVEMENTS[28 + i][j] = i+1;
				MOVEMENTS[35 + i][j] = -i-1;
				MOVEMENTS[42  + i][j] = -i2;
				MOVEMENTS[49 + i][j] = i2;
				i2 *= -1;
			}
		}
	}
}
