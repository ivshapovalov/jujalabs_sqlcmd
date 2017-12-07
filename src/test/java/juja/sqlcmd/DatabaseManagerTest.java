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
    public void connectWhenValidAllParametersReturnsTrue() {
        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidDatabaseNameReturnsFalse() {
        assertFalse(databaseManager.connect("Notsqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserNameReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "Notsqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWhenInvalidUserPasswordReturnsFalse() {
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "Notsqlcmd"));
    }
}
