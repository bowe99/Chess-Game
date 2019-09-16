package com.chess.engine.board;

public class BoardUtils {

	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
	
	public static final boolean[] SECOND_ROW = initRow(8);
	public static final boolean[] SEVENTH_ROW = initRow(48);
	
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;
	private BoardUtils() {
		throw new RuntimeException("You can't instantiate a board utility");
	}
	
	private static boolean[] initColumn(int columnNumber) {
		
		final boolean[] column = new boolean[NUM_TILES];
		
		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
			
		}while(columnNumber < NUM_TILES);
		
		return column;
	}
	
	private static boolean[] initRow(int tileID) {
		
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[tileID] = true;
			tileID++;
			
		}while(tileID % NUM_TILES_PER_ROW != 0);
		
		return row;
	}

	public static boolean isValidTileCoordinate(int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}

}