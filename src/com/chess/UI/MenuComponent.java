//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This class is a child of a JLayeredPane component and sets all GUI attributes of the Menu
//It also implements ActionListenr which in turn ends up calling the implemented playAction method in MainControllerUI
package com.chess.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MenuComponent extends JLayeredPane implements ActionListener {

	private JLabel menuImageLabel;
	private JPanel menuBarPanel;
	private JPanel subMenuSettingsPanel;
	private JTextPane gameInfoPane;
	private JTextPane toggleSplitScreenText;
	private JTextPane toggleDevModeText;
	private JTextPane togglePlayerOneTeamText;
	private JButton playBtn;
	private JRadioButton toggleSplitScreen;
	private JRadioButton toggleDevMode;
	private JRadioButton togglePlayerOneTeam;
	private TitledBorder menuSettingsBorder;

	private MenuBarListener action;

	public MenuComponent() { //Constructs the Menu page by creating components and setting their attributes then laying them out using a layout manager 
		menuImageLabel = new JLabel(new CustomImage("/resources/ChessBackGround.jpg", 1008, 756));
		menuBarPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) { //Implements menuBarPanel JPanel instance's paintComponent method in order to make sure to paint the background before the menuBarPanel
														//This is in order to avoid painting artifacts when setting menuBarPanel Component to semi-transparent
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		subMenuSettingsPanel = new JPanel();
		gameInfoPane = new JTextPane();
		toggleSplitScreenText = new JTextPane();
		toggleDevModeText = new JTextPane();
		togglePlayerOneTeamText = new JTextPane();
		playBtn = new JButton("PLAY");
		toggleSplitScreen = new JRadioButton();
		toggleDevMode = new JRadioButton();
		togglePlayerOneTeam = new JRadioButton();
		menuSettingsBorder = new TitledBorder(BorderFactory.createTitledBorder(menuSettingsBorder, "Settings",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans-serif", Font.PLAIN, 15), new Color(4, 148, 255)));

		toggleSplitScreenText.setText("Toggle Split Screen");
		toggleSplitScreenText.setFont(new Font("Sans-serif", Font.PLAIN, 18));
		toggleSplitScreenText.setOpaque(false);
		toggleSplitScreenText.setForeground(new Color(0, 230, 0));
		toggleSplitScreenText.setEditable(false);
		toggleSplitScreen.setOpaque(false);

		toggleDevModeText.setText("Toggle Deveveloper Mode");
		toggleDevModeText.setFont(new Font("Sans-serif", Font.PLAIN, 18));
		toggleDevModeText.setEditable(false);
		toggleDevModeText.setOpaque(false);
		toggleDevModeText.setForeground(new Color(0, 230, 0));
		toggleDevMode.setOpaque(false);
		
		togglePlayerOneTeamText.setText("Change Player One Team to Dark");
		togglePlayerOneTeamText.setFont(new Font("Sans-serif", Font.PLAIN, 18));
		togglePlayerOneTeamText.setEditable(false);
		togglePlayerOneTeamText.setOpaque(false);
		togglePlayerOneTeamText.setForeground(new Color(0, 230, 0));
		togglePlayerOneTeam.setOpaque(false);

		subMenuSettingsPanel.setLayout(new GridBagLayout());
		subMenuSettingsPanel.setBorder(menuSettingsBorder);
		subMenuSettingsPanel.setPreferredSize(new Dimension(300, 75));
		subMenuSettingsPanel.setOpaque(false);

		
		// GridBagConstraints notes x y wd ht wtx wty anchor fill margin(top,left,btm,rt) padx pady
		GridBagConstraints gbc = new GridBagConstraints(); //Lays out the sub menu 
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, -15, 0, 3);
		subMenuSettingsPanel.add(toggleSplitScreenText, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		subMenuSettingsPanel.add(toggleSplitScreen, gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, -75, 0, 3);
		subMenuSettingsPanel.add(toggleDevModeText, gbc);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		subMenuSettingsPanel.add(toggleDevMode, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0, 35, 8, 0);
		subMenuSettingsPanel.add(togglePlayerOneTeamText, gbc);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(5, 44, 8, 0);
		subMenuSettingsPanel.add(togglePlayerOneTeam, gbc);
		
		playBtn.setFont(new Font("Sans-serif", Font.PLAIN, 30));
		playBtn.setBackground(new Color(4, 148, 255));
		playBtn.setForeground(new Color(230, 230, 230));
		playBtn.setFocusPainted(false);
		playBtn.setBorderPainted(false);
		playBtn.addActionListener(this);
		playBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				playBtn.setBackground(new Color(40, 160, 255));
				playBtn.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				playBtn.setBackground(new Color(4, 148, 255));
				playBtn.setForeground(new Color(230, 230, 230));
			}
		});

		menuBarPanel.setLayout(new GridBagLayout());
		menuBarPanel.setBounds(0, 612, 1008, 144);
		menuBarPanel.setOpaque(false);
		menuBarPanel.setBackground(new Color(0, 0, 0, 200));

		// x y wd ht wtx wty anchor fill margin(top,left,btm,rt) padx pady
		menuBarPanel.add(playBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(8, 75, 5, 40), 115, 20));
		menuBarPanel.add(subMenuSettingsPanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.BASELINE,
				GridBagConstraints.NONE, new Insets(0, 0, 5, 75), 250, 44));

		menuImageLabel.setSize(1008, 756);
		menuImageLabel.setOpaque(false);

		gameInfoPane.setBounds(768, 0, 260, 20);
		gameInfoPane.setText("Beta 1.1 Developed by Jake Sutton.");
		gameInfoPane.setOpaque(false);
		gameInfoPane.setForeground(new Color(0, 230, 0));
		gameInfoPane.setFont(new Font("Sans-serif", Font.PLAIN, 12));
		gameInfoPane.setEditable(false);
		
		//Adds the components to the layered pane forming the MenuComponment 
		add(menuImageLabel);
		add(menuBarPanel, 0);
		add(gameInfoPane, 0);
	}

	public void setActionListener(MenuBarListener listener) {//MenuBarListner setter in order that MainControllerUI may be the actual implementation of this class' action performed method
		this.action = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) { //Action Performed method implementation which calls the implemented playAction method in MainController 
		if (action != null)
			action.playAction(toggleSplitScreen.isSelected(), toggleDevMode.isSelected(), togglePlayerOneTeam.isSelected());
	}
}