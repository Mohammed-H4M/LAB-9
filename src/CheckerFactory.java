public class CheckerFactory {

    public static SudokuChecker createChecker(String type, int index, int[][] board) {
        switch (type) {
            case "ROW":
                return new RowChecker(index, board);
            case "COL":
                return new ColumnChecker(index, board);
            case "BOX":
                return new BoxChecker(index, board);
            default:
                throw new IllegalArgumentException("Unknown checker type: " + type);
        }
    }
}
