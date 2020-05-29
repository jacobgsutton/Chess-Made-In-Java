//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This Class is the graphical representation of a Piece object which sets graphical attributes and adds listeners to the object itself
package com.chess.UI;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PieceUI extends JPanel {
	
	private JLabel pieceLabel;
	private CustomImage image;
	private int id;
	private GameEventListener dragged;
	private GameEventListener released;
	private int errorCode;
	private int mouseDarggedInteractionCount;
	public PieceUI(String pieceImagePath, int id) {	
		this.id = id;
		pieceLabel = new JLabel(image = new CustomImage(pieceImagePath, 95,95));
		pieceLabel.setSize(95,95);
		setLayout(new GridLayout());
		setOpaque(false);
		add(pieceLabel);
		addMouseListener(new MouseAdapter() { 
			public void mouseReleased(MouseEvent evt) { //Calls the implemented mouse released method, which is done in a lambda in ChessBoardUI
				System.out.println("Mouse released Piece Graphic: " + pieceImagePath);
				DeveloperModePane.printText("Mouse released Piece Graphic: " + pieceImagePath);
				mouseDarggedInteractionCount = 0;
				released.gameAction(evt, errorCode);
			}
			public void mouseEntered(MouseEvent evt) { //When this component receives mouse entered events it passes those events to its parent 
				getParent().dispatchEvent(evt); //Sends the mouseEntered event to the parent of this component which is the BoardSquareUI it is in
			}
		});
		addMouseMotionListener(new MouseAdapter() { 
			public void mouseDragged(MouseEvent evt) { //Calls the implemented mouse dragged method, which is done in a lambda in ChessBoardUI 
													  //only after it runs a check on the color value of where the event is being received from in order 
													  //that nothing happens if the alpha 0 part of the panel is receiving the event
				int alpha = 0;
				int alphaFirstInteraction = 0;
				try {
					if(mouseDarggedInteractionCount == 0)
						alphaFirstInteraction = image.getColorAtPoint(evt.getX(), evt.getY()).getAlpha();
					alpha = image.getColorAtPoint(evt.getX(), evt.getY()).getAlpha();
				} catch(ArrayIndexOutOfBoundsException e) {
					alpha = -1;
				}
				if(alpha != 0 && alpha != -1) {
					System.out.println("Mouse Is Dragging Piece Graphic: " + pieceImagePath);
					DeveloperModePane.printText("Mouse Is Dragging Piece Graphic: " + pieceImagePath);
					dragged.gameAction(evt, errorCode);
				}
				else if(alpha == -1 && alphaFirstInteraction != 0)
					dragged.gameAction(evt, errorCode = -1);
				else if(alpha == -1)
					dragged.gameAction(evt, errorCode = -1);
				errorCode = 0;
				mouseDarggedInteractionCount++;
			}
		});
	}
	//Simple getters and setters
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public void setGameEventDragged(GameEventListener gel) {
		this.dragged = gel;
	}
	
	public void setGameEventReleased(GameEventListener gel) {
		this.released = gel;
	}
}
