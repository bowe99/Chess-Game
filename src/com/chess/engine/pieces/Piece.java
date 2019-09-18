package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cacheHashCode;
	
	Piece(final int piecePosition, 
		  final Alliance pieceAlliance,
		  final PieceType pieceType){
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		//TODO more work
		this.isFirstMove = false;
		this.pieceType = pieceType;
		this.cacheHashCode = computeHashCode();
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return pieceAlliance == otherPiece.getPieceAlliance() && pieceType == otherPiece.getPieceType() &&
			   piecePosition == otherPiece.getPiecePosition() && isFirstMove == otherPiece.isFirstMove();	
				
	}
	
	@Override 
	public int hashCode() {
		return this.cacheHashCode;
	}
	
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}
	
	public int getPiecePosition() {
		return this.piecePosition;
	}
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public abstract Piece movePiece(Move move);
	
	public enum PieceType{
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				return false;
			}
		}, 
		KNIGHT("H") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return true;
			}
		};
		
		private String pieceName;
		
		PieceType(String pieceName){
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public abstract boolean isKing();
	}
	
	
}
