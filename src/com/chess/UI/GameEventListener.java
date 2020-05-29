//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Functional Interface used for implementation of game events
package com.chess.UI;

import java.awt.event.MouseEvent;
 
public interface GameEventListener {
	public void gameAction(MouseEvent mouseEvt, int errorCode);
}
