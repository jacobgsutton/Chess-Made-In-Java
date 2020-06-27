//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Stores stats for a game, all attributes and functions are static
//and there is no constructor, this is single storage class it is never an "object," 
//all other classes access this single static code base, all time is first counted in nanoSeconds using
//System.nanoTime(), I picked this because it seemed the most accurate in that it uses JVMs internal time keeping
//functionality instead of native "wall clock" time

package com.chess.base;

import java.util.ArrayList;

import com.chess.UI.DeveloperModePane;

public class StatStorage {
	//Holds all turn times for light
	private static ArrayList<Long> lightTurnTimes = new ArrayList<Long>();
	//Holds all turn times for dark
	private static ArrayList<Long> darkTurnTimes = new ArrayList<Long>();
	//Holds temporal current turn start time and other attribute holds temporal current end of turn time
	private static Long tempTurnTimeStart, tempTurnTimeEnd;
	//Holds all 18 main stats
	private static ArrayList<Object> statStorage = new ArrayList<Object>(18);
	//Holds all titles of stats that parallel statStorage
	private static final String[] STAT_TITLES = {"Light Turns", "Dark Turns", "Total Turns", "Light In Check", "Dark In Check",
									"updateDangerBoard() Inner Iterations", "getPieceLocation() Invocations", 
									"movedBlocked() Inner Interations", "checkMate() Inner Interations", "convertToIntXOrY() Invocations",
									"Developer Pane Word Count", "inCheckOrDanger() Invocations", "updateGraphicalBoard() Inner Interations", "Total Game Time", 
									"Light's Total Turn Time", "Dark's Total Turn Time", "Light's Turn Time Average", "Dark's Turn Time Average"};
	
	//Fills storage with 0s 
	private static void fillStorageWithZeros(){
		for(int i = 0; i < 18; i++)
			statStorage.add((Object)0);
	}
	
	//Increments a stat in a certain index by 1 
	public static void increment(int index) {
		if(index < 13)
			statStorage.set(index, (Integer)statStorage.get(index) + 1);
	}
	//Returns a stat at index
	public static Object getStat(int index) {
		return statStorage.get(index);
	}
	//Calculates total turns by adding dark turns and light turns
	private static void calculateTotalTurns() {
		statStorage.set(2, (Integer)statStorage.get(0) + (Integer)statStorage.get(1));
	}
	
	//Takes in a nanoSeconds and turns it into a formated String in HH:MM:ss.milliseconds.nanoseconds
	private static String formatNanosecondsToTimeString(double nanoseconds) {
		int seconds = 0, minutes = 0, hours = 0, milliseconds = (int)(nanoseconds / 1000000);
		nanoseconds %= 1000000;
		if(milliseconds != 0)
			 seconds = milliseconds / 1000;
		milliseconds %= 1000;
		if(seconds != 0)
			minutes = seconds / 60;
		seconds %= 60;
		if(minutes != 0)
			hours = minutes / 60;
		minutes %= 60;
		
		return hours + ":" + minutes + ":" + seconds + "." + milliseconds + "." + (int)nanoseconds;
	}
	
	//Starts turn clock by grabbing the nanoSecond at time of this method execution
	public static void startTurnClock() {
		tempTurnTimeStart = System.nanoTime();
	}
	
	//Ends turn clock by grabbing the nanoSecond at time of this method execution and then subtracts tempTurnTimeStart and adds it to a respect9ive index in storage specified by what team is passed 
	//0 for light, 1 for dark (or any other number)
	public static void endTurnClockFor(int team) {
		tempTurnTimeEnd = System.nanoTime();
		if(team == 0)
			lightTurnTimes.add(tempTurnTimeEnd - tempTurnTimeStart);
		else
			darkTurnTimes.add(tempTurnTimeEnd - tempTurnTimeStart);
	}
	
	//Calculates final time totals and averages by processing lightTurnTimes and darkTurnTimes and then passes those values to 
	//formatNanosecondsToTimeString() to be formated and then passes the return value to respective storage index
	private static void calculateTimeTotalsAndAverages() {
		long gameTime = 0, lightTimeTotal = 0, darkTimeTotal = 0;
		double lightAverageTime = 0, darkAverageTime = 0;
		
		for(long times : lightTurnTimes) 
			lightTimeTotal += times;
		for(long times : darkTurnTimes) {
			darkTimeTotal += times;
		}
		lightAverageTime = lightTimeTotal / lightTurnTimes.size();
		darkAverageTime = darkTimeTotal / darkTurnTimes.size();
		gameTime = lightTimeTotal + darkTimeTotal;
		statStorage.set(13, formatNanosecondsToTimeString(gameTime));
		statStorage.set(14, formatNanosecondsToTimeString(lightTimeTotal));
		statStorage.set(15, formatNanosecondsToTimeString(darkTimeTotal));
		statStorage.set(16, formatNanosecondsToTimeString(lightAverageTime));
		statStorage.set(17, formatNanosecondsToTimeString(darkAverageTime));
	}
	
	//This calls calculation methods and the DeveloperModePane word count method in order to finalize stats that can't be calculated until end of a game
	public static void updateCaculations() {
		calculateTotalTurns();
		calculateTimeTotalsAndAverages();
		statStorage.set(10, DeveloperModePane.getWordCount());
	}
	
	//Resets all stat storage attributes, this is needed because this is a static class and it is possible that multiple game 
	//"instances" are created during a single runtime if player plays chess multiple times
	public static void reset() {
		lightTurnTimes.clear();
		darkTurnTimes.clear();
		statStorage.clear();
		tempTurnTimeStart = 0l;
		tempTurnTimeEnd = 0l;
		fillStorageWithZeros();
	}
	
	//Prints out all stats
	public static String staticToString() {
		String output = "";
		int statTitalIndex = 0;
		for(Object stat : statStorage) {
			output += STAT_TITLES[statTitalIndex] + ": " + stat + "\n";
			statTitalIndex++;
		}
		return output;
	}

}
