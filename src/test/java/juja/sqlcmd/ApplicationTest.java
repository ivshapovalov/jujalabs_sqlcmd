package juja.sqlcmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Before
    public void setUpStreams() throws SQLException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testloadApplication() {
        try {
            new Application().simpleSQL();
            String expected = "db is empty." + LINE_SEPARATOR +
                    "user" + LINE_SEPARATOR +
                    LINE_SEPARATOR +
                    "1 | user1 | password1" + LINE_SEPARATOR +
                    "2 | user2 | password2" + LINE_SEPARATOR +
                    "3 | user3 | password3" + LINE_SEPARATOR +
                    LINE_SEPARATOR +
                    "1 | user1 | password1" + LINE_SEPARATOR +
                    "3 | user3 | password3" + LINE_SEPARATOR +
                    "2 | userсhange1 | password2" + LINE_SEPARATOR +
                    LINE_SEPARATOR +
                    "1 | user1 | password1" + LINE_SEPARATOR +
                    "2 | userсhange1 | password2" + LINE_SEPARATOR +
                    LINE_SEPARATOR;
            assertEquals(expected,outContent.toString());
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERROR!" + e.getMessage());
        }
    }
}
