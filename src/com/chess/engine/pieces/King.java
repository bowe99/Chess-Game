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

public class King extends Piece{
	
	private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

	
	public King(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.KING);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
			final int currentCandidateDestination = this.piecePosition + currentCandidate;
			
			if (BoardUtils.isValidTileCoordinate(currentCandidateDestination)) {
				final Tile candidateDestinationTile = board.getTile(currentCandidateDestination);
				
				if(isFirstColumnExclusion(this.piecePosition, currentCandidateDestination) ||
				   isEighthColumnExclusion(this.piecePosition, currentCandidateDestination)) {
					continue;
				}
				
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new Move.MajorMove(board, this, currentCandidateDestination));
				}
				else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					
					if(this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new Move.AttackMove(board, this, currentCandidateDestination, pieceAtDestination));
					}
				}
			}
			
		}
		
		
		
		
		
		
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
	}
	
	public King movePiece(Move move) {
		return new King( move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

}
