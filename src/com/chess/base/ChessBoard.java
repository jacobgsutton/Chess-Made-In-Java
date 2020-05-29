//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Main Chess "logic" class, this is where the actual game is "played," rules, game status, game actions, etc. are all done through this class it is 
//the entire chess board object and all of its properties and rules associated with interacting with it, with main functions such as holding Piece objects 
//in specific positions, moving pieces, checking for spots that are in danger, and checking legality of moves

package com.chess.base;

import java.util.ArrayList;

import com.chess.UI.DeveloperModePane;

public class ChessBoard {
	// 2D board that can hold Pieces (8 x 8)
	private Piece[][] board;
	// 3D board that matches board but doesn't hold Pieces and instead holds ints,
	// the amount in each spot is the amount of Pieces putting that location in
	// danger (it is 3D because each "spot" on the board is actually 2 elements in
	// danger board, one for light team danger and the other dark team danger), (8 x 8 x 2)
	private int[][][] dangerBoard;
	//Holds list of Pieces putting certain spots in dangerBoard in danger
	private ArrayList<ArrayList<String>> spotDangerList;
	// Holds current light Pieces
	private ArrayList<Piece> lightPieces;
	// Holds current dark Pieces
	private ArrayList<Piece> darkPieces;
	// Holds Pieces captured by light
	private ArrayList<Piece> lightsCapturedPieces;
	// Holds Pieces captured by dark
	private ArrayList<Piece> darksCapturedPieces;
	//Holds pieces that light pawns can be promoted to, 8 of each kind
	private ArrayList<ArrayList<Piece>> lightPromotionPieces;
	//Holds pieces that dark pawns can be promoted to, 8 of each kind
	private ArrayList<ArrayList<Piece>> darkPromotionPieces;
	// Holds current status of kings being in check or not
	private boolean lightKingInCheck, darkKingInCheck;
	// Holds boolean for if game is from light perspective i.e true, set to false if
	// it is darks perspective
	private boolean lightPerspective;
	// Holds current turn (true for light false for dark)
	private boolean lightsTurn;
	//Holds status of light pawn being at the last row respective to the player
	private boolean lightPawnAtLastRank;
	//Holds status of dark pawn being at the last row respective to the player
	private boolean darkPawnAtLastRank;
	//Stores name of king based on perspective (used for check mate and still in check methods)
	private String lightKingName = "king_E1_1", darkKingName = "king_E8_1";

	public ChessBoard(String player1Color) { // Constructor of the ChessBoard and main game methods (player1Color is team color from "set perspective")	
		StatStorage.startTurnClock();
		System.out.println("Chess Board Created from " + player1Color + " perspective");
		DeveloperModePane.printText("Chess Board Created from " + player1Color + " perspective");
		board = new Piece[8][8];
		dangerBoard = new int[8][8][2];
		lightKingInCheck = false;
		darkKingInCheck = false;
		lightPerspective = true;
		lightsTurn = true;
		if (player1Color.equals("dark"))
			lightPerspective = false;
		if (!lightPerspective) {
			lightKingName = "king_D8_1";
			darkKingName = "king_D1_1";
		}
		lightsCapturedPieces = new ArrayList<Piece>();
		darksCapturedPieces = new ArrayList<Piece>();
		spotDangerList = new ArrayList<ArrayList<String>>(64);
		lightPromotionPieces = new ArrayList<ArrayList<Piece>>(4);
		darkPromotionPieces = new ArrayList<ArrayList<Piece>>(4);
		for(int i = 0; i < 64; i++)
			spotDangerList.add(new ArrayList<String>());
		for(int i = 0; i < 4; i++) {
			lightPromotionPieces.add(new ArrayList<Piece>());
			darkPromotionPieces.add(new ArrayList<Piece>());
		}
		buildPieces(player1Color);
		placePiecesAtStartingPostions();
		updateDangerBoard();
	}

	private void placePiecesAtStartingPostions() {// Places all created Pieces at standard starting locations on board.
		for (int i = 0; i < 16; i++) {
			board[convertToIntXOrY(lightPieces.get(i).getStarterColumnAndRowIndex(), 'y')][convertToIntXOrY(
					lightPieces.get(i).getStarterColumnAndRowIndex(), 'x')] = lightPieces.get(i);
			board[convertToIntXOrY(darkPieces.get(i).getStarterColumnAndRowIndex(), 'y')][convertToIntXOrY(
					darkPieces.get(i).getStarterColumnAndRowIndex(), 'x')] = darkPieces.get(i);
		}
	}

	// Fills lightPieces ArrayList and darkPieces ArrayList (and the promotion pieces ArrayLists) with all Piece
	// objects needed to play standard chess (These list do not change
	// throughout the instance and there elements are just merely used
	// to be copied for other methods and list)
	private void buildPieces(String player1Color) {

		// All this and the proceeding if statement are for	if board is being set up for dark perspective
		int lightDefault0 = 1, darkDefault0 = 8, lightDefault1 = 2, darkDefault1 = 7, letterIncDecDefault = 0; 
		if (!lightPerspective) {
			lightDefault0 = 8;
			darkDefault0 = 1;
			lightDefault1 = 7;
			darkDefault1 = 2;
			letterIncDecDefault = 1;
		}
		char letter = 'A';
		lightPieces = new ArrayList<Piece>(16);
		darkPieces = new ArrayList<Piece>(16);
		for (int i = 0; i < 16; i++) {
			lightPieces.add(null);
			darkPieces.add(null);
		}
		for (int i = 0; i < 8; i++) {
			letter = (char) (letter + i);
			lightPieces.set(i, new Pawn(letter, lightDefault1, (i + 1), "light"));
			darkPieces.set(i, new Pawn(letter, darkDefault1, (i + 1), "dark"));
			switch (i) {
			case 0:
				lightPieces.set(8, new Rook(letter, lightDefault0, 1, "light"));
				darkPieces.set(8, new Rook(letter, darkDefault0, 1, "dark"));
				break;
			case 1:
				lightPieces.set(9, new Knight(letter, lightDefault0, 1, "light"));
				darkPieces.set(9, new Knight(letter, darkDefault0, 1, "dark"));
				break;
			case 2:
				lightPieces.set(10, new Bishop(letter, lightDefault0, 1, "light"));
				darkPieces.set(10, new Bishop(letter, darkDefault0, 1, "dark"));
				break;
			case 3:
				lightPieces.set(11, new Queen((char) (letter + letterIncDecDefault), lightDefault0, 1, "light"));
				darkPieces.set(11, new Queen((char) (letter + letterIncDecDefault), darkDefault0, 1, "dark"));
				break;
			case 4:
				lightPieces.set(12, new King((char) (letter - letterIncDecDefault), lightDefault0, 1, "light"));
				darkPieces.set(12, new King((char) (letter - letterIncDecDefault), darkDefault0, 1, "dark"));
				break;
			case 5:
				lightPieces.set(13, new Bishop(letter, lightDefault0, 2, "light"));
				darkPieces.set(13, new Bishop(letter, darkDefault0, 2, "dark"));
				break;
			case 6:
				lightPieces.set(14, new Knight(letter, lightDefault0, 2, "light"));
				darkPieces.set(14, new Knight(letter, darkDefault0, 2, "dark"));
				break;
			case 7:
				lightPieces.set(15, new Rook(letter, lightDefault0, 2, "light"));
				darkPieces.set(15, new Rook(letter, darkDefault0, 2, "dark"));
				break;
			default:
				lightPieces.set(15, null);
				darkPieces.set(15, null);
			}
			letter = 'A';
		}
		
		//lightDefault0 and darkDefualt0 are switched because the promotion pieces are created at the opposite end from the respective team.
		//Numbers are added to respective piece "piece number" attribute to account for the default created pieces
		//A is used as the default column for every piece and will be changed once piece actually enters the game
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				switch(i) {
					case 0: lightPromotionPieces.get(i).add(new Queen('A', darkDefault0, j + 2, "light"));
							darkPromotionPieces.get(i).add(new Queen('A', lightDefault0, j + 2, "dark"));
							break;
					case 1: lightPromotionPieces.get(i).add(new Knight('A', darkDefault0, j + 3, "light"));
					 		darkPromotionPieces.get(i).add(new Knight('A', lightDefault0, j + 3, "dark"));
					 		break;
					case 2: lightPromotionPieces.get(i).add(new Rook('A', darkDefault0, j + 3, "light"));
					 		darkPromotionPieces.get(i).add(new Rook('A', lightDefault0, j + 3, "dark"));
					 		break;
					case 3: lightPromotionPieces.get(i).add(new Bishop('A', darkDefault0, j + 3, "light"));
					 		darkPromotionPieces.get(i).add(new Bishop('A', lightDefault0, j + 3, "dark"));
					 		break;
					default:
						break;
				}
			}
		}
	}

	// Updates dangerBoard by placing in each location the amount of Pieces that is
	// putting that specific spot in danger (negatives for dark team danger and
	// positives for light team danger)
	public void updateDangerBoard() {
		String xy = "";
		int set = 0, exclude = 0;
		for (int i = 0; i < 8; i++) // Resets dangerBoard so it holds only 0s, resets spotDangerList as well
			for (int j = 0; j < 8; j++) {
				dangerBoard[i][j][0] = 0;
				dangerBoard[i][j][1] = 0;
				spotDangerList.get((i * 8) + j).clear();
			}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					if (board[i][j].getName().contains("pawn"))
						set = 2;
					else
						set = 0;
					if(board[i][j].getName().contains("king"))
						exclude = 2;
					else
						exclude = 0;
					for (; set < board[i][j].getMovements().length - exclude; set++) {
						StatStorage.increment(5);
						if (!moveBlocked(board[i][j].getName(),
								xy = convertToGoToXY(board[i][j].getName(), board[i][j].getMovements()[set]), false)
								&& specialCaseChecker(board[i][j].getName(), xy, board[i][j].getMovements()[set], true) 
								&& !spotDangerList.get((convertToIntXOrY(xy, 'y') * 8) + convertToIntXOrY(xy, 'x')).contains(board[i][j].getName())) {
							if (board[i][j].getColor().equals("dark"))
								dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][1]--;
							else
								dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][0]++;
							spotDangerList.get((convertToIntXOrY(xy, 'y') * 8) + convertToIntXOrY(xy, 'x')).add(board[i][j].getName());
						}
					}
				}
			}
		}
	}

	public int afterTurnUpdate() { //Updates danger board and then proceeds to check for check on one of the kings
								   //or check mate on one of the kings
		updateDangerBoard();
		if (lightsTurn) {
			StatStorage.endTurnClockFor(0);
			lightsTurn = false;
		}
		else {
			lightsTurn = true;
			StatStorage.endTurnClockFor(1);
		}
		if (inCheckOrDanger(lightKingName, getPieceLocation(lightKingName))) {
			lightKingInCheck = true;
			StatStorage.increment(3);
			System.out.println("Light King In Check");
			DeveloperModePane.printText("Light King In Check");
		}
		else
			lightKingInCheck = false;
		if (inCheckOrDanger(darkKingName, getPieceLocation(darkKingName))) {
			darkKingInCheck = true;
			StatStorage.increment(4);
			System.out.println("Dark King In Check");
			DeveloperModePane.printText("Dark King In Check");
		}
		else
			darkKingInCheck = false;
		StatStorage.startTurnClock();
		if (checkMate())
			return 0;
		return 1;
	} 
	
	// Converts an array of two ints (x,y)
	// transformation and Piece "name" (needed to
	// find current location) into "goToXY" cord
	// once transformation is added to piece
	// location
	private String convertToGoToXY(String name, int[] transformation) 
	{
		String xy = "";
		if((getPiece(name).getColor().contains("light") && lightPerspective) || (getPiece(name).getColor().contains("dark") && !lightPerspective)) {
			xy += convertToIntXOrY(getPieceLocation(name), 'x') + transformation[0];
			xy += convertToIntXOrY(getPieceLocation(name), 'y') - transformation[1];
		} 
		else {
			xy += convertToIntXOrY(getPieceLocation(name), 'x') - transformation[0];
			xy += convertToIntXOrY(getPieceLocation(name), 'y') + transformation[1];
		}
		return xy;
	}
	 
	// Adds Piece to a "captured" ArrayList based on Piece color (Does not
	// automatically remove Piece from where it is being passed from)									 
	public void addCapture(Piece piece) { 
		if (piece.getColor().equals("light"))
			darksCapturedPieces.add(piece);
		else
			lightsCapturedPieces.add(piece);
	}
	
	// Returns and removes Piece with "name" from a capturedPieces ArrayList
	// determined by its team color which is checked by the first for loops
	public Piece getCaptured(String name) 
	{
		for (Piece piece : lightPieces)
			for(ArrayList<Piece> lightList : lightPromotionPieces)
				for(Piece promotionPiece : lightList)
					if (piece.getName().equals(name) || promotionPiece.getName().equals(name))
						for (int i = 0; i < darksCapturedPieces.size(); i++)
							if (darksCapturedPieces.get(i).getName().equals(name))
								return darksCapturedPieces.remove(i);
		for (Piece piece : darkPieces)
			for(ArrayList<Piece> darkList : darkPromotionPieces)
				for(Piece promotionPiece : darkList)
					if (piece.getName().equals(name) || promotionPiece.getName().equals(name))
						for (int i = 0; i < lightsCapturedPieces.size(); i++)
							if (lightsCapturedPieces.get(i).getName().equals(name))
								return lightsCapturedPieces.remove(i);
		return null;
	}

	public ArrayList<Piece> getCapturedList(String color) // Returns ArrayList of captured Pieces dependent on "color"
	{
		if (color.equals("light"))
			return darksCapturedPieces;
		else
			return lightsCapturedPieces;
	}
	
	//Getters for access to a specific array which holds the respective promotion piece  
	public ArrayList<Piece> getLightPromotionPieceArray(int i) {
		return lightPromotionPieces.get(i);
	}
	
	public ArrayList<Piece> getDarkPromotionPieceArray(int i) {
		return darkPromotionPieces.get(i);
	}
	
	//Boolean flag getter methods to check to see if a pawn is ready for promotion
	public boolean isLightPawnReadyForPromotion() {
		return lightPawnAtLastRank;
	}
	
	public boolean isDarkPawnReadyForPromotion() {
		return darkPawnAtLastRank;
	}
	
	//Takes in a piece and puts it at location xy in board
	public void placePieceAtLocation(Piece piece, String xy) {
		board[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')] = piece;
	}

	public Piece getAndRemovePieceAtLocation(String xy) // Returns and removes Piece at (x,y) String cord
	{
		Piece grabbed = board[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')];
		board[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')] = null;
		return grabbed;
	}

	public String getPieceLocation(String name) // Returns Piece specified by "name" (x,y) String cord and does not remove the Piece
	{	
		StatStorage.increment(6);
		String xy = "";
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (board[i][j] != null && board[i][j].getName().equals(name))
					return xy = "" + j + i;
		return xy;
	}

	public Piece getPiece(String name) // Returns Piece object that is currently on the board with "name" but does not remove it							
	{
		return board[convertToIntXOrY(getPieceLocation(name), 'y')][convertToIntXOrY(getPieceLocation(name), 'x')];
	}

	public boolean isLightsTurn() // Returns true if it is lights turn and returns false if it is darks turn
	{
		return lightsTurn;
	}
	
	//Returns copy of chess board
	public Piece[][] getChessBoard() {
		Piece[][] copy = new Piece[8][8];
		copy = board;
		return copy;
	}

	public void movePieceToLocation(String name, String goToXY, boolean actualMove) // searches for Piece based by "name" and moves it to location on board indicated by (x,y) cord
	{

		board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] = getAndRemovePieceAtLocation(
				getPieceLocation(name));
		//This a special switch that is used to "block" pawns 0,2 transformation once pawn has been moved one time
		if(actualMove && !getPiece(name).getHasMoved())
			getPiece(name).setHasMoved(true);
		//This is used in order to update pawn ready for promotion flags before turn is over
		updatePawnReadyForPromotionBooleans(); 
	}

	public boolean legalMove(String name, String goToXY) // Returns t/f if Piece specified by "name" can move to specified location "goToXY" legally from current position
	{
		boolean legalMove = false, moveNotBlocked = false, specialCase = false, kingNotInCheck = false, output = false; 
		int[][] transformation = new int[1][2];
		if (Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)) < 3) {
			transformation[0][0] = convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x');
			transformation[0][1] = -(convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y'));
		} 
		else {
			transformation[0][0] = -(convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x'));
			transformation[0][1] = convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y');
		}
		output = (legalMove = board[convertToIntXOrY(getPieceLocation(name), 'y')][convertToIntXOrY(getPieceLocation(name), 'x')]
				.legalMove(transformation)) && (moveNotBlocked = !moveBlocked(name, goToXY, true))
				&& (specialCase = specialCaseChecker(name, goToXY, transformation[0], false)) && (kingNotInCheck = !kingStillInCheck(name, goToXY));
		System.out.println("Legal Move: " + legalMove + "\nMove Not Blocked: " + moveNotBlocked + "\nMove Special Case Checker: " + specialCase + "\nKing Not In Check: " + kingNotInCheck); 
		DeveloperModePane.printText("Legal Move: " + legalMove + "\nMove Not Blocked: " + moveNotBlocked + "\nMove Special Case Checker: " + specialCase + "\nKing Not In Check: " + kingNotInCheck);
		return output;
	}

	// Helper methods for legal move checking and execution, pretty important and
	// complicated
	private boolean moveBlocked(String name, String goToXY, boolean checkCapture) // This returns true if something is in between a movement from current spot to "goToXY" i.e. "blocked" (it ignores knights and if checkCapture is false it won'tcheck if spot is blocked by allied Piece)
	{
		try { // Catches goToXY that are outside of the board returning true as in "move is blocked"	
			if (board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] == null);
		} catch (Exception e) {
			return true;
		}
		int x1 = convertToIntXOrY(getPieceLocation(name), 'x'), x2 = convertToIntXOrY(goToXY, 'x'),
				y1 = convertToIntXOrY(getPieceLocation(name), 'y'), y2 = convertToIntXOrY(goToXY, 'y'), xSign = 0,
				ySign = 0, iIncrements = Math.abs(y2 - y1), jIncrements = Math.abs(x2 - x1);
		if (!name.contains("knight")) { //need to check if knight can move based on if something is already there
			if (x2 - x1 != 0)
				xSign = (x2 - x1) / (Math.abs(x2 - x1));
			else
				jIncrements = 1;
			if (y2 - y1 != 0)
				ySign = (y2 - y1) / (Math.abs(y2 - y1));
			else
				iIncrements = 1;
			for (int i = 0, y = y1 + ySign; i < iIncrements; i++, y += ySign)
				for (int j = 0, x = x1 + xSign; j < jIncrements; j++, x += xSign) {
					StatStorage.increment(7);
					if (board[y][x] != null && ((i != iIncrements - 1 || j != jIncrements - 1) 
							|| ((i == iIncrements - 1 || j == jIncrements - 1) && checkCapture
									&& !isCapture(name, goToXY))))
						return true;
					if(iIncrements == jIncrements) {
						y += ySign;
						i++;
					}
				}
		}
		else {
			if(checkCapture && board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] != null && !isCapture(name, goToXY))
				return true;
		}
			
		return false;
	}
	
	//This is used for four special rules and if the rules are not met by the movement then it returns false(Rule 1: Pawns can't move diagonal unless their capturing,
	//Rule 2 Pawns can only perform transformation 0,2 on that pawns first move, Rule 3 pawns can not capture with a 0,1 transformation,
	//Rule 4 Kings can perform special "Castling" in very strict circumstances these circumstances are checked and if allowed this method automatically moves respective rook to its respective position  
	private boolean specialCaseChecker(String name, String goToXY, int[] transformation, boolean skipPawnCheck) 
	{	
		String lightLeftRook = "rook_A1_1", lightRightRook = "rook_H1_2", darkLeftRook = "rook_H8_2", darkRightRook = "rook_A8_1";
		Piece piece = getPiece(name);
		String location = getPieceLocation(name);
		int incDec = 1;
		int pieceStartingRow = 0;
		if(name.charAt(name.length()-3) == '1')
			pieceStartingRow = 7;
		if(piece.getColor().contains("dark"))
			incDec = -1;
		if(!lightPerspective) {
			lightLeftRook = "rook_H8_2";
			lightRightRook = "rook_A8_1";
			darkLeftRook = "rook_A1_1";
			darkRightRook = "rook_H1_2";
			incDec = -incDec;
		}
		
		String[] rightSideSpots = {(convertToIntXOrY(location, 'x') + incDec) + "" + location.charAt(1),(convertToIntXOrY(location, 'x') + (incDec*2)) + "" + location.charAt(1),(convertToIntXOrY(location, 'x') + (incDec*3)) + "" + location.charAt(1)};
		String[] leftSideSpots = {(convertToIntXOrY(location, 'x') - incDec) + "" + location.charAt(1),(convertToIntXOrY(location, 'x') - (incDec*2)) + "" + location.charAt(1),(convertToIntXOrY(location, 'x') - (incDec*3)) + "" + location.charAt(1)};

		if (!skipPawnCheck && name.substring(0, 4).equals("pawn") && ((transformation[0] != 0
				&& board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] == null) 
					|| (transformation[1] == 2 && piece.getHasMoved()) || (transformation[0] == 0 && isCapture(name, goToXY))))
			return false;
		
		//The Rest of method is special case move "Castling"
		if(name.substring(0, 4).equals("king") && (transformation[0] == 2 || transformation[0] == -2)) {
			if(convertToIntXOrY(location, 'y') == pieceStartingRow && !inCheckOrDanger(name,goToXY)) {
				if(piece.getColor().contains("light") && !lightKingInCheck) {
					if(transformation[0] == 2 && convertToIntXOrY(getPieceLocation(lightRightRook), 'y') == pieceStartingRow 
								&& !piece.getHasMoved() && !getPiece(lightRightRook).getHasMoved() && !inCheckOrDanger(name,rightSideSpots[0])) {
						movePieceToLocation(lightRightRook, rightSideSpots[0], true);
						return true;
					}
					else if(transformation[0] == -2 && convertToIntXOrY(getPieceLocation(lightLeftRook), 'y') == pieceStartingRow
							&& !piece.getHasMoved() && !getPiece(lightLeftRook).getHasMoved() && !inCheckOrDanger(name,leftSideSpots[0]) 
							&& board[convertToIntXOrY(leftSideSpots[2], 'y')][convertToIntXOrY(leftSideSpots[2], 'x')] == null) {
						movePieceToLocation(lightLeftRook, leftSideSpots[0], true);
						return true;
					}
					return false;
				}	
				else if(piece.getColor().contains("dark") && !darkKingInCheck) {
					if(transformation[0] == 2 && convertToIntXOrY(getPieceLocation(darkRightRook), 'y') == pieceStartingRow
							&& !piece.getHasMoved() && !getPiece(darkRightRook).getHasMoved() && !inCheckOrDanger(name,rightSideSpots[0]) 
							&& board[convertToIntXOrY(rightSideSpots[2], 'y')][convertToIntXOrY(rightSideSpots[2], 'x')] == null) {
						movePieceToLocation(darkRightRook, rightSideSpots[0], true);
						return true;
					}
					else if(transformation[0] == -2 && convertToIntXOrY(getPieceLocation(darkLeftRook), 'y') == pieceStartingRow 
							&& !piece.getHasMoved() && !getPiece(darkLeftRook).getHasMoved() && !inCheckOrDanger(name,leftSideSpots[0])) {
						movePieceToLocation(darkLeftRook, leftSideSpots[0], true);
						return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return true;
	}

	private boolean inCheckOrDanger(String name, String xy) // Sees if moving "name" to "xy" is dangerous by looking at dangerBoard
	{
		StatStorage.increment(11);
		if (getPiece(name).getColor().equals("light"))
			return dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][1] < 0;
		else
			return dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][0] > 0;
	}
	
	//King still in check plays out the specified move. updates danger board and then checks if king is in check, returns t/f, then undoes the the specified move no matter what
	//This function acts as a "simulated move" in order to check threats to the king in special situations.
	private boolean kingStillInCheck(String name, String goToXY) 
	{	
		String pastLocation = getPieceLocation(name);
		boolean isCapture = isCapture(name, goToXY);
		String capturedName = "";
		if(isCapture) {
			capturedName = board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')].getName();
			addCapture(getAndRemovePieceAtLocation(goToXY));
		}
		movePieceToLocation(name, goToXY, false);
		updateDangerBoard();
		if((lightsTurn && inCheckOrDanger(lightKingName, getPieceLocation(lightKingName))) 
				|| (!lightsTurn && inCheckOrDanger(darkKingName, getPieceLocation(darkKingName)))) {
			movePieceToLocation(name, pastLocation, false);
			if(isCapture)
				placePieceAtLocation(getCaptured(capturedName), goToXY);	
			updateDangerBoard();
			return true;
		}
		else {
			movePieceToLocation(name, pastLocation, false);
			if(isCapture)
				placePieceAtLocation(getCaptured(capturedName), goToXY);	
			updateDangerBoard();
			return false;
		}
	}

	public boolean checkMate() //Checks to see if one of the the kings are in check mate, similar algorithm to updateDangerBoard()
	{
		int exclude = 0;
		String xy = "";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null && ((darkKingInCheck && board[i][j].getColor().contains("dark")) 
						|| (lightKingInCheck && board[i][j].getColor().contains("light")))) {
					if(board[i][j].getName().contains("king"))
						exclude = 2;
					else 
						exclude = 0;
					for (int set = 0; set < board[i][j].getMovements().length - exclude; set++) {
						StatStorage.increment(8);
						if (!moveBlocked(board[i][j].getName(),
								xy = convertToGoToXY(board[i][j].getName(), board[i][j].getMovements()[set]), true)
								&& specialCaseChecker(board[i][j].getName(), xy, board[i][j].getMovements()[set], false) && !kingStillInCheck(board[i][j].getName(), xy)) {
							return false;
						}
					}
				}
			}
		}
		if(!darkKingInCheck && !lightKingInCheck)
			return false;
		return true;
	}
	
	private void updatePawnReadyForPromotionBooleans() { //Looks to see if a pawn is in position for promotion and updates respective attributes
		int lastRankRespectiveToLight = 0, lastRankRespectiveToDark = 7;
		if(!lightPerspective) {
			 lastRankRespectiveToLight = 7;
			 lastRankRespectiveToDark = 0;
		}
		for(int i = 0; i < 8; i++) {
			if(board[lastRankRespectiveToLight][i] != null && board[lastRankRespectiveToLight][i].getName().contains("pawn")) {
				lightPawnAtLastRank = true;
				break;
			}
			else
				lightPawnAtLastRank = false;
			if(board[lastRankRespectiveToDark][i] != null && board[lastRankRespectiveToDark][i].getName().contains("pawn")) {
				darkPawnAtLastRank = true;
				break;
			}
			else
				darkPawnAtLastRank = false;
		}
	}
	
	public boolean isCapture(String name, String xy) // Takes in name of a Piece and then checks xy to see if the Piece there is on the opposite team, if so then method returns true
	{
		if(board[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')] != null)
			return !getPiece(name).getColor().equals(board[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')].getColor());
		return false;
	}

	private int convertToIntXOrY(String xy, char xOrY) // Helper method for location conversation
	{
		StatStorage.increment(9);
		if (xOrY == 'x')
			return Integer.parseInt(xy.substring(0, 1));
		else
			return Integer.parseInt(xy.substring(1, 2));
	}

	@Override
	public String toString() // Prints out String representation of the board with all Piece locations
	{
		String boardString = "";
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece == null)
					boardString += "#PositionEmpty_" + piece + "#\t";
				else
					boardString += piece.toString().substring(piece.toString().indexOf('#'),
							piece.toString().lastIndexOf('#') + 1) + "\t";
			}
			boardString += "\n";
		}
		return boardString + "\n\n";
	}

	public String toStringDangerBoard() // Prints out String representation of dangerBoard in its current state with all "danger spots" numbered
	{
		String dangerBoardString = "";
		for (int[][] rowAndcol : dangerBoard) {
			for (int[] z : rowAndcol) {
				for (int num : z)
					dangerBoardString += num + " ";
				dangerBoardString += "\t";
			}
			dangerBoardString += "\n";
		}
		return dangerBoardString + "\n\n";
	}
	
	public String printSpotDangerList() { //Prints out each piece that is putting a specific spot in danger
		String output = "";
		int count = 0;
		for(ArrayList<String> spots : spotDangerList) {
			output += "::::Danger to spot " + count + "::::\n";
			for(String name : spots) {
				output += name + "\n";
			}
			output += "\n";
			count++;
		}
		return output;
	}
}