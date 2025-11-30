import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnChecker implements SudokuChecker {

    private final int colIndex; // 0-based
    private final int[][] board;
    private final Map<Integer, List<Integer>> duplicatePositions = new HashMap<>();

    public ColumnChecker(int colIndex, int[][] board) {
        this.colIndex = colIndex;
        this.board = board;
    }

    @Override
    public void check() {
        Map<Integer, List<Integer>> positions = new HashMap<>();

        for (int row = 0; row < 9; row++) {
            int value = board[row][colIndex];
            positions.computeIfAbsent(value, k -> new ArrayList<>()).add(row + 1); // 1-based row index
        }

        for (Map.Entry<Integer, List<Integer>> entry : positions.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicatePositions.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public int getColumnNumber() {
        return colIndex + 1; // 1-based
    }

    public Map<Integer, List<Integer>> getDuplicatePositions() {
        return duplicatePositions;
    }
}
