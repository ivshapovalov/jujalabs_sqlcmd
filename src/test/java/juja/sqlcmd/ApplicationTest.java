package juja.sqlcmd;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private PrintStream originalOut;
    private PrintStream originalErr;

    @Before
    public void setUpStreams() {
        originalOut=System.out;
        originalErr=System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void out() throws SQLException, ClassNotFoundException {
        new Application().simpleSQL();
        String expected = "users" + LINE_SEPARATOR +
                " 1 | user1 | password1" +LINE_SEPARATOR +
                " 2 | user2 | password2" +LINE_SEPARATOR +
                " 3 | user3 | password3" +LINE_SEPARATOR +
                LINE_SEPARATOR +
                " 1 | user1 | password1" +LINE_SEPARATOR +
                " 3 | user3 | password3" +LINE_SEPARATOR +
                " 2 | userсhange1 | password2" +LINE_SEPARATOR +
                LINE_SEPARATOR +
                " 1 | user1 | password1" +LINE_SEPARATOR +
                " 2 | userсhange1 | password2" +LINE_SEPARATOR +
                LINE_SEPARATOR;
        assertEquals(expected, outContent.toString());
    }
}
