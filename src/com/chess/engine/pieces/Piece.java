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
		  final PieceType pieceType,
		  final boolean isFirstMove){
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = isFirstMove;
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
	
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public abstract Piece movePiece(Move move);
	
	public enum PieceType{
		
		PAWN("P", 100) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		}, 
		KNIGHT("H", 300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B", 300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R", 500) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q", 900) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K", 10000) {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue){
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		public int getPieceValue() {
			return this.pieceValue;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		public abstract boolean isRook();
	}
	
	
}
