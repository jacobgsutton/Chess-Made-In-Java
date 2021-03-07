//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Main Controller of UI in other words this class and its associated 
//class MainFrame (in the same file) acts as the main "switch board" for the underlying 
//graphical components for this program, some main functions include specifying JFrame 
//attributes, implementing action listeners for the overall graphical instance, 
//and launching the actual game components
package com.chess.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class MainControllerUI {
	public MainControllerUI() {
		new MainFrame();
	}
}

@SuppressWarnings("serial")
class MainFrame extends JFrame {
	public MainFrame() {
		//Since swing components are thread safe meaning they cann't be touched by threads outside the single thread they are activated "maintained" by -the 
		//Event Dispatch Thread EDT- invoke later is used to make sure all your code is put on the Event Dispatch Thread but only after all other pending events on the thread
		//are executed
		SwingUtilities.invokeLater(new Runnable() {

			private Dimension dim;
			private MenuComponent mc;
			private GameDisplayComponent gdc;
			private boolean fullScreen = false;
			private InputStream is;

			@Override
			public void run() { //This will be called when the a MainFrame is constructed and this acts as the "graphical setup" method 
								//and is actually from which every other part of the graphical environment stems 
				dim = Toolkit.getDefaultToolkit().getScreenSize();
				mc = new MenuComponent();
				is = getClass().getResourceAsStream("/resources/ChessIconImage.png");
				
				//Sets up the JFrame and sets the content pane to a MenuComponent
				setSize(1008, 795);
				setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
				setTitle("Chess");
				try {
					setIconImage(ImageIO.read(is));
				} catch (IOException e) {
					e.printStackTrace();
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setResizable(false);
				setContentPane(mc);
				setVisible(true);
				
				//Sets MenuComponents action listener by passing a MenuBarListener interface with implemented playAction method
				mc.setActionListener(new MenuBarListener() {
					@Override
					public void playAction(boolean splitScreen, boolean developerMode, boolean changePlayerOneToDark) {
						System.out.println("Play button clicked");
						DeveloperModePane.printText("Play button clicked");
						//Loads game once play button is clicked and passes specific toggle settings
						loadGameUI(splitScreen, developerMode, changePlayerOneToDark);
					}
				});
			}
			
			//Load game function which creates a GameDisplayCompoenent and sets the JFrame content pane to contain that component
			//it also implements a key listener tied to the JFrame in order to listen for a alt + enter key command which toggles full screen
			void loadGameUI(boolean splitScreen, boolean developerMode, boolean changePlayerOneToDark) {
				gdc = new GameDisplayComponent(dim.width, dim.height, splitScreen, developerMode, changePlayerOneToDark);
				setResizable(true);
				fullScreen();
				setContentPane(gdc);
				setVisible(true);
				
				//Listener for alt + enter full screen toggle
				addKeyListener(new KeyAdapter() {
					//Set Interface which is implemented by HashSet which contains Integer wrapper objects
					private final Set<Integer> pressed = new HashSet<Integer>();

					@Override
					public synchronized void keyPressed(KeyEvent ke) { //Implements KeyPressed and also synchronizes it in order that two events can be listened for on one thread
						System.out.println("Key Pressed");
						DeveloperModePane.printText("Key Pressed");
						pressed.add(ke.getKeyCode()); //Adds pressed key code to the HashSet
						if (pressed.size() > 1) { //Checks to see if two keys are pressed in other words "Are two key codes in the Hash Set?"
							if (pressed.contains(KeyEvent.VK_ENTER) && pressed.contains(KeyEvent.VK_ALT)) { //Checks to see if the key codes match enter and alt, if so it either calls full screen function or reverses full screen if already full screen 
								System.out.println("Toggling fullscreen");
								DeveloperModePane.printText("Toggling fullScreen");
								if (fullScreen) {
									fullScreen = false;
									dispose();
									setUndecorated(false);
									setVisible(true);
								} else
									fullScreen();
							}
						}
					}

					@Override
					public synchronized void keyReleased(KeyEvent ke) { //Removes key that is released from the HashSet 
						pressed.remove(ke.getKeyCode());
					}
				});
			}
			
			//Full screen function in other words it updates the JFrame to be in its extended 
			//state by disposing the old state and then setting extended state to maxim values
			void fullScreen() {
				fullScreen = true;
				dispose();
				setUndecorated(true);
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				setVisible(true);
			}
		});
	}
}