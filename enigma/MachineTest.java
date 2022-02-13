package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Evelyn Vo
 */
public class MachineTest {

    /* Testing Utilities */
    private String[][] navRtrSettings = {{"I", "M", "Q"},
        {"II", "M", "E"}, {"III", "M", "V"},
        {"IV", "M", "J"}, {"V", "M", "Z"},
        {"VI", "M", "ZM"}, {"VII", "M", "ZM"},
        {"VIII", "M", "ZM"}, {"Beta", "N", ""},
        {"Gamma", "N", ""}};

    private final HashMap<String, Rotor> defRotors = new HashMap<>();

    /** Sets default Rotors to the ArrayList of the rotors defined by NAVALA. */
    private void setDefaultRotors() {
        for (String[] rotor: navRtrSettings) {
            String name = rotor[0];
            Permutation perm = new Permutation(NAVALA.get(name), UPPER);
            assertEquals(NAVALA_MAP.get(name), perm.toString());
            String notches = rotor[2];
            switch (rotor[1].charAt(0)) {
            case 'M':
                defRotors.put(name, new MovingRotor(name, perm, notches));
                break;
            case 'N':
                defRotors.put(name, new FixedRotor(name, perm));
                break;
            case 'R':
                defRotors.put(name, new Reflector(name, perm));
                break;
            default:
                throw new EnigmaException("Error on creating "
                    + "default machine.");
            }
        }
        Permutation permB = new Permutation(NAVALA.get("B"), UPPER);
        defRotors.put("B", new Reflector("B", permB));
        Permutation permC = new Permutation(NAVALA.get("C"), UPPER);
        defRotors.put("C", new Reflector("C", permC));
    }

    /** Returns the default machine defined by default.conf and NAVALA. */
    private Machine getDefaultMachine(String[] rotorSel) {
        setDefaultRotors();
        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        for (String name: rotorSel) {
            rotors.add(defRotors.get(name));
        }
        return new Machine(UPPER, 5, 3, rotors);
    }

    /** The expected output for translating exampleNaval.in. */
    private String inputExampleNaval = "FROMHISSHOULDERHIAWATHA\n"
            + "TOOKTHECAMERAOFROSEWOOD\n"
            + "MADEOFSLIDINGFOLDINGROSEWOOD\n"
            + "NEATLYPUTITALLTOGETHER\n"
            + "INITSCASEITLAYCOMPACTLY\n"
            + "FOLDEDINTONEARLYNOTHING\n"
            + "BUTHEOPENEDOUTTHEHINGES\n"
            + "PUSHEDANDPULLEDTHEJOINTS\n"
            + "ANDHINGES\n"
            + "TILLITLOOKEDALLSQUARES\n"
            + "ANDOBLONGS\n"
            + "LIKEACOMPLICATEDFIGURE\n"
            + "INTHESECONDBOOKOFEUCLID";

    /** The expected output for translating exampleNaval.in. */
    private String outputExampleNaval = "QVPQSOKOILPUBKJZPISFXDW\n"
            + "BHCNSCXNUOAATZXSRCFYDGU\n"
            + "FLPNXGXIXTYJUJRCAUGEUNCFMKUF\n"
            + "WJFGKCIIRGXODJGVCGPQOH\n"
            + "ALWEBUHTZMOXIIVXUEFPRPR\n"
            + "KCGVPFPYKIKITLBURVGTSFU\n"
            + "SMBNKFRIIMPDOFJVTTUGRZM\n"
            + "UVCYLFDZPGIBXREWXUEBZQJO\n"
            + "YMHIPGRRE\n"
            + "GOHETUXDTWLCMMWAVNVJVH\n"
            + "OUFANTQACK\n"
            + "KTOZZRDABQNNVPOIEFQAFS\n"
            + "VVICVUDUEREYNPFFMNBJVGQ";

    /** Checks that changing Machine to test exampleNaval
     * inputs didn't make previous tests fail. */
    private void checkDidntBreakPrevFunctionality() {
        testCorrectEncryptionDefault();
        testCorrectDecryptionDefault();
        testCorrectEncryptionSimple();
        testCorrectDecryptionSimple();
    }

    /** Checks that the current settings match the expected settings. */
    private void checkPositions(Machine mach, String[][] exp, Alphabet alph) {
        HashMap<String, String> currSettings = mach.rotorSettings();
        for (String[] rotor: exp) {
            String name = rotor[0];
            String expectedPos = rotor[1];
            int index = Integer.parseInt(currSettings.get(name));
            assertEquals(expectedPos, "" + alph.toChar(index));
        }
    }

    /* Tests */

    /** Checks that Machine correctly encrypts the trivial inputs.
     * Shows that the Machine works correctly without a plugboard and on
     * inputs of a single line. */
    @Test
    public void testCorrectEncryptionDefault() {
        Machine defConf = getDefaultMachine(new String[]{"Beta",
            "II", "I", "B", "III"});
        String defInput = "HELLOWORLD";
        String defOutput = "ILBDAAMTAZ";
        defConf.insertRotors(new String[]{"B", "Beta", "I", "II", "III"});
        defConf.setRotors("AAAA");
        assertEquals("Default encryption for trivial.in incorrect.",
                defOutput, defConf.convert(defInput));
    }

    /** Checks that Machine correctly decrypts the trivial inputs.
     * Shows that the Machine works correctly without a plugboard and on
     * inputs of a single line. */
    @Test
    public void testCorrectDecryptionDefault() {
        Machine defConf = getDefaultMachine(new String[]{"Beta",
            "II", "I", "B", "III"});
        String defInput = "HELLOWORLD";
        String defOutput = "ILBDAAMTAZ";
        defConf.insertRotors(new String[]{"B", "Beta", "I", "II", "III"});
        defConf.setRotors("AAAA");
        assertEquals("Default decryption for trivial.in incorrect.",
                defInput, defConf.convert(defOutput));
    }

    /** Checks that Machine correctly encrypts simple input of
     * a single line that has a plugboard and nonzero rotor settings. */
    @Test
    public void testCorrectEncryptionSimple() {
        Machine defConf = getDefaultMachine(new String[] {"Beta",
            "IV", "B", "I", "III"});
        defConf.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        defConf.setRotors("AXLE");
        Permutation plug = new Permutation("(YF) (ZH)", UPPER);
        defConf.setPlugboard(plug);
        assertEquals("Simple machine encryption for simple.in incorrect.",
                "Z", defConf.convert("Y"));
        for (int x = 0; x < 11; x += 1) {
            defConf.convert("A");
        }
        String[][] expected = new String[][] {{"Beta", "A"}, {"III", "X"},
            {"IV", "L"}, {"I", "Q" }};
        checkPositions(defConf, expected, UPPER);
    }


    /** Checks that Machine correctly decrypts simple input of
     * a single line that has a plugboard and nonzero rotor settings. */
    @Test
    public void testCorrectDecryptionSimple() {
        Machine defConf = getDefaultMachine(new String[] {"Beta",
            "IV", "B", "I", "III"});
        defConf.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        defConf.setRotors("AXLE");
        Permutation plug = new Permutation("(YF) (ZH)", UPPER);
        defConf.setPlugboard(plug);
        assertEquals("Simple machine decryption for simple.in incorrect.",
                "Y", defConf.convert("Z"));
    }


    /** Checks that Machine correctly encrypts the exampleNaval input. */
    @Test
    public void testCorrectEncryptionExampleNaval() {
        checkDidntBreakPrevFunctionality();
        Machine defConf = getDefaultMachine(new String[] {"Beta",
            "I", "III", "B", "IV"});
        defConf.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        defConf.setRotors("AXLE");
        Permutation plug = new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER);
        defConf.setPlugboard(plug);
        String convertedMsg = defConf.convert(inputExampleNaval);
        assertEquals("Input machine encryption for exampleNaval.in incorrect.",
                outputExampleNaval, convertedMsg);
    }

    /** Checks that Machine correctly decrypts the exampleNaval input. */
    @Test
    public void testCorrectDecryptionExampleNaval() {
        checkDidntBreakPrevFunctionality();
        Machine defConf = getDefaultMachine(new String[] {"Beta",
            "I", "III", "B", "IV"});
        defConf.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        defConf.setRotors("AXLE");
        Permutation plug = new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER);
        defConf.setPlugboard(plug);
        String convertedMsg = defConf.convert(outputExampleNaval);
        assertEquals("Input machine decryption for exampleNaval.in incorrect.",
                    inputExampleNaval, convertedMsg);
    }

    /** Checks that convert(int) works correctly with or without a plugboard.
     * Tests for whether convert(int) correctly identifies when to advance.
     * A given rotor should advance if: (1) it is the rightmost rotor,
     * (2) the rotor’s right neighbor (prevRotor) is at a notch, or
     * (3) the rotor is at a notch and its left neighbor rotates. */
    @Test
    public void testConvertInt() {
    }

    /** Checks that convert(str) works correctly with or without a plugboard. */
    @Test
    public void testConvertString() {
    }

    /** Checks that setPlugboard(...) correctly errors on an
     * invalid plugboard (plugboard alphabet does not match
     * the machine alphabet). */
    @Test(expected = EnigmaException.class)
    public void testInvalidPlugboardAlphabet() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that setRotors(...) correctly errors when a
     * setting with characters not in the alphabet of the
     * Machine is sent. */
    @Test(expected = EnigmaException.class)
    public void testInvalidSettingsToSetRotors() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that setRotors(...) correctly errors when a
     * setting the incorrect length is sent to setRotors(...). */
    @Test(expected = EnigmaException.class)
    public void testInvalidSettingsLengthToSetRotors() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that setRotors(...) sets rotors to the correct setting. */
    @Test
    public void testRotorsSettingsCorrect() {
    }

    /** Checks that insertRotors(...) errors when parameters
     * length != number of rotors expected. */
    @Test(expected = EnigmaException.class)
    public void testInvalidNumberRotorsToInsertRotors() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when rotors not
     * in the machine are sent to insertRotors(...). */
    @Test(expected = EnigmaException.class)
    public void testRotorsNotInMachine() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when the first
     * (leftmost) rotor sent is not a reflector. */
    @Test(expected = EnigmaException.class)
    public void testFirstRotorNotReflector() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when the last
     * (rightmost) rotor sent is not a moving rotor. */
    @Test(expected = EnigmaException.class)
    public void testLastRotorNotMovingRotor() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when there is
     * more than one reflector. */
    @Test(expected = EnigmaException.class)
    public void testMultipleReflectors() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when duplicate
     * rotors are passed to InsertRotors (multiple fixed). */
    @Test(expected = EnigmaException.class)
    public void testDuplicatesFixed() {
        throw new EnigmaException("Method not yet implemented.");
    }

    /** Checks that insertRotors(...) errors when duplicate rotors
     * are passed to InsertRotors (multiple moving). */
    @Test(expected = EnigmaException.class)
    public void testDuplicatesMoving() {
        throw new EnigmaException("Method not yet implemented.");
    }

}
