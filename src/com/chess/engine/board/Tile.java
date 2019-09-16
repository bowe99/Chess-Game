package com.chess.engine.board;


import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

public abstract class Tile {	//Abstract because I don't want to be able to instantiate the Tile class only the concrete Tile classes
	
	protected final int tileCoordinate;	//Can only be set during construction of the object
	
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = creatAllPossibleEmptyTiles();
	
	private static Map<Integer, EmptyTile> creatAllPossibleEmptyTiles() {
		
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		
		return ImmutableMap.copyOf(emptyTileMap);	// Can only be used once ImmutableMap comes from the Guava library
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	/**
	 * Tile constructor
	 * @param tileCoordinate location of the tile on the board
	 */
	private Tile(int tileCoordinate){
		this.tileCoordinate = tileCoordinate;
	}
	
	/**
	 * Determines if the called upon tile object is occupied by a chess piece
	 * @return boolean value determined by presence of piece on the tile
	 */
	public abstract boolean isTileOccupied();
	
	/**
	 * Gets the type of piece that is on the called upon Tile
	 * @return Piece object 
	 */
	public abstract Piece getPiece();
	
	/**
	 * Class for the empty tiles
	 * @author Kevin
	 *
	 */
	public static final class EmptyTile extends Tile{	//Static because I didn't want to put the class in a different file 
		
		private EmptyTile(final int coordinate){	//Once an empty tile is created the coordinate can't be changed
			super(coordinate);
		}
		
		@Override
		public String toString() {
			return "-";
		}
		
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		public Piece getPiece() {
			return null;
		}
	}
	
	/**
	 * Class for tiles that will be occupied by pieces
	 * @author Kevin
	 *
	 */
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOnTile; //Not accessible by anything other than getPice()
		
		private OccupiedTile( int tileCoordinate, final Piece pieceOnTile){
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
				getPiece().toString();
		}
		
		@Override
		public boolean isTileOccupied() {
			return true;
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
	}
	
}
