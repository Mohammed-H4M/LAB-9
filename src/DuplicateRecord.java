import java.util.List;

public class DuplicateRecord {

    private final String type; // "ROW", "COL", "BOX"
    private final int index;   // 1..9
    private final int digit;   // 1..9
    private final List<Integer> locations; // indices 1..9 (columns, rows, or box positions)

    public DuplicateRecord(String type, int index, int digit, List<Integer> locations) {
        this.type = type;
        this.index = index;
        this.digit = digit;
        this.locations = locations;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getDigit() {
        return digit;
    }

    public List<Integer> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return String.format("%s %d, #%d, %s", type, index, digit, locations.toString());
    }
}
