package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private DatabaseManager manager;

    @Before
    public void setUp() {
        manager = new DatabaseManager();
    }


    @Test
    public void connectWithValidParametersShouldReturnTrue() {
        assertTrue(manager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWithWrongDatabaseNameShouldReturnFalse() {
     assertFalse(manager.connect("wrongName", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectWithWrongUserNameShouldReturnFalse() {
        assertFalse(manager.connect("sqlcmd", "wrongUserName", "sqlcmd"));
    }

    @Test
    public void connectWithWrongPasswordShouldReturnFalse() {
        assertFalse(manager.connect("sqlcmd", "sqlcmd", "wrongPassword"));
    }
}