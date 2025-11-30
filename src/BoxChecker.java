import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxChecker implements SudokuChecker {

    // boxIndex: 0..8 (left-to-right, top-to-bottom)
    private final int boxIndex;
    private final int[][] board;
    private final Map<Integer, List<Integer>> duplicatePositions = new HashMap<>();

    public BoxChecker(int boxIndex, int[][] board) {
        this.boxIndex = boxIndex;
        this.board = board;
    }

    @Override
    public void check() {
        Map<Integer, List<Integer>> positions = new HashMap<>();

        int boxRow = boxIndex / 3;
        int boxCol = boxIndex % 3;
        int startRow = boxRow * 3;
        int startCol = boxCol * 3;

        int pos = 0; // position inside box (0..8)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                pos++;
                int value = board[startRow + r][startCol + c];
                positions.computeIfAbsent(value, k -> new ArrayList<>()).add(pos); // position 1..9
            }
        }

        for (Map.Entry<Integer, List<Integer>> entry : positions.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicatePositions.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public int getBoxNumber() {
        return boxIndex + 1; // 1-based (1..9)
    }

    public Map<Integer, List<Integer>> getDuplicatePositions() {
        return duplicatePositions;
    }
}
