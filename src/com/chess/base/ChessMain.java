//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Called by the JVM when program is executed and creates a new 
//MainControllerUI which handles the main thread for the game UI 
//and where actual chess.base logic will be used eventually by other UI components

package com.chess.base;

import com.chess.UI.MainControllerUI;

public class ChessMain {
	public static void main(String[] args) {
		new MainControllerUI();
	}
}
