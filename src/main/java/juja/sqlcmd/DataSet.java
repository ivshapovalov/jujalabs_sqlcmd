package juja.sqlcmd;

import java.util.Arrays;

public class DataSet {
    private String[] tableRow;
    private int size;

    public DataSet(int size) {
        if (size < 0) {
            String message = String.format("Size of DataSet can't be less than 0, current value: '%s'.", size);
            throw new IllegalArgumentException(message);
        }
        this.size = size;
        tableRow = new String[size];
    }

    public void insertValue(int columnIndex, String value) {
        if (columnIndex >= size || columnIndex < 0) {
            String message = String.format("Column index should be between 0 and %s-1", size);
            throw new IllegalArgumentException(message);
        }
        tableRow[columnIndex] = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSet)) return false;
        DataSet dataSet = (DataSet) o;
        return size == dataSet.size && Arrays.equals(tableRow, dataSet.tableRow);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(tableRow);
        result = 31 * result + size;
        return result;
    }
}
