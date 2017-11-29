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
    public void testConnectionWithWrongDatabase() {
        assertFalse(databaseManager.connect("Notsqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectionWithWrongUser() {
        assertFalse(databaseManager.connect("sqlcmd", "Notsqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectionWithWrongPassword() {
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "Notsqlcmd"));
    }
}
