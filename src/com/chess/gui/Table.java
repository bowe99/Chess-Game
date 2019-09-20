package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.chess.engine.board.BoardUtils;

public class Table {
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
	
	private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
	
	public Table() {
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		
		this.gameFrame.setVisible(true);
		
		
		
		
	}
	
	
	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
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
		
	}
	
	private class TilePanel extends JPanel{
		
		private final int tileID;
		
		TilePanel(final BoardPanel boardPanel, 
				  final int tileID){
			super(new GridBagLayout());
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			validate();
		}

		private void assignTileColor() {
			if(BoardUtils.FIRST_ROW[this.tileID] ||
					BoardUtils.THIRD_ROW[this.tileID] ||
					BoardUtils.FIFTH_ROW[this.tileID] ||
					BoardUtils.SEVENTH_ROW[this.tileID]) {
				setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
			}
			else if(BoardUtils.SECOND_ROW[this.tileID] ||
					BoardUtils.FOURTH_ROW[this.tileID] ||
					BoardUtils.SIXTH_ROW[this.tileID] ||
					BoardUtils.EIGHTH_ROW[this.tileID]) {
				setBackground(this.tileID % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}