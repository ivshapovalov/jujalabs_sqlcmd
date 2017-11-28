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
    public void connectTest() {

        assertTrue(databaseManager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectIfDatabaseNotExist() {

        assertFalse(databaseManager.connect("noDatabase", "sqlcmd", "sqlcmd"));
    }
    @Test
    public void connectIfWrongUser() {

        assertFalse(databaseManager.connect("sqlcmd", "wrongUser", "sqlcmd"));
    }
    @Test
    public void connectIfWrongPassword() {

        assertFalse(databaseManager.connect("sqlcmd", "sqlcmd", "wrongPassword"));
    }
}
