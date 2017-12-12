package juja.sqlcmd;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private DatabaseManager manager;

    @Before
    public void initialize() {
        manager = new DatabaseManager();
    }


    @Test
    public void connectionWithValidParametersHasToReturnTrue() {
        assertTrue(manager.connect("sqlcmd", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectionWithWrongDatabaseNameHasToReturnFalse() {
     assertFalse(manager.connect("wrongName", "sqlcmd", "sqlcmd"));
    }

    @Test
    public void connectionWithWrongUserNameHasToReturnFalse() {
        assertFalse(manager.connect("sqlcmd", "wrongUserName", "sqlcmd"));
    }

    @Test
    public void connectionWithWrongPasswordHasToReturnFalse() {
        assertFalse(manager.connect("sqlcmd", "sqlcmd", "wrongPassword"));
    }
}