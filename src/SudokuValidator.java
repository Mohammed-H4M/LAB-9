import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SudokuValidator {

    private final int[][] board;
    private final int mode;

    private final List<DuplicateRecord> rowIssues = new ArrayList<>();
    private final List<DuplicateRecord> colIssues = new ArrayList<>();
    private final List<DuplicateRecord> boxIssues = new ArrayList<>();

    public SudokuValidator(int[][] board, int mode) {
        this.board = board;
        this.mode = mode;
    }

    public boolean validate() throws InterruptedException {
        switch (mode) {
            case 0:
                validateSequential();
                break;
            case 3:
                validateMode3();
                break;
            case 27:
                validateMode27();
                break;
            default:
                throw new IllegalArgumentException("Mode must be 0, 3, or 27.");
        }

        return rowIssues.isEmpty() && colIssues.isEmpty() && boxIssues.isEmpty();
    }

    // ------------------ MODE 0: sequential ------------------

    private void validateSequential() {
        RowChecker[] rowCheckers = new RowChecker[9];
        ColumnChecker[] colCheckers = new ColumnChecker[9];
        BoxChecker[] boxCheckers = new BoxChecker[9];

        for (int i = 0; i < 9; i++) {
            rowCheckers[i] = (RowChecker) CheckerFactory.createChecker("ROW", i, board);
            rowCheckers[i].check();

            colCheckers[i] = (ColumnChecker) CheckerFactory.createChecker("COL", i, board);
            colCheckers[i].check();

            boxCheckers[i] = (BoxChecker) CheckerFactory.createChecker("BOX", i, board);
            boxCheckers[i].check();
        }

        collectAll(rowCheckers, colCheckers, boxCheckers);
    }

    // ------------------ MODE 3: 3 worker threads ------------------

    private void validateMode3() throws InterruptedException {
        RowChecker[] rowCheckers = new RowChecker[9];
        ColumnChecker[] colCheckers = new ColumnChecker[9];
        BoxChecker[] boxCheckers = new BoxChecker[9];

        for (int i = 0; i < 9; i++) {
            rowCheckers[i] = (RowChecker) CheckerFactory.createChecker("ROW", i, board);
            colCheckers[i] = (ColumnChecker) CheckerFactory.createChecker("COL", i, board);
            boxCheckers[i] = (BoxChecker) CheckerFactory.createChecker("BOX", i, board);
        }

        Thread rowsThread = new Thread(() -> {
            for (RowChecker rc : rowCheckers) {
                rc.check();
            }
        });

        Thread colsThread = new Thread(() -> {
            for (ColumnChecker cc : colCheckers) {
                cc.check();
            }
        });

        Thread boxesThread = new Thread(() -> {
            for (BoxChecker bc : boxCheckers) {
                bc.check();
            }
        });

        rowsThread.start();
        colsThread.start();
        boxesThread.start();

        rowsThread.join();
        colsThread.join();
        boxesThread.join();

        collectAll(rowCheckers, colCheckers, boxCheckers);
    }

    // ------------------ MODE 27: 27 worker threads ------------------

    private void validateMode27() throws InterruptedException {
        RowChecker[] rowCheckers = new RowChecker[9];
        ColumnChecker[] colCheckers = new ColumnChecker[9];
        BoxChecker[] boxCheckers = new BoxChecker[9];
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            rowCheckers[i] = (RowChecker) CheckerFactory.createChecker("ROW", i, board);
            Thread tr = new Thread(rowCheckers[i]::check);
            threads.add(tr);

            colCheckers[i] = (ColumnChecker) CheckerFactory.createChecker("COL", i, board);
            Thread tc = new Thread(colCheckers[i]::check);
            threads.add(tc);

            boxCheckers[i] = (BoxChecker) CheckerFactory.createChecker("BOX", i, board);
            Thread tb = new Thread(boxCheckers[i]::check);
            threads.add(tb);
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        collectAll(rowCheckers, colCheckers, boxCheckers);
    }

    // ------------------ Collect & report helpers ------------------

    private void collectAll(RowChecker[] rowCheckers,
                            ColumnChecker[] colCheckers,
                            BoxChecker[] boxCheckers) {

        for (RowChecker rc : rowCheckers) {
            for (Map.Entry<Integer, List<Integer>> e : rc.getDuplicatePositions().entrySet()) {
                rowIssues.add(new DuplicateRecord("ROW", rc.getRowNumber(), e.getKey(), e.getValue()));
            }
        }

        for (ColumnChecker cc : colCheckers) {
            for (Map.Entry<Integer, List<Integer>> e : cc.getDuplicatePositions().entrySet()) {
                colIssues.add(new DuplicateRecord("COL", cc.getColumnNumber(), e.getKey(), e.getValue()));
            }
        }

        for (BoxChecker bc : boxCheckers) {
            for (Map.Entry<Integer, List<Integer>> e : bc.getDuplicatePositions().entrySet()) {
                boxIssues.add(new DuplicateRecord("BOX", bc.getBoxNumber(), e.getKey(), e.getValue()));
            }
        }
    }

    public void printReport(boolean valid) {
        if (valid) {
            System.out.println("VALID");
            return;
        }

        System.out.println("INVALID");

        // Rows
        for (DuplicateRecord rec : rowIssues) {
            System.out.println(rec);
        }
        System.out.println("------------------------------------------");

        // Columns
        for (DuplicateRecord rec : colIssues) {
            System.out.println(rec);
        }
        System.out.println("------------------------------------------");

        // Boxes
        for (DuplicateRecord rec : boxIssues) {
            System.out.println(rec);
        }
    }
}
