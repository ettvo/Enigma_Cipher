package enigma;

import org.junit.Test;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Main.
 *  @author Evelyn Vo
 */
public class MainTest {

    /**
     * Tests that Main correctly parses a correct file and
     * outputs the correct encryption.
     */
    @Test
    public void testCorrectEncryption() {

    }

    /**
     * Tests that Main correctly parses a correct file and
     * outputs the correct decryption.
     */
    @Test
    public void testCorrectDecryption() {

    }

    /**
     * Tests that Main correctly accepts initial entry strings
     * with a plugboard and without a plugboard.
     */
    @Test
    public void testAcceptsOptionalArgs() {

    }

    /**
     * Tests that Main correctly accepts initial entry strings with
     * a variable amount of whitespace between arguments.
     */
    @Test
    public void testRandomWhitespaceInInitialPositions() {

    }

    /**
     * Tests that Main correctly accepts settings from the configuration
     * file with a variable amount of whitespace between arguments (between
     * name, type/notches, and cycles arguments; between cycles in parentheses).
     */
    @Test
    public void testRandomWhitespaceInConf() {

    }

    /**
     * Tests that Main errors on an invalid input: file does not exist.
     */
    @Test(expected = EnigmaException.class)
    public void testInputDoesNotExist() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: configuration
     * file has the wrong format.
     */
    @Test(expected = EnigmaException.class)
    public void testWrongInput() {
        throw new EnigmaException("Method not completed yet");

    }

    /**
     * Tests that Main errors on an invalid input: setting line
     * has the wrong number of arguments.
     */
    @Test(expected = EnigmaException.class)
    public void testIncorrectNumArgs() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: rotors repeated.
     */
    @Test(expected = EnigmaException.class)
    public void testRepeatedRotors() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: rotors with
     * invalid names.
     */
    @Test(expected = EnigmaException.class)
    public void testInvalidRotorName() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: incorrect
     * rotor order in the settings line such that the first
     * (leftmost) rotor is not a reflector.
     */
    @Test(expected = EnigmaException.class)
    public void testFirstRotorNotReflector() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: incorrect
     * rotor order in the settings line such that the last
     * (rightmost) rotor is not a MovingRotor.
     */
    @Test(expected = EnigmaException.class)
    public void testLastRotorNotMovingRotor() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: incorrect number
     * of rotors and pawls such that pawls > total rotors.
     */
    @Test(expected = EnigmaException.class)
    public void testMorePawlsThanRotors() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: negative
     * number of pawls.
     */
    @Test(expected = EnigmaException.class)
    public void testNegativePawls() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: negative
     * total rotors.
     */
    @Test(expected = EnigmaException.class)
    public void testNegativeTotalRotors() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid
     * number of pawls (0).
     */
    @Test(expected = EnigmaException.class)
    public void testInvalidNumPawls0() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid
     * number of rotors (0). There must be both a reflector and
     * a moving rotor.
     */
    @Test(expected = EnigmaException.class)
    public void testInvalidNumRotors0() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid
     * number of rotors (1). There must be both a reflector and
     * a moving rotor.
     */
    @Test(expected = EnigmaException.class)
    public void testInvalidNumRotors1() {
        throw new EnigmaException("Method not completed yet");
    }


    /**
     * Tests that Main errors on an invalid input: an
     * initial positions string length with an invalid length
     * such that the initial positions string has not enough inputs.
     */
    @Test(expected = EnigmaException.class)
    public void testInitialPositionsLengthTooShort() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: an initial positions
     * string without an asterisk in the first column.
     */
    @Test(expected = EnigmaException.class)
    public void testInitialPositionsWithoutAsterisk() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * empty cycles.
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardWithEmptyCycles() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * letters not in the machine's alphabet.
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardWithInvalidAlphabet() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * incorrectly structured cycles (no parentheses).
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardNoParenthesesCycles() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * incorrectly structured cycles (incorrectly closed). Ex: (( ...)
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardIncorrectlyClosed0() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * incorrectly structured cycles (incorrectly closed). Ex: (...) ..)
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardIncorrectlyClosed1() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: invalid plugboards with
     * incorrectly structured cycles (incorrectly closed). Ex: (... ..)
     */
    @Test(expected = EnigmaException.class)
    public void testPlugboardIncorrectlyClosed2() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: message to be
     * encrypted/decrypted not in the machine alphabet.
     */
    @Test(expected = EnigmaException.class)
    public void testInputNotInAlphabet() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: initial positions string
     * does not contain the correct number of rotors (must be equal to
     * total rotors).
     */
    @Test(expected = EnigmaException.class)
    public void testIncorrectTotalRotorsInInput() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: incorrect
     * number of moving rotors in input (must be equal to moving
     * rotors).
     */
    @Test(expected = EnigmaException.class)
    public void testIncorrectTotalMovingRotorsInInput() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: rotors repeated
     * in the initial positions string.
     */
    @Test(expected = EnigmaException.class)
    public void testRepeatedRotorsInInitialPositions() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: rotors
     * passed in the initial positions string are not in the
     * machine.
     */
    @Test(expected = EnigmaException.class)
    public void testRotorsInInitialPositionsNotInMachine() {
        throw new EnigmaException("Method not completed yet");
    }

    /**
     * Tests that Main errors on an invalid input: rotors
     * in the machine with different alphabets.
     */
    @Test(expected = EnigmaException.class)
    public void testRotorsWithDifferentAlphabets() {
        throw new EnigmaException("Method not completed yet");
    }


}
