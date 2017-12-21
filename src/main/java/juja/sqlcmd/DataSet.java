package juja.sqlcmd;

import java.util.Arrays;

public class DataSet {

    private String[] oneRow;

    DataSet(int length) {
        if (length < 0) {
            String exceptionMessage = String.format("DataSet length = %s, but should be greater or equal to 0.", length);
            throw new IllegalArgumentException(exceptionMessage);
        }
        this.oneRow = new String[length];
    }

    public void add(int columnIndex, String value) {
        if ((columnIndex >= 0) && (columnIndex < oneRow.length)) {
            this.oneRow[columnIndex] = value;
        } else {
            String exceptionMessage = String.format("Column index = %s, but should be between 0 and %s.",
                    columnIndex, oneRow.length - 1);
            throw new IllegalArgumentException(exceptionMessage);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet dataSet = (DataSet) o;
        return Arrays.equals(oneRow, dataSet.oneRow);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(oneRow);
    }
}
