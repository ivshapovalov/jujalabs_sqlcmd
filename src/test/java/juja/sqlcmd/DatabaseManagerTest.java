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
    public void testConnect() {

        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void testConnectIfDatabaseNotExist() {

        assertFalse(databaseManager.connect("noDatabase", "sqlcmd", "sqlcmd"));
    }
    @Test
    public void testConnectIfWrongUser() {

        assertFalse(databaseManager.connect("sqlcmd", "wrongUser", "sqlcmd"));
    }
    @Test
    public void testConnectIfWrongPassword() {

        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "wrongPassword"));
    }
}
