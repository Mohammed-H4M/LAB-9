import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SudokuIO {

    public static int[][] loadBoardFromCsv(String path) throws IOException {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // ignore empty lines

                // Support comma / semicolon / whitespace as separators
                String[] parts = line.split("[,;\\s]+");
                if (parts.length != 9) {
                    throw new IllegalArgumentException("Each row must contain 9 values. Offending line: " + line);
                }

                int[] row = new int[9];
                for (int i = 0; i < 9; i++) {
                    int val = Integer.parseInt(parts[i]);
                    if (val < 1 || val > 9) {
                        throw new IllegalArgumentException("Values must be in range 1-9. Offending value: " + val);
                    }
                    row[i] = val;
                }
                rows.add(row);
            }
        }

        if (rows.size() != 9) {
            throw new IllegalArgumentException("Board must have exactly 9 rows.");
        }

        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            board[i] = rows.get(i);
        }

        return board;
    }
}
