//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Pawn object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class Pawn extends Piece
{	
	//(up 1, up 2, right 1 up 1, left 1 up 1) Going to probably have logic later on 
	//in the board class for when Pawns are allowed to move diagonal 
	//(The piece objects don't know anything but who they are and what moves are "possible")
	private static final int[][] MOVEMENTS = {{0,1},{0,2},{1,1},{-1,1}};
	private boolean justMoved2 = false;
	
	public Pawn(char letter, int sRow, int number, String c)					
	{	
		name = "pawn_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 5;
		justMoved2 = false;
		if(c.contains("dark"))
			pieceID = 11;
		setMovements(MOVEMENTS);
	}
	
	public boolean getJustMoved2() {
		return justMoved2;
	}
	
	public void setJustMoved2(boolean moved) {
		justMoved2 = moved;
	}
}
