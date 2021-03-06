package loon.action.map;

import java.util.StringTokenizer;

import loon.BaseIO;
import loon.LSystem;
import loon.utils.CollectionUtils;
import loon.utils.StringUtils;
import loon.utils.TArray;

public class TileMapConfig {

	private int[][] backMap;

	public int[][] getBackMap() {
		return backMap;
	}

	public void setBackMap(int[][] backMap) {
		this.backMap = backMap;
	}

	public static Field2D loadCharsField(String resName, int tileWidth,
			int tileHeight) {
		Field2D field = new Field2D(loadCharsMap(resName), tileWidth,
				tileHeight);
		return field;
	}

	public static int[][] loadCharsMap(String resName) {
		int[][] map = null;
		String result = BaseIO.loadText(resName);
		if (result == null) {
			return map;
		}
		StringTokenizer br = new StringTokenizer(result, "\r\n");
		String line = br.nextToken();
		int width = Integer.parseInt(line);
		line = br.nextToken();
		int height = Integer.parseInt(line);
		map = new int[width][height];
		for (int i = 0; i < width; i++) {
			line = br.nextToken();
			for (int j = 0; j < height; j++) {
				map[i][j] = line.charAt(j);
			}
		}
		return map;
	}

	public static TArray<int[]> loadList(final String fileName) {
		String result = BaseIO.loadText(fileName);
		if (result == null) {
			return null;
		}
		StringTokenizer br = new StringTokenizer(result, "\r\n");
		TArray<int[]> records = new TArray<int[]>(
				CollectionUtils.INITIAL_CAPACITY);
		for (; br.hasMoreTokens();) {
			result = StringUtils.replace(br.nextToken().trim(), LSystem.LS, "");
			if (!StringUtils.isEmpty(result)) {
				String[] stringArray = result.split(",");
				int size = stringArray.length;
				int[] intArray = new int[size];
				for (int i = 0; i < size; i++) {
					intArray[i] = Integer.parseInt(stringArray[i]);
				}
				records.add(intArray);
			}
		}
		return records;
	}

	public static int[][] reversalXandY(final int[][] array) {
		int col = array[0].length;
		int row = array.length;
		int[][] result = new int[col][row];
		for (int y = 0; y < col; y++) {
			for (int x = 0; x < row; x++) {
				result[y][x] = array[x][y];
			}
		}
		return result;
	}

	public static int[][] loadAthwartArray(final String fileName) {
		TArray<int[]> list = loadList(fileName);
		int col = list.size;
		int[][] result = new int[col][];
		for (int i = 0; i < col; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	public static int[][] loadJustArray(final String fileName) {
		TArray<int[]> list = loadList(fileName);
		int col = list.size;
		int[][] mapArray = new int[col][];
		for (int i = 0; i < col; i++) {
			mapArray[i] = list.get(i);
		}
		int row = ((mapArray[col > 0 ? col - 1 : 0]).length);
		int[][] result = new int[row][col];
		for (int y = 0; y < col; y++) {
			for (int x = 0; x < row; x++) {
				result[x][y] = mapArray[y][x];
			}
		}
		return result;
	}

	public static int[][] stringToIntArrays(String srcStr) {
		int[][] resArr = null;
		if ((srcStr == null) || (srcStr.length() == 0)) {
			return resArr;
		}
		try {
			String[] strLns = StringUtils.split(srcStr, '\n');
			resArr = new int[strLns.length][];
			for (int i = 0; i < strLns.length; i++) {
				String[] strPrms = StringUtils.split(strLns[i], ',');
				resArr[i] = new int[strPrms.length];
				for (int j = 0; j < strPrms.length; j++) {
					resArr[i][j] = stingToInt(strPrms[j]);
				}
			}
		} catch (Exception ex) {
		}
		return resArr;
	}

	private static int stingToInt(String srcStr) {
		int resNo = 0;
		try {
			resNo = Integer.parseInt(srcStr);
		} catch (Exception ex) {
		}
		return resNo;
	}

	public static String[][] stringToStringArrays(String srcStr) {
		String[][] resArr = (String[][]) null;
		if ((srcStr == null) || (srcStr.length() == 0))
			return resArr;
		try {
			String[] strLns = StringUtils.split(srcStr, '\n');
			resArr = new String[strLns.length][];
			for (int i = 0; i < strLns.length; i++) {
				String[] strPrms = StringUtils.split(strLns[i], ',');
				resArr[i] = new String[strPrms.length];
				for (int j = 0; j < strPrms.length; j++) {
					resArr[i][j] = strPrms[j];
				}
			}
		} catch (Exception ex) {
		}
		return resArr;
	}

}
