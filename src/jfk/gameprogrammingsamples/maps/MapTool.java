package jfk.gameprogrammingsamples.maps;

import java.awt.Point;
import java.util.*;

public class MapTool {

	public static enum Direction{UP , RIGHT , DOWN , LEFT };
	public static final Point UpDeltas = new Point(0,-1),RightDeltas = new Point(1,0), DownDeltas = new Point(0,1), LeftDeltas = new Point(-1,0); 
	private MapTool () {}
	
	public static List<Point> getNeighborsInsideMapNSEW(int[][] map, Point tileToCheck) {
		List<Point> neighbors = get4NeighborsNSEW(tileToCheck);
		removeTilesOutsideMap(map, neighbors);
		return neighbors;
	}
	
	public static List<Point> get8NeighborsInsideMap(int[][] map, Point tileToCheck) {
		List<Point> neighbors = get8Neighbors(tileToCheck);
		removeTilesOutsideMap(map, neighbors);
		return neighbors;
	}
	
	public static void removeTilesOutsideMap(int[][] tiles, List<Point> neighbors) {
		for (int tileIndex = neighbors.size() - 1; tileIndex >= 0; tileIndex--) {
			if (isOutside(tiles, neighbors.get(tileIndex))) {
				neighbors.remove(tileIndex);
			}
		}
	}

	public static boolean isBorderTile(int[][] tiles, Point pointToCheck) {

		return (pointToCheck.x == 0 || pointToCheck.y == 0 || pointToCheck.x == tiles.length - 1
				|| pointToCheck.y == tiles[0].length - 1);
	}
	
	public static boolean isOutside(int[][] tiles, Point pointToCheck) {
		return (pointToCheck.x < 0 || pointToCheck.y < 0 || pointToCheck.x >= tiles.length 
				|| pointToCheck.y >= tiles[0].length );
	}

	public static List<Point> get4NeighborsNSEW(int column, int row) {
		return get4NeighborsNSEW(new Point(column, row));
	}
	
	public static List<Point> get4NeighborsNSEW(Point pointToGetNeighborsFor) {
		List<Point> neighbors = new ArrayList<>();
		neighbors.add(new Point(pointToGetNeighborsFor.x, pointToGetNeighborsFor.y - 1));
		neighbors.add(new Point(pointToGetNeighborsFor.x + 1, pointToGetNeighborsFor.y));
		neighbors.add(new Point(pointToGetNeighborsFor.x, pointToGetNeighborsFor.y + 1));
		neighbors.add(new Point(pointToGetNeighborsFor.x - 1, pointToGetNeighborsFor.y));
		return neighbors;
	}

	public static List<Point> get4DiagonalNeighbors(Point pointToGetNeighborsFor) {
		List<Point> diagonalNeighbors = new ArrayList<Point>();
		diagonalNeighbors.add(new Point(pointToGetNeighborsFor.x - 1, pointToGetNeighborsFor.y - 1));
		diagonalNeighbors.add(new Point(pointToGetNeighborsFor.x + 1, pointToGetNeighborsFor.y + 1));
		diagonalNeighbors.add(new Point(pointToGetNeighborsFor.x - 1, pointToGetNeighborsFor.y + 1));
		diagonalNeighbors.add(new Point(pointToGetNeighborsFor.x + 1, pointToGetNeighborsFor.y - 1));
		return diagonalNeighbors;
	}
	
	public static List<Point> get8Neighbors(Point pointToGetNeighborsFor) {
		List<Point> neighbors = get4NeighborsNSEW(pointToGetNeighborsFor);
		neighbors.add(new Point(pointToGetNeighborsFor.x - 1, pointToGetNeighborsFor.y - 1));
		neighbors.add(new Point(pointToGetNeighborsFor.x + 1, pointToGetNeighborsFor.y + 1));
		neighbors.add(new Point(pointToGetNeighborsFor.x - 1, pointToGetNeighborsFor.y + 1));
		neighbors.add(new Point(pointToGetNeighborsFor.x + 1, pointToGetNeighborsFor.y - 1));
		return neighbors;
	}
}