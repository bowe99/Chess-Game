package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece{
	
	private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};
	
	public Pawn(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance, PieceType.PAWN, true);
	}
	
	public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
			final int currentCandidateDestination = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidate);
			
			if(!BoardUtils.isValidTileCoordinate(currentCandidateDestination)) {
				continue;
			}
			
			if(currentCandidate == 8 && !board.getTile(currentCandidateDestination).isTileOccupied()) {
				
				if(this.pieceAlliance.isPawnPromotionSquare(currentCandidateDestination)) {
					legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, currentCandidateDestination)));
				}
				else {
					legalMoves.add(new Move.PawnMove(board, this, currentCandidateDestination));
				}
			}
			
			else if((currentCandidate == 16) && this.isFirstMove() && 
					!board.getTile(currentCandidateDestination - (this.getPieceAlliance().getDirection() * 8)).isTileOccupied() && //Behind candidate destination
					!board.getTile(currentCandidateDestination).isTileOccupied() &&
					((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.pieceAlliance.isBlack()) ||
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()))) {
				legalMoves.add(new Move.PawnJump(board, this, currentCandidateDestination));
			}
			
			
			else if ((currentCandidate == 7) &&
				!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) || 
				  (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))){
				
				if(board.getTile(currentCandidateDestination).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(currentCandidateDestination).getPiece();
					
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance) {
						if(this.pieceAlliance.isPawnPromotionSquare(currentCandidateDestination)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, currentCandidateDestination, pieceOnCandidate)));
						}
						else {
							legalMoves.add(new Move.PawnAttackMove(board, this, currentCandidateDestination, pieceOnCandidate)); 
						}
					}
				}
					
				else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, currentCandidateDestination, pieceOnCandidate));
						}
					}
				}
				
			
			}
			else if ((currentCandidate == 9) &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) || 
					  (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
					
				if(board.getTile(currentCandidateDestination).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(currentCandidateDestination).getPiece();
					
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance) {
						if(this.pieceAlliance.isPawnPromotionSquare(currentCandidateDestination)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, currentCandidateDestination, pieceOnCandidate)));
						}
						else {
							legalMoves.add(new Move.PawnAttackMove(board, this, currentCandidateDestination, pieceOnCandidate)); 
						}
					}
					
				}
				else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, currentCandidateDestination, pieceOnCandidate));
						}
					}
				}
				
			}
			
		}
		
		
		
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	public Pawn movePiece(Move move) {
		return new Pawn( move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}

	public Piece getPromotionPiece() {
		return new Queen(this.piecePosition, this.pieceAlliance, false);
	}

}
