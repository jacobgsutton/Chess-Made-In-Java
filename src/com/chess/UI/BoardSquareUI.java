//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This class is for the creation of board squares that have specific identification and is able to have PieceUIs added to them
package com.chess.UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoardSquareUI extends JPanel {

	private String squareID; //xy id
	private int squareNumber;
	
	public BoardSquareUI(String id) {
		this.squareID = id;
		if(this.squareID.charAt(0) == '7')
			setPreferredSize(new Dimension(98,97));
		else
			setPreferredSize(new Dimension(97,97));
		
		setOpaque(false);
		setLayout(new GridLayout());
		addMouseListener(new MouseAdapter() { //Listener for seeing where mouse has entered
			public void mouseEntered(MouseEvent evt) {
				System.out.println("Mouse entered square (" + getID().charAt(0) + ", " + getID().charAt(1) + ")");
				DeveloperModePane.printText("Mouse entered square (" + getID().charAt(0) + ", " + getID().charAt(1) + ")");
			}
		});
	}
	
	//Getters and setters for identifying board square component 
	public String getID() {
		return this.squareID;
	}
	
	public void setID(String id) {
		this.squareID = id;
	}
	
	public int getSquareNumber() {
		return this.squareNumber;
	}
	
	public void setSquareNumber(int squareNumber) {
		this.squareNumber = squareNumber;
	}
	
	public String toString() {
		return "Square: " + this.squareID;
	}

	
//Test code for being able to easily see the squares
//	private Color randomColor() {
//		switch((int)(Math.random() * 6)) {
//		
//			case 0 : return Color.BLACK; 
//			case 1 : return Color.YELLOW;
//			case 2 : return Color.RED;
//			case 3 : return Color.GREEN;
//			case 4 : return Color.BLUE;
//			default : return Color.YELLOW;
//		}
//	}
}

