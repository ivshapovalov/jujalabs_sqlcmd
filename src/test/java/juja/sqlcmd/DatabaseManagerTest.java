package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {
    private DatabaseManager databaseManager;

    @Before
    public void init() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testCorrectConnection() {
        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectionWithWrongData() {
        assertFalse(databaseManager.connect("Notsqlcmd", "sqlcmd", "sqlcmd"));
        assertFalse(databaseManager.connect("sqlcmd", "Notsqlcmd", "sqlcmd"));
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "Notsqlcmd"));
    }
}
