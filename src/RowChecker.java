import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowChecker implements SudokuChecker {

    private final int rowIndex; // 0-based
    private final int[][] board;
    private final Map<Integer, List<Integer>> duplicatePositions = new HashMap<>();

    public RowChecker(int rowIndex, int[][] board) {
        this.rowIndex = rowIndex;
        this.board = board;
    }

    @Override
    public void check() {
        Map<Integer, List<Integer>> positions = new HashMap<>();

        for (int col = 0; col < 9; col++) {
            int value = board[rowIndex][col];
            positions.computeIfAbsent(value, k -> new ArrayList<>()).add(col + 1); // 1-based column index
        }

        for (Map.Entry<Integer, List<Integer>> entry : positions.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicatePositions.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public int getRowNumber() {
        return rowIndex + 1; // 1-based
    }

    public Map<Integer, List<Integer>> getDuplicatePositions() {
        return duplicatePositions;
    }
}
