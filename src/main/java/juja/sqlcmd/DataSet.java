package juja.sqlcmd;

public class DataSet {
    private String[] tableRow;

    public DataSet(int size) {
        tableRow = new String[size];
    }

    public void insertValue(int columnIndex, String value) {
        tableRow[columnIndex] = value;
    }
}
