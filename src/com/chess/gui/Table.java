package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

public class Table {
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	public Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	private boolean visibleLegals;
	
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(1200, 1200);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(800, 700);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(20, 20);
	private static String defaultPieceImagesPath = "art/simple/";
	
	private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
	
	public Table() {
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.visibleLegals = true;
		
		this.chessBoard = Board.createStandardBoard();
		
		this.boardDirection = BoardDirection.NORMAL;
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		
		this.gameFrame.setVisible(true);
		
		
		
		
	}
	
	
	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferenceMenu());
		return tableMenuBar; 
	}


	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		
		final JMenuItem openPGN = new JMenuItem("Load PGM file");
		openPGN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open up that pgn file");
			}
			
		});
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);
		
		return fileMenu;
	}
	
	private JMenu createPreferenceMenu() {
		
		final JMenu preferenceMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
			
		});
		preferenceMenu.add(flipBoardMenuItem);
		
		preferenceMenu.addSeparator();
		
		final JMenuItem visibleLegalMoves = new JMenuItem("Enable/Disable Visible Legal Moves");
		visibleLegalMoves.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(visibleLegals) {
					visibleLegals = false;
				}
				else {
					visibleLegals = true;
				}
			}
			
		});
		preferenceMenu.add(visibleLegalMoves);
		
		return preferenceMenu;
	}
	
	public enum BoardDirection{
		NORMAL{

			@Override
			public List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return boardTiles;
			}

			@Override
			public BoardDirection opposite() {
				return FLIPPED;
			}
			
		}, 
		FLIPPED {
			@Override
			public List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}

			@Override
			public BoardDirection opposite() {
				return NORMAL;
			}
		};
		
		public abstract List<TilePanel> traverse(List<TilePanel> boardTiles);
		public abstract BoardDirection opposite();
	}
	
	
	private class BoardPanel extends JPanel{
		
		final List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridLayout(8, 8));
			this.boardTiles = new ArrayList<>();
			
			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(final Board chessBoardToDraw) {
			removeAll();
			for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(chessBoardToDraw);
				add(tilePanel);
			}
			validate();
			repaint();
		}
		
	}
	
	private class TilePanel extends JPanel{
		
		private final int tileID;
		
		TilePanel(final BoardPanel boardPanel, 
				  final int tileID){
			super(new GridBagLayout());
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTilePieceIcon(chessBoard);
			assignTileColor();
			
			addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
					
					if(SwingUtilities.isRightMouseButton(arg0)) {
						
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
						
						
					}
					else if (SwingUtilities.isLeftMouseButton(arg0)) {
						
						if(sourceTile == null) {
							//first click
							sourceTile = chessBoard.getTile(tileID);
							humanMovedPiece = sourceTile.getPiece();
							if(humanMovedPiece == null) {
								sourceTile = null;
							}
							
						}
						else {
							//second click
							destinationTile = chessBoard.getTile(tileID);
							//TODO fill in under
							final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getBoard();
								//TODO add the move to the move log
							}
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
							
						}
						
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								boardPanel.drawBoard(chessBoard);
							}
							
						});
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			
			
			validate();
		}

		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
			validate();
			repaint();
		}
		
		
		private void highlightLegals(final Board board) {
			if(visibleLegals) {
				for(final Move move : pieceLegalMoves(board)) {
					if(move.getDestinationCoordinate() == this.tileID) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board){
			if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}

		private void assignTileColor() {
			if(BoardUtils.EIGHTH_RANK[this.tileID] ||
					BoardUtils.SIXTH_RANK[this.tileID] ||
					BoardUtils.FOURTH_RANK[this.tileID] ||
					BoardUtils.SECOND_RANK[this.tileID]) {
				setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
			}
			else if(BoardUtils.SEVENTH_RANK[this.tileID] ||
					BoardUtils.FIFTH_RANK[this.tileID] ||
					BoardUtils.THIRD_RANK[this.tileID] ||
					BoardUtils.FIRST_RANK[this.tileID]) {
				setBackground(this.tileID % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
		
		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileID).isTileOccupied()) {
				try {
					final BufferedImage image = 
							ImageIO.read(new File(defaultPieceImagesPath + 
									board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1) + 
									board.getTile(this.tileID).getPiece().toString() +
									".gif"));
					add(new JLabel(new ImageIcon(Table.resize(image, 60, 60))));
					
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
