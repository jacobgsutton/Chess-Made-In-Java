//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This class is for the creation of a JTextArea thats sole purpose is display internal program information the text 
//area attribute is also a static field so all class references refer to one TextArea 
package com.chess.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class DeveloperModePane extends JScrollPane {

	private static TextArea textArea;
	private static long wordCount;

	static { //static block to create a static Text Area
		textArea = new TextArea();
	}

	public DeveloperModePane() { // Constructs a DevloperModePane by specifying parent JScrollPane settings  
		super(textArea);

		JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL) {
			@Override
			public boolean isVisible() { //Overrides scrollBar JscrollBar Object to always return true, "tricks" java into not showing the scrollBar even if added to the JScrollPane
				return true;			// and its functionality is still able to be used through mouse wheel
			}
		};
		setPreferredSize(new Dimension(1400, 125));
		scrollBar.setUnitIncrement(16); //Sets scrolling speed 
		setVerticalScrollBar(scrollBar); //setsTheVertacalScrollBar to scrollBar
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); //Don't show vertical scroll bar
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //Don't show horizontal scroll bar
		setBorder(null); //Gets rid of any border surrounding the panel 
	}

	public static void printText(String text) { //Adds texts to the DeveloperPane by appending it
		updateWordCount(text);
		textArea.append(text + "\n");
	}
	private static void updateWordCount(String text) { //Called in order to update the wordCount
		String[] temp = text.split(" ");
		wordCount += temp.length;
	}
	
	public static long getWordCount() { //Getter for wordCount
		return wordCount;
	}
}

@SuppressWarnings("serial")
class TextArea extends JTextArea {

	public TextArea() { //TextArea settings constructor 
		setFocusable(false);
		setEditable(false);
		setFont(new Font("Sans-serif", Font.PLAIN, 12));
		setBackground(Color.BLACK);
		setForeground(Color.GREEN);
		append("Developer Mode Active...\n");
	}
}