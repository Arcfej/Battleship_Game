/**
 * Represents a position in the battlefield.
 */
public class Position {

    /**
     * The row-index of the position
     */
    private int row;

    /**
     * The column-index of the position
     */
    private int column;

    /**
     * Default constructor of the class
     *
     * @param position The String representation of the position on the Battlefield.
     *                 Its first character should be the column with a letter, not case sensitive.
     *                 The second (or second and third) character(s) should be a number.
     * @throws IllegalArgumentException if the position argument not in the correct format (e.g. 'A8')
     */
	public Position(String position) throws IllegalArgumentException {
		position = position.toUpperCase();
		char[] coordinates = position.toCharArray();
		if (validatePosition(coordinates)) {
			column = calculateColumn(coordinates);
			row = calculateRow(coordinates);
		} else {
			throw new IllegalArgumentException("Illegal argument for position: " + position);
		}
	}

	/**
	 * 'A' = 1, not 0
	 *
	 * @param i
	 * @return
	 */
	public static char calculateColumnIndexFromInteger(int i) {
		return (char) (i + 'A' - 1);
	}

    /**
     * Validate if the input is in the correct format.
     *
     * @param input to validate
     * @return true if the input is correctly validated.
     */
	private boolean validatePosition(char[] input) {
		return validateInputLength(input) && validateRow(input) && validateColumn(input);
	}

    /**
     * Validate if the input's length is correct.
     *
     * @param input to validate.
     * @return true if the length is within the appropriate limits.
     */
	private boolean validateInputLength(char[] input) {
		return input.length >= 2 && input.length <= 3;
	}

    /**
     * Validate the character which represents the column-index of the position.
     *
     * @param input to validate.
     * @return true if the column-index is correct.
     */
	private boolean validateColumn(char[] input) {
		if (!validateInputLength(input)) return false;
		int column = calculateColumn(input);
		return column > 0 && column <= Menu.NUMBER_OF_COLUMNS;
	}

    /**
     * Validate the character(s) which represents the row-index of the position.
     *
     * @param input to validate
     * @return true if the row-index is correct.
     */
	private boolean validateRow(char[] input) {
		if (!validateInputLength(input)) return false;
		int row;
		try {
			row = calculateRow(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return row > 0 && row <= Menu.NUMBER_OF_ROWS;
	}

    /**
     * Calculate the row-index from the character which represents it.
     *
     * @param input to calculate from
     * @return the row-index of the position.
     */
	private int calculateColumn(char[] input) {
		return input[0] - 'A' + 1;
	}

    /**
     * Calculate the column-index from the character(s) which represents them.
     *
     * @param input to calculate from
     * @return the column-index of the position.
     */
	private int calculateRow(char[] input) {
		try {
			return Integer.parseInt(new String(input, 1, input.length - 1));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

    /**
     * @return the row-index of the position.
     */
	public int getRow() {
		return row;
	}

    /**
     * @return the column-index of the position.
     */
	public int getColumn() {
		return column;
	}
}
