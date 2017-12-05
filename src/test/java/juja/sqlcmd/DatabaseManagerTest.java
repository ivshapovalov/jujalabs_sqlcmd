package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {
    private DatabaseManager databaseManager;

    @Before
    public void setup() {
        databaseManager = new DatabaseManager();
    }

    @Test
    public void testConnectionWithCorrectParameters() {
        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectionIfDatabaseNotExists() {
        assertFalse(databaseManager.connect("noDatabase", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectionIfWrongUser() {
        assertFalse(databaseManager.connect("sqlcmd", "wrongUser", "sqlcmd"));
    }

    @Test
    public void testConnectionIfWrongPassword() {
        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "wrongPassword"));
    }
}
