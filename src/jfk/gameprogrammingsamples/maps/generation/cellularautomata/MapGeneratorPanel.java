package jfk.gameprogrammingsamples.maps.generation.cellularautomata;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.*;


import jfk.gameprogrammingsamples.maps.MapPanel;
import jfk.gameprogrammingsamples.maps.MapTool;

public class MapGeneratorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static Random rnd = new Random();
	Color greenColor = new Color(0, 230, 30);
	static int tileSize = 8; 		// size of the tiles in the sprite sheet in pixels
	static int rows = 128; // the number of rows in the map
	static int columns = 128; // the number of columns in the map
	int currentXoffset, currentYoffset; // stores how much the map is scrolled left and up
	static String windowTitle = "Cellular automata map generation";
	MapPanel mapPanel;
	JComboBox<GeneratorConfiguration> configurationCombo;

	public static void main(String[] args) {
		MapGeneratorPanel examplePanel = new MapGeneratorPanel(columns, rows, tileSize); 
		JFrame frame = new JFrame(windowTitle); // create a Frame (window)
		frame.setResizable(false); // lock its size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the X button to close the window
		frame.setSize(columns * tileSize, rows * tileSize);
frame.setLocationRelativeTo(null);
		frame.getContentPane().add(examplePanel, BorderLayout.CENTER); // add our panel
		frame.setVisible(true);
	}
	
	private GeneratorConfiguration getSelectedConfiguration() {
		return configurationCombo.getItemAt(configurationCombo.getSelectedIndex());
	}

	public MapGeneratorPanel(int columns, int rows, int tileSize) {

		int controlpanelHeight = 50;
		int minWidth = columns * tileSize;
		int minHeight = rows * tileSize + controlpanelHeight;
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(minWidth, controlpanelHeight));
		controlPanel.setLayout(new BorderLayout());
		JButton startButton = new JButton("Random start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				randomMap();
				repaint();
			}
		});
		controlPanel.add(startButton, BorderLayout.WEST);

		JButton iterationButton = new JButton("Run one iteration");

		iterationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mapPanel.setMap(runIteration(mapPanel.getMap()));
				repaint();
			}

			private int[][] runIteration(int[][] map) {
				int[][] tempMap = new int[columns][rows];

				
				for (int x = 0; x < columns; x++) {
					for (int y = 0; y < columns; y++) {
						int numberOfNeighborLandTiles = getNumberOfLandTileNeighbors(map, x, y);
						if (map[x][y] == 0 && numberOfNeighborLandTiles > getSelectedConfiguration().getConvertToLandAbove()) {
							tempMap[x][y] = 1;
						} else if (map[x][y] == 1 && numberOfNeighborLandTiles < getSelectedConfiguration().getConvertToWaterBelow()) {
							tempMap[x][y] = 0;
						} else {
							tempMap[x][y] = map[x][y];
						}
					}
				}
				return tempMap;
			}

			private int getNumberOfLandTileNeighbors(int[][] map, int x, int y) {
				int landTileCounter = 0;
				for (Point neighbor : MapTool.get8NeighborsInsideMap(map, new Point(x, y))) {
					if (map[neighbor.x][neighbor.y] == 1) {
						landTileCounter++;
					}
				}
				return landTileCounter;
			}
		});
		controlPanel.add(iterationButton, BorderLayout.EAST);

		java.util.List<GeneratorConfiguration> configurations =  Arrays.asList(
				new GeneratorConfiguration("Water world", .3, 3, 5),
				new GeneratorConfiguration("Islands", .3, 2, 4),
				new GeneratorConfiguration("Earthlike", .4, 3, 4),
				new GeneratorConfiguration("Inland", .5, 2, 4),
				new GeneratorConfiguration("1.000 lakes", .5, 2, 5)
				);
		configurationCombo = new  JComboBox<>();
		for(GeneratorConfiguration configuration : configurations) {
			configurationCombo.addItem(configuration);
		}
		configurationCombo.setSelectedIndex(2);
		controlPanel.add(configurationCombo, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(controlPanel, BorderLayout.NORTH); // add our panel
		mapPanel = new MapPanel(columns, rows, tileSize, Arrays.asList(Color.blue, greenColor));
		mapPanel.setPreferredSize(new Dimension(minWidth, minHeight));

		add(mapPanel, BorderLayout.CENTER);
	}
	
	private void randomMap() {
		
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < columns; y++) {
				if (rnd.nextDouble() < getSelectedConfiguration().getInitialLandpercentage()) {
					mapPanel.getMap()[x][y] = 1;
				}
				else {
					mapPanel.getMap()[x][y] = 0;
				}
			}
		}
	}
}