//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Rook object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class Rook extends Piece
{
	private static final int[][] MOVEMENTS = new int[28][2]; //Rooks can move in a range of 7 in any direction vertically and horizontally 
	public Rook(char letter, int sRow, int number, String c)									
	{																	
		name = "rook_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 0;
		if(c.contains("dark"))
			pieceID = 6;
		generateMovements();
		setMovements(MOVEMENTS);
	}
	
	private void generateMovements()
	{
		for(int i = 0; i < 7; i++)
		{
			MOVEMENTS[i][1] = i+1;
			MOVEMENTS[7 + i][1] = -i-1;
			MOVEMENTS[14 + i][0] = i+1;
			MOVEMENTS[21 + i][0] = -i-1;
		}
	}
}
