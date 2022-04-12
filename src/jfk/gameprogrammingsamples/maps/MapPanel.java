package jfk.gameprogrammingsamples.maps;

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int columns, rows, tileSize;
	private java.util.List<Color> tileColors = new ArrayList<Color>();
	private int[][] map;
	private Random random = new Random();

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public java.util.List<Color> getTileColors() {
		return tileColors;
	}

	public void setTileColors(java.util.List<Color> tileColors) {
		this.tileColors = tileColors;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public MapPanel(int columns, int rows, int tileSize, java.util.List<Color> tileColors) {
		super();
		this.setColumns(columns);
		this.setRows(rows);
		this.setTileSize(tileSize);
		this.setTileColors(tileColors);
		this.setPreferredSize(new Dimension(columns * tileSize, rows * tileSize));
		this.setMinimumSize(new Dimension(columns * tileSize, rows * tileSize));
		this.setMap(new int[columns][rows]);
		//createRandomMap();
	}

	public void createRandomMap() {
		for (int columnCounter = 0; columnCounter < getColumns(); columnCounter++) {
			for (int rowCounter = 0; rowCounter < getRows(); rowCounter++) {
				map[columnCounter][rowCounter] = random.nextInt(tileColors.size());
			}
		}
	}

	public void setTile(int column, int row, int value) {
		getMap()[column][row] = value;
	}

	public void setTile(Point position, int value) {
		getMap()[position.x][position.y] = value;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawMap(g);
	}

	private void drawMap(Graphics g) {
		for (int columnCounter = 0; columnCounter < columns; columnCounter++) {
			for (int rowCounter = 0; rowCounter < rows; rowCounter++) {
				g.setColor(getTileColors().get(map[columnCounter][rowCounter]));
				int xPositionForTile = columnCounter * tileSize;
				int yPositionForTile = rowCounter * tileSize;
				g.fillRect(xPositionForTile, yPositionForTile, tileSize, tileSize);
			}
		}
	}

}