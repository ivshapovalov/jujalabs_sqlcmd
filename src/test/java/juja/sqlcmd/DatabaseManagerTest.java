package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabaseManagerTest {
    DatabaseManager databaseManager;

    @Before
    public void init() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testSuccessfulConnection() {
        boolean expected = databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd");
        assertEquals(expected, true);
    }

    @Test
    public void testUnSuccessfulConnection() {
        boolean expected = databaseManager.connect("NotSqlcmd", "sqlcmd", "sqlcmd");
        assertEquals(expected, false);
    }
}
