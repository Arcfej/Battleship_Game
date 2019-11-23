
public class Position {

	public static final int NUMBER_OF_ROWS = 10;
	public static final int NUMBER_OF_COLUMNS = 10;

	private int row;
	private int column;

	public Position(String position) throws IllegalArgumentException {
		position = position.toUpperCase();
		char[] coordinates = position.toCharArray();
		if (validatePosition(coordinates)) {
			row = calculateRow(coordinates);
			column = calculateColumn(coordinates);
		} else {
			throw new IllegalArgumentException("Please use a valid coordinate as argument for the Position class, like 'B10' or 'b10'.\nCoordinates are valid from A - j or a - j and 1 - 10 .\nYou can validate the argument with the validatePosition() method.");
		}
	}

	private boolean validatePosition(char[] input) {
		return validateInputLength(input) && validateColumn(input) && validateRow(input);
	}

	private boolean validateInputLength(char[] input) {
		return input.length >= 2 && input.length <= 3;
	}

	private boolean validateRow(char[] input) {
		if (!validateInputLength(input)) return false;
		int row = calculateRow(input);
		return row >= 0 && 9 >= row;
	}

	private boolean validateColumn(char[] input) {
		if (!validateInputLength(input)) return false;
		int column;
		try {
			column = calculateColumn(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return column >= 0 && column <= 10;
	}

	private int calculateRow(char[] input) {
		return input[0] - 'A';
	}

	private int calculateColumn(char[] input) {
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
