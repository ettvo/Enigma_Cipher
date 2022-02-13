package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Evelyn Vo
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            int expectedTranslation = rotor.convertForward(ci);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            int expectedInverse = rotor.convertForward(ei);
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    /** Checks that the Rotor constructor works correctly. */
    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));

        setRotor("II", NAVALA, "");
        checkRotor("Rotor II (A)", UPPER_STRING, NAVALA_MAP.get("II"));

        setRotor("III", NAVALA, "");
        checkRotor("Rotor III (A)", UPPER_STRING, NAVALA_MAP.get("III"));

        setRotor("Beta", NAVALA, "");
        checkRotor("Rotor Beta (A)", UPPER_STRING, NAVALA_MAP.get("Beta"));

        setRotor("IV", NAVALA, "");
        checkRotor("Rotor IV (A)", UPPER_STRING, NAVALA_MAP.get("IV"));
    }

    /** Checks that advance() works correctly. */
    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        String testId = "Rotor I advanced by 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("I"));

        for (int x = 0; x < 24; x += 1) {
            rotor.advance();
        }
        testId = "Rotor I advanced by 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("I"));

        setRotor("IV", NAVALA, "");
        rotor.advance();
        testId = "Rotor IV advanced by 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("IV"));

        for (int x = 0; x < 24; x += 1) {
            rotor.advance();
        }
        testId = "Rotor IV advanced by 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("IV"));

        setRotor("Beta", NAVALA, "");
        rotor.advance();
        testId = "Rotor Beta advanced by 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("Beta"));

        for (int x = 0; x < 24; x += 1) {
            rotor.advance();
        }
        testId = "Rotor Beta advanced by 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("Beta"));
    }

    /** Checks that set(...) works correctly. */
    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        String testId = "Rotor I set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("I"));
        rotor.set(1);
        testId = "Rotor I set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("I"));

        setRotor("II", NAVALA, "");
        rotor.set(25);
        testId = "Rotor II set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("II"));
        rotor.set(1);
        testId = "Rotor II set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("II"));

        setRotor("III", NAVALA, "");
        rotor.set(25);
        testId = "Rotor III set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("III"));
        rotor.set(1);
        testId = "Rotor III set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("III"));

        setRotor("IV", NAVALA, "");
        rotor.set(25);
        testId = "Rotor IV set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("IV"));
        rotor.set(1);
        testId = "Rotor IV set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("IV"));

        setRotor("VI", NAVALA, "");
        rotor.set(25);
        testId = "Rotor VI set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("VI"));
        rotor.set(1);
        testId = "Rotor VI set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("VI"));

        setRotor("VII", NAVALA, "");
        rotor.set(25);
        testId = "Rotor VII set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("VII"));
        rotor.set(1);
        testId = "Rotor VII set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("VII"));

        setRotor("Gamma", NAVALA, "");
        rotor.set(25);
        testId = "Rotor Gamma set to pos. 25";
        checkRotor(testId, UPPER_STRING, NAVALZ_MAP.get("Gamma"));
        rotor.set(1);
        testId = "Rotor Gamma set to pos. 1";
        checkRotor(testId, UPPER_STRING, NAVALB_MAP.get("Gamma"));

    }

    /** Checks that atNotch() works correctly. */
    @Test
    public void checkAtNotch() {
        setRotor("VII", NAVALA, "ZM");
        assertFalse(rotor.atNotch());
        rotor.set(25);
        assertTrue(rotor.atNotch());
        rotor.set(12);
        assertTrue(rotor.atNotch());
        rotor.set(16);
        assertFalse(rotor.atNotch());
        setRotor("III", NAVALA, "");
        assertFalse(rotor.atNotch());
        rotor.set(25);
        assertFalse(rotor.atNotch());
        rotor.set(12);
        assertFalse(rotor.atNotch());
        rotor.set(16);
        assertFalse(rotor.atNotch());
    }

    /** Checks that the constructor errors on incorrect notches (letters not
     * in alphabet). */
    @Test(expected = EnigmaException.class)
    public void checkInvalidNotches() {
        setRotor("VII", NAVALA, "?");
    }
}
