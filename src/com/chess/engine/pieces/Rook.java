package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Rook extends Piece{

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};
	
	public Rook(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.ROOK, true);
	}

	public Rook(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(piecePosition, pieceAlliance, PieceType.ROOK, isFirstMove);
	}
	
	
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;
			
			
				
			
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { //Checking for valid tile because if tile is occupied then there is no point to continue looking 
				
				
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				
				candidateDestinationCoordinate += candidateCoordinateOffset;
				
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					
					if(!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
					}
					else {
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
						
						if(this.pieceAlliance != pieceAlliance) {
							legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						break;
					}
				}
				
			}
			
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffsetVector) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffsetVector == -1);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffsetVector) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffsetVector == 1);
	}
	
	public Rook movePiece(Move move) {
		return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

}
