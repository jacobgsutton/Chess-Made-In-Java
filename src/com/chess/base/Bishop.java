//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Bishop object, which is a child of Piece and specifies which movements it can make
package com.chess.base;

public class Bishop extends Piece
{
	private final int[][] MOVEMENTS = new int[26][2]; //Bishops can move on diagonals in any direction in a range of either 6 or 7 depending on the Bishop type (dark square or light square bishop)
	public Bishop(char letter, int sRow, int number, String c)
	{
		name = "bishop_" + letter + sRow + "_" + number;
		color = c;
		pieceID = 2;
		if(c.contains("dark"))
			pieceID = 8;
		if((number == 1 && sRow == 1) || (number == 2 && sRow == 8))
		{
			generateMovements(0);
			name = "(D)" + name;
		}
		else
		{
			generateMovements(1);
			name = "(L)" + name;
		}
		setMovements(MOVEMENTS);
	}
	
	// Movements are weird with bishops because light square 
	//bishops can move in a range of 7 spaces left-up and right-down 
	//(range of 6 spaces on the other diagonals) while dark square bishops
	// can move in a range of 7 spaces right-up and left-down (range of 6 spaces 
	//on the other diagonals). Since there is technically 26 different moves bishops 
	//can take that only vary slightly I'm using a algorithm to place in the matrix what moves a bishop can make.
	private void generateMovements(int bishopType) //a 0 is a dark square bishop, a 1 is a light square bishop 
	{
		int n = 1, n2 = 1, m = 6, m2 = 6, s = 0;
		if(bishopType == 0)
			m += 1;
		else
		{
			m2 += 1;
			s = 1;
		}
		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				if(i < m)
				{
					MOVEMENTS[i][j] = n;
					MOVEMENTS[7 - s + i][j] = -n;
				}
				if(i < m2)
				{
					MOVEMENTS[14 - s*2 + i][j] = -n2;
					MOVEMENTS[20 - s + i][j] = n2;
				}
				n2 *= -1;
			}
			n++;
			n2 = n;
		}
	}
}