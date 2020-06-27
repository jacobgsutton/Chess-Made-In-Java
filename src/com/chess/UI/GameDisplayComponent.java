//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This is GUI of the game itself and is the component that the MainFrame's content pane is set to when the play button is clicked
//It lays out the game display depending on setting passed from the MainController, it also sets JOptionPane internal attributes through UIManager
package com.chess.UI;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import com.chess.base.ChessBoard;
import com.chess.base.StatStorage;

@SuppressWarnings("serial")
public class GameDisplayComponent extends JLayeredPane {

	private JPanel mainPanel;
	private JTextPane fullscreenToggleInfo;
	private int leftMarginOnDeveloperPane = 0;
	private int width;
	private int height;
	private ChessBoard gameInstance;
	private ChessBoardUI playerOneView;
	private ChessBoardUI playerTwoView;

	public GameDisplayComponent(int width, int height, boolean splitScreen, boolean developerMode, boolean changePlayerOneToDark) { //Constructs the component
		StatStorage.reset();
		this.width = width;
		this.height = height;

		fullscreenToggleInfo = new JTextPane();

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setBounds(0, 0, this.width, this.height);
		mainPanel.setLayout(new GridBagLayout());

		fullscreenToggleInfo.setBounds(width-175, height-22, 175, 20);
		fullscreenToggleInfo.setText("alt + enter to toggle fullscreen.");
		fullscreenToggleInfo.setOpaque(false);
		fullscreenToggleInfo.setForeground(new Color(0, 230, 0));
		fullscreenToggleInfo.setFont(new Font("Sans-serif", Font.PLAIN, 12));
		fullscreenToggleInfo.setEditable(false);
		fullscreenToggleInfo.setFocusable(false);
		
		UIManager.put("OptionPane.background",new ColorUIResource(0,0,0));
		UIManager.put("OptionPane.messageForeground", Color.GREEN);
		UIManager.put("Panel.background",new ColorUIResource(0,0,0));
		UIManager.put("OptionPane.noButtonMnemonic","1");
		UIManager.put("OptionPane.yesButtonMnemonic","1");
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		defaults.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		
		if(changePlayerOneToDark) {
			gameInstance = new ChessBoard("dark");
		}
		else {
			gameInstance = new ChessBoard("light");
		}
		
		// GridBagConstraints notes x y wd ht wtx wty anchor fill margin(top,left,btm,rt) padx pady
		if (!splitScreen) { //Single view
			mainPanel.add(new ChessBoardUI(gameInstance, playerTwoView), new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0, GridBagConstraints.CENTER, // Center Board																						
					GridBagConstraints.NONE, new Insets(0, 0, 92, 0), 0, 0));
		}
		if (splitScreen) { //Split screen view
			leftMarginOnDeveloperPane = 90;
			mainPanel.add(playerOneView = new ChessBoardUI(gameInstance, playerTwoView = new ChessBoardUI(gameInstance, playerOneView, true, true)), new GridBagConstraints(0, 0, 0, 0, 1.0, 0.0, GridBagConstraints.WEST, // Left Board																									 
					GridBagConstraints.NONE, new Insets(0, 90, 92, 0), 0, 0));
			playerTwoView.setOtherView(playerOneView);
			mainPanel.add(playerTwoView, new GridBagConstraints(0, 0, 0, 0, 0.0, 0.0, GridBagConstraints.EAST, // Right board
					GridBagConstraints.NONE, new Insets(0, 0, 92, 90), 0, 0));
		}
		if (developerMode) { //DevloperMode view
			mainPanel.add(new DeveloperModePane(), new GridBagConstraints(0, 0, 0, 0, 0.0, 1.0, // DeveloperMode Pane
					GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, leftMarginOnDeveloperPane, 0, 0), 0, 0));
		}

		add(mainPanel);
		add(fullscreenToggleInfo, 0);
	}
}
