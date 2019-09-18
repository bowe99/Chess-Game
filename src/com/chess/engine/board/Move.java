package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;

public abstract class Move {
	
	
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
		this.board = board;
		this.destinationCoordinate = destinationCoordinate;
		this.movedPiece = movedPiece;
	}
	
	public static final class MajorMove extends Move{

		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
			
		}

		@Override
		public Board execute() {
			
			final Builder builder = new Builder();
			for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
				//TO-DO hashcode and equals for pieces
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			//move the moved piece
			builder.setMoveMaker(null);
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();

		}
	}
	
	public static final class AttackMove extends Move{
		
		final Piece attackedPiece;
		
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public Board execute() {
			return null;
		}
		
	}

	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}

	public abstract Board execute();
}
