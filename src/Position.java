
public class Position {

	public static final int NUMBER_OF_ROWS = 10;
	public static final int NUMBER_OF_COLUMNS = 10;

	private int row;
	private int column;

	public Position(String position) throws IllegalArgumentException {
		position = position.toUpperCase();
		char[] coordinates = position.toCharArray();
		if (validatePosition(coordinates)) {
			column = calculateColumn(coordinates);
			row = calculateRow(coordinates);
		} else {
			throw new IllegalArgumentException("Please use a valid coordinate as argument for the Position class, like 'B10' or 'b10'.\nCoordinates are valid from A - j or a - j and 1 - 10 .\nYou can validate the argument with the validatePosition() method.");
		}
	}

	private boolean validatePosition(char[] input) {
		return validateInputLength(input) && validateRow(input) && validateColumn(input);
	}

	private boolean validateInputLength(char[] input) {
		return input.length >= 2 && input.length <= 3;
	}

	private boolean validateColumn(char[] input) {
		if (!validateInputLength(input)) return false;
		int column = calculateColumn(input);
		return column > 0 && column <= NUMBER_OF_COLUMNS;
	}

	private boolean validateRow(char[] input) {
		if (!validateInputLength(input)) return false;
		int row;
		try {
			row = calculateRow(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return row > 0 && row <= NUMBER_OF_ROWS;
	}

	private int calculateColumn(char[] input) {
		return input[0] - 'A' + 1;
	}

	private int calculateRow(char[] input) {
		try {
			return Integer.parseInt(new String(input, 1, input.length - 1));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
