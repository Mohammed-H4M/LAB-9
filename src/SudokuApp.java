import java.io.IOException;

public class SudokuApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <sudoku.csv> <mode>");
            System.err.println("mode = 0 (sequential), 3 (rows/cols/boxes threads), 27 (one per row/col/box)");
            System.exit(1);
        }

        String csvPath = args[0];
        int mode;
        try {
            mode = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Mode must be 0, 3, or 27.");
            return;
        }

        try {
            int[][] board = SudokuIO.loadBoardFromCsv(csvPath);
            SudokuValidator validator = new SudokuValidator(board, mode);
            boolean valid = validator.validate();
            validator.printReport(valid);
        } catch (IllegalArgumentException ex) {
            System.err.println("Input error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Failed to read file: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.err.println("Validation interrupted: " + ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
