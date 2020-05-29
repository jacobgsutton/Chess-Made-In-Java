//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Functional interface for menu bar event implementation
package com.chess.UI;

public interface MenuBarListener {
	public void playAction(boolean splitScreen, boolean developerMode, boolean changePlayerOneToDark);
}
