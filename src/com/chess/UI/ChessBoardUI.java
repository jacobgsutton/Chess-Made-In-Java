//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This is the graphical ChessBoard object which sets and updates the view and listens for user input
//Some important functions include updating the graphical board by looking at the the "actual" chess board, and 
//implementation of Mouse Events on PieceUI objects, which is done in lambdas. This class is the main user interaction with the ChessBoard thus 
//this class acts as the house upon the ChessBoard foundation "coupling" the classes closely together
//This class is quite important probably just as impotent as the Chess Board itself
package com.chess.UI; 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.base.ChessBoard;
import com.chess.base.Piece;
import com.chess.base.StatStorage;

@SuppressWarnings("serial")
public class ChessBoardUI extends JLayeredPane {
	private JLabel chessBoardImageLabel;
	private JPanel gameFoundation;
	private JPanel boardFoundation;
	private JPanel topLayer;
	//otherView is used when split screen is active and each instance of this same class need to communicate about updates to the view
	private ChessBoardUI otherView;
	private PromotionUI currentPromotionComponent;
	private ArrayList<ArrayList<PieceUI>> pieceUITempStorage;
	//Each index corresponds to a Piece ID and each int inside each index corresponds to how many Pieces of that id there needs to be including promotion pieces
	private final int[] NUMBER_OF_PIECES = {10,10,10,9,1,8,10,10,10,9,1,8}; 
	private boolean darkPerspective;
	private ChessBoard gameInstance;
	private BoardSquareUI currentHeldSquare;
	private Piece currentHeldActualPiece;
	private GameEventListener dragged;
	private GameEventListener released;
	private GameEventListener promotionPieceClicked;
	private JPanel currentHeldPiece;
	private JPanel startingSquare;
	private int x, y;
	private boolean block;
	private String squareID;
	private String name;
	private int draggedEventInteration;
	
	public ChessBoardUI(ChessBoard gameInstance, ChessBoardUI otherView) {
		this(gameInstance, otherView, false, false);
	}
	//Constructs the Graphical chess board by setting the actual image of the board and building the various layers of JPanels 
	public ChessBoardUI(ChessBoard gameInstance, ChessBoardUI otherView, boolean darkPerspective, boolean reverse) { 
		this.darkPerspective = darkPerspective;
		this.gameInstance = gameInstance;
		this.otherView = otherView;
		System.out.println(gameInstance);
		DeveloperModePane.printText(gameInstance.toString());
		System.out.println(gameInstance.toStringDangerBoard());
		DeveloperModePane.printText(gameInstance.toStringDangerBoard());
		System.out.println(gameInstance.printSpotDangerList());
		DeveloperModePane.printText(gameInstance.printSpotDangerList());
		setPreferredSize(new Dimension(825, 922));
		
		pieceUITempStorage = new ArrayList<ArrayList<PieceUI>>(12);
		for(int i=0; i < 12; i++) {
		    pieceUITempStorage.add(new ArrayList<PieceUI>());
		}
		
		gameFoundation = new JPanel();
		gameFoundation.setSize(825, 922);
		gameFoundation.setBackground(Color.BLACK);
		gameFoundation.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		if(reverse)
			chessBoardImageLabel = new JLabel(new CustomImage("/resources/Chess_Board_Reverse.png", 825, 825));
		else
			chessBoardImageLabel = new JLabel(new CustomImage("/resources/Chess_Board.png", 825, 825));
		chessBoardImageLabel.setBounds(0,97,825, 825);
		
		boardFoundation = new JPanel();
		boardFoundation.setBounds(24, 124, 777, 776);
		boardFoundation.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		boardFoundation.setOpaque(false);
		
		topLayer = new JPanel();
		topLayer.setBounds(0,97,825,825);
		topLayer.setOpaque(false);
		
		generateChessBoardPanelGrid();
		
		add(gameFoundation);
		add(chessBoardImageLabel,0);
		add(boardFoundation,0);
		add(topLayer,0);
		
		//Once setup view is completed the "running view" is entered through the run method
		run();
	}

	private void generateChessBoardPanelGrid() { //Generates a Grid of BoardSquareUIs 8 x 8 on top of boardFoundation
		int squareStart63 = 63;
		int squareStart0 = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(darkPerspective) {
					currentHeldSquare = new BoardSquareUI((7-j) + "" + (7-i));
					currentHeldSquare.setSquareNumber(squareStart63--);
					boardFoundation.add(currentHeldSquare);
				}
				else {
					currentHeldSquare = new BoardSquareUI(j + "" + i);
					currentHeldSquare.setSquareNumber(squareStart0++);
					boardFoundation.add(currentHeldSquare);
				}
			}
		}
	}
	
	private void loadUIPieces() { //Loads PieceUIs needed to play standard chess
		PieceUI pieceUI = null;
		for(int i = 0; i < 12; i++) 
			for(int j = 0; j < NUMBER_OF_PIECES[i]; j++) {
				switch(i) {
					case 0 : pieceUI = new PieceUI("/resources/RookW.png", 0); break;
					case 1 : pieceUI = new PieceUI("/resources/KnightW.png", 1); break;
					case 2 : pieceUI = new PieceUI("/resources/BishopW.png", 2); break;
					case 3 : pieceUI = new PieceUI("/resources/QueenW.png", 3); break;
					case 4 : pieceUI = new PieceUI("/resources/KingW.png", 4); break;
					case 5 : pieceUI = new PieceUI("/resources/PawnW.png", 5); break;
					case 6 : pieceUI = new PieceUI("/resources/RookB.png", 6); break;
					case 7 : pieceUI = new PieceUI("/resources/KnightB.png", 7); break;
					case 8 : pieceUI = new PieceUI("/resources/BishopB.png", 8); break;
					case 9 : pieceUI = new PieceUI("/resources/QueenB.png", 9); break;
					case 10 : pieceUI = new PieceUI("/resources/KingB.png", 10); break;
					case 11 : pieceUI = new PieceUI("/resources/PawnB.png", 11); break;
					default : pieceUI = new PieceUI("/resources/PawnW.png", 5); break;
				}
				//Sets the PieceUI's GameEventListers with the implementations of the GameEventListener interface
				if(dragged != null) 
					pieceUI.setGameEventDragged(dragged);
				if(released != null)
					pieceUI.setGameEventReleased(released);
				pieceUITempStorage.get(i).add(pieceUI);
			}
	}
	
	public void updateGraphicalBoard(ChessBoardUI instance) { //Updates the graphical board by looking at the actual board instance, this acts a refresh to account for changes to the board
															  //that are not always automatically accounted for
		int square = 0;
		int decInc = 1;
		PieceUI current = null;
		
		for(int i = 0; i < 8; i++)  //Removes all PieceUIs
			for(int j = 0; j < 8; j++) {
				if(((BoardSquareUI)instance.boardFoundation.getComponent(square)).getComponentCount() == 1) 
					instance.pieceUITempStorage.get(((PieceUI)((BoardSquareUI)instance.boardFoundation.getComponent(square)).getComponent(0))
					.getID()).add(((PieceUI)((BoardSquareUI)instance.boardFoundation.getComponent(square)).getComponent(0)));
				square++;
			}
		if(instance.darkPerspective) {
			square = 63;
			decInc = -1;
		}
		else
			square = 0;
		for(int i = 0; i < 8; i++)  //Looks at the actual board and grabs and adds PieceUIs to the right BoardSquareUIs accordingly
			for(int j = 0; j < 8; j++) {
				if(((JPanel)(instance.boardFoundation.getComponent(square))).getComponentCount() != 0)
					((JPanel)(instance.boardFoundation.getComponent(square))).removeAll();
				for(int k = 0; k < 12; k++) {
					StatStorage.increment(12);
					if(gameInstance.getChessBoard()[i][j] != null && gameInstance.getChessBoard()[i][j].getID() == k) {
						current = instance.pieceUITempStorage.get(k).remove(0);
						current.setName(gameInstance.getChessBoard()[i][j].getName());
						((JPanel)(instance.boardFoundation.getComponent(square))).add(current);
						k = 12;
					}
				}
				square += decInc;
			}
		if(instance != null) {
			instance.repaint();
			instance.revalidate();
		}
	}
	
	private void run() { //This method maintains the graphical board by implementation of game events and is really where the entire 
						//game is "played," in all event implementations the graphical board is being updated 
		dragged = (evt, errorCode) -> { //Implements PieceUI dragged events
			if(draggedEventInteration == 0) {
				currentHeldPiece = (JPanel)evt.getSource();
				startingSquare = (JPanel)currentHeldPiece.getParent();
				if(!block && (gameInstance.isLightsTurn() && ((PieceUI)evt.getSource()).getID() < 6) || (!gameInstance.isLightsTurn() && ((PieceUI)evt.getSource()).getID() > 5)) {
					x = ((JPanel)evt.getSource()).getParent().getX() + 24;
					y = ((JPanel)evt.getSource()).getParent().getY() + 27;
					topLayer.add(currentHeldPiece);
					currentHeldPiece.setLocation(x,y);
					repaint();
				}
				else {
					startingSquare.add(currentHeldPiece);
				}
			}
			int newX = currentHeldPiece.getX() + evt.getX()-48, newY = currentHeldPiece.getY() + evt.getY()-48;
			if(currentHeldPiece.getParent() == topLayer) 
				currentHeldPiece.setLocation(newX,newY);
			else {
				startingSquare.add(currentHeldPiece);
				draggedEventInteration = -1;
			}
			repaint();
			x = newX;
			y = newY;
			System.out.println("Dragged to " + newX + ", " + newY);
			DeveloperModePane.printText("Dragged to " + newX + ", " + newY);
			draggedEventInteration++;
		};
		released = (evt, errorCode) -> { //Implements PieceUI released events, and then in turn calls a lot of the ChessBoard logic methods to check for move legality as well as game updates
			revalidate();
			draggedEventInteration = 0;
			name = ((PieceUI)evt.getSource()).getName();
			if((JPanel)boardFoundation.getComponentAt(x + 24, y + 27) != null && (JPanel)boardFoundation.getComponentAt(x + 24, y + 27) != startingSquare && errorCode != -1) {
				squareID = ((BoardSquareUI)boardFoundation.getComponentAt(x + 24, y + 27)).getID();
				if(gameInstance.legalMove(name, squareID)) {
					if(gameInstance.isCapture(name, squareID)) {
						if(gameInstance.getEnPassant()) {
							x = gameInstance.getEnPassantInt()[0] * 100;
							y = gameInstance.getEnPassantInt()[1] * 100;
							if(darkPerspective) {
								x = 700 - x;
								y = 700 - y;
							}
							gameInstance.addCapture(gameInstance.getAndRemovePieceAtLocation(gameInstance.getEnPassantXY()));
						}
						else 
							gameInstance.addCapture(gameInstance.getAndRemovePieceAtLocation(squareID));
						pieceUITempStorage.get(((PieceUI)((JPanel)boardFoundation.getComponentAt(x + 24, y + 27)).getComponent(0)).getID()).add(((PieceUI)((JPanel)boardFoundation.getComponentAt(x + 24, y + 27)).getComponent(0)));
						((JPanel)boardFoundation.getComponentAt(x + 24, y + 27)).removeAll();
					}
					gameInstance.movePieceToLocation(name, squareID, true);
					((JPanel)boardFoundation.getComponentAt(x + 24, y + 27)).add(((JPanel)evt.getSource()));
					
					//This and following else if checks to see if a PromotionUI needs to be added to view if pawn is ready for promotion
					if(gameInstance.isLightsTurn() && gameInstance.isLightPawnReadyForPromotion()) {
						currentPromotionComponent = new PromotionUI(false);
						currentPromotionComponent.setGameEventPromotionPieceClicked(promotionPieceClicked);
						gameFoundation.add(currentPromotionComponent);
						block = true;
						if(otherView != null)
							otherView.block = true;
						repaint();
						revalidate();
					}
					else if(!gameInstance.isLightsTurn() && gameInstance.isDarkPawnReadyForPromotion()) {
						currentPromotionComponent = new PromotionUI(true);
						currentPromotionComponent.setGameEventPromotionPieceClicked(promotionPieceClicked);
						gameFoundation.add(currentPromotionComponent);
						block = true;
						if(otherView != null)
							otherView.block = true;
						repaint();
						revalidate();
					}
					else
						afterTurn();
				}
				else if(startingSquare != null)
					startingSquare.add(((JPanel)evt.getSource()));
			}
			else if(startingSquare != null) {
				startingSquare.add(((JPanel)evt.getSource()));
			}
			currentHeldSquare = null;
			currentHeldPiece = null;
			startingSquare = null;
			name = null;
			repaint();
			revalidate();
		};
		promotionPieceClicked = (promotionEvt, promotionErrorCode) -> { //Implementation for PromotionUI events, which is used to replace pawn with a newly picked piece 
			System.out.println(((JPanel)promotionEvt.getSource()).getName());
			DeveloperModePane.printText(((JPanel)promotionEvt.getSource()).getName());
			gameInstance.getAndRemovePieceAtLocation(squareID);
			if(gameInstance.isLightsTurn())
				currentHeldActualPiece = gameInstance.getLightPromotionPieceArray(Integer.parseInt(((JPanel)promotionEvt.getSource()).getName())).get(0);
			else
				currentHeldActualPiece = gameInstance.getDarkPromotionPieceArray(Integer.parseInt(((JPanel)promotionEvt.getSource()).getName())).get(0);
			currentHeldActualPiece.setColumnAttribute((char)('A' + Integer.parseInt(squareID.substring(0,1))));
			gameInstance.placePieceAtLocation(currentHeldActualPiece, squareID);
			gameFoundation.remove(0);
			block = false;
			if(otherView != null)
				otherView.block = false;
			afterTurn();
		};
		
		loadUIPieces(); //When first Ran the pieces get created
		updateGraphicalBoard(this);
	}
	
	public void afterTurn() { //This is called after a released event has resulted in a piece being moved to new location and thus a turn has been taken
		//Update both graphical instances
		updateGraphicalBoard(this);
		if(otherView != null)
			updateGraphicalBoard(otherView);
		repaint();
		revalidate();
		System.out.println("Released");
		DeveloperModePane.printText("Released");
		System.out.println(gameInstance);
		DeveloperModePane.printText(gameInstance.toString());
		System.out.println(gameInstance.toStringDangerBoard());
		DeveloperModePane.printText(gameInstance.toStringDangerBoard());
		System.out.println(gameInstance.printSpotDangerList());
		DeveloperModePane.printText(gameInstance.printSpotDangerList());
		
		//Calls logical afterTurnUpdate on gameInstance and displays GAME OVER Prompt if necessary
		String team = "Dark";
		if(gameInstance.isLightsTurn()) {
			team = "Light";
			StatStorage.increment(0);
		}
		else
			StatStorage.increment(1);
		String gameOverMsg = "Checkmate! " + team + " Team Wins!";
		switch(gameInstance.afterTurnUpdate()) {
			case 0: break;
			case 2: gameOverMsg = "Stalemate! The Game Is A Draw!";
			default:
			System.out.println("GAME OVER");
			DeveloperModePane.printText("GAME OVER");
			StatStorage.updateCaculations();
			//Game Over simple JOptionPane, shows stats and gives option to play again or quit
			int input = JOptionPane.showConfirmDialog(this.getParent(), gameOverMsg + " Do you want to play again?\nSTATS::\n" + StatStorage.staticToString(), "GAME OVER", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new CustomImage("/resources/GameOverChessArt.jpg", 420,379));
			if(input == JOptionPane.YES_OPTION) {
				((MainFrame)SwingUtilities.getRoot(this)).dispose();
				new MainControllerUI();
			}
			else if(input == JOptionPane.NO_OPTION)
				System.exit(0);
		}
	}
	
	public ChessBoardUI getOtherView() { //returns the other view
		return otherView;
	}
	
	public void setOtherView(ChessBoardUI otherView) { //sets the other view
		this.otherView = otherView; 
	}
	
}











