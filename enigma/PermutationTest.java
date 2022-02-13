package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Evelyn Vo
 */
public class PermutationTest {

    /**
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     */
    public Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    /**
     *  @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    public Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }

    /**
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    public Alphabet getNewAlphabet() {
        return new Alphabet();
    }

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    /**
     * Checks that the Permutation constructor errors
     * on invalid cycles with duplicate characters in the cycles.
     */
    @Test(expected = EnigmaException.class)
    public void checkDuplicatesInCycles() {
        Alphabet small = getNewAlphabet("ABCD");
        Permutation smallPerm = getNewPermutation("(BACDD)", small);
    }

    /**
     * Checks that the Permutation constructor errors
     * on invalid cycles with extra characters in the cycles
     * that are not in the provided alphabet.
     */
    @Test(expected = EnigmaException.class)
    public void checkExtraInCycles() {
        Alphabet small = getNewAlphabet("ABCD");
        Permutation smallPerm = getNewPermutation("(BAYCD)", small);
        Alphabet threeCycs = getNewAlphabet("ABC");
        String thCycs = "(F) (B) (A) (B)";
        Permutation threeCycsPerm = getNewPermutation(thCycs, threeCycs);
    }


    /**
     * Checks that permutation and inversion are both bijections.
     * In other words, this test checks whether there is a
     * one-to-one correspondence between the alphabet
     * and its permutation, and the alphabet and its inverse
     * permutation, and whether each letter is mapped correctly.
     */
    @Test
    public void checkBijection() {
        Alphabet small = getNewAlphabet("ABCD");
        Permutation smallPerm = getNewPermutation("(BACD)", small);
        checkPerm("small alphabet", "ABCD", "CADB", smallPerm, small);

        Alphabet single = getNewAlphabet("A");
        Permutation singlePerm = getNewPermutation("(A)", single);
        checkPerm("single letter", "A", "A", singlePerm, single);

        Alphabet two = getNewAlphabet("AB");
        Permutation twoPerm = getNewPermutation("(BA)", two);
        checkPerm("two letters", "AB", "BA", twoPerm, two);

        Alphabet twoCycs = getNewAlphabet("AB");
        Permutation twoCycsPerm = getNewPermutation("(B) (A)", twoCycs);
        checkPerm("two letters", "AB", "AB", twoCycsPerm, twoCycs);

        Alphabet three = getNewAlphabet("ABC");
        Permutation threePerm = getNewPermutation("(CBA)", three);
        checkPerm("three letters", "ABC", "CAB", threePerm, three);

        Alphabet threeCycs = getNewAlphabet("ABC");
        Permutation threeCycsPerm = getNewPermutation("(C) (B) (A)", threeCycs);
        String testId = "two letters with several cycles";
        checkPerm(testId, "ABC", "ABC", threeCycsPerm, threeCycs);
    }

    /**
     * Tests size() to make sure that the correct size is
     * returned, and that the original alphabet, permutation,
     * and inverse permutation all have the same size.
     */
    @Test
    public void testSize() {
        Alphabet small = getNewAlphabet("ABCD");
        Permutation abcdCyc = getNewPermutation("(A) (B) (C) (D)", small);
        Permutation abcd = getNewPermutation("", small);
        Permutation cbad = getNewPermutation("(AC) (B) (D)", small);
        Permutation bcda = getNewPermutation("(CDAB)", small);
        Permutation dcba = getNewPermutation("(AD) (BC)", small);

        assertEquals(abcdCyc.size(), small.size());
        assertEquals(abcd.size(), small.size());
        assertEquals(cbad.size(), small.size());
        assertEquals(bcda.size(), small.size());
        assertEquals(dcba.size(), small.size());

        Alphabet single = getNewAlphabet("A");
        Permutation a = getNewPermutation("", single);
        assertEquals(a.size(), single.size());

        assertEquals(26, getNewAlphabet().size());
    }

    /**
     * Tests whether Permutation correctly errors
     * when an incorrect parameter is passed to permute(...).
     */
    @Test(expected = EnigmaException.class)
    public void testNotInPermuteAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.permute('F');
    }

    /**
     * Tests whether Permutation correctly errors
     * when an incorrect parameter is passed to invert(...).
     */
    @Test(expected = EnigmaException.class)
    public void testNotInInvertAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
    }

    /**
     * Tests whether derangement() correctly returns
     * whether the permutation is a derangement.
     */
    @Test
    public void testDerangement() {
        Alphabet small = getNewAlphabet("ABCD");
        Permutation abcdCyc = getNewPermutation("(A) (B) (C) (D)", small);
        Permutation abcd = getNewPermutation("", small);
        Permutation cbad = getNewPermutation("(AC) (B) (D)", small);
        Permutation dbca = getNewPermutation("(AD) (B) (C)", small);
        Permutation cabd = getNewPermutation("(ACB) (D)", small);
        assertFalse(abcd.derangement());
        assertFalse(abcdCyc.derangement());
        assertFalse(cbad.derangement());
        assertFalse(dbca.derangement());
        assertFalse(cabd.derangement());
        Permutation cadb = getNewPermutation("(ACDB)", small);
        Permutation bcda = getNewPermutation("(CDAB)", small);
        Permutation dcba = getNewPermutation("(AD) (BC)", small);
        Permutation cbda = getNewPermutation("(AC) (BD)", small);
        assertTrue(cadb.derangement());
        assertTrue(bcda.derangement());
        assertTrue(dcba.derangement());
        assertTrue(cbda.derangement());
    }

    /**
     * Helper function for testIsomorphism() that checks
     * if an individual permutation's permutation and inverse
     * permutation functions are isomorphisms that are each other's
     * inverse transformations.
     */
    private void testIsomorphismHelper(Permutation perm) {
        Alphabet alph = perm.alphabet();
        for (int origIndex = 0; origIndex < alph.size(); origIndex += 1) {
            char origLetter = alph.toChar(origIndex);
            char permLetter = perm.permutation().toChar(origIndex);
            int permIndex = alph.toInt(permLetter);
            char invLetter = perm.inversePermutation().toChar(origIndex);
            int invIndex = alph.toInt(invLetter);
            boolean pmUndoInvCh = perm.permute(invLetter) != origLetter;
            boolean pmUndoInvIn = perm.permute(invIndex) != origIndex;
            boolean invUndoPmCh = perm.invert(permLetter) != origLetter;
            boolean invUndoPmIn = perm.invert(permIndex) != origIndex;
            if (pmUndoInvCh || pmUndoInvIn) {
                throw new EnigmaException("Perm. does not undo inverse perm.");
            } else if (invUndoPmCh || invUndoPmIn) {
                throw new EnigmaException("Inv. perm. does not undo perm.");
            }
        }
    }

    /**
     * Tests that the permutation and inverse permutation of
     * an alphabet map in opposite directions and get the correct
     * letters. In other words, that permuting and inversely
     * permuting are both isomorphisms that undo each other.
     */
    @Test
    public void testIsomorphism() {
        Alphabet alph = new Alphabet();
        Permutation permNavalaI = new Permutation(navalaI, alph);
        testIsomorphismHelper(permNavalaI);
        Permutation permNavalaII = new Permutation(navalaII, alph);
        testIsomorphismHelper(permNavalaII);
        Permutation permNavalaIII = new Permutation(navalaIII, alph);
        testIsomorphismHelper(permNavalaIII);
        Permutation permNavalaIV = new Permutation(navalaIV, alph);
        testIsomorphismHelper(permNavalaIV);
        Permutation permNavalaV = new Permutation(navalaV, alph);
        testIsomorphismHelper(permNavalaV);
        Permutation permNavalaVII = new Permutation(navalaVII, alph);
        testIsomorphismHelper(permNavalaVII);
        Permutation permNavalaC = new Permutation(navalaC, alph);
        testIsomorphismHelper(permNavalaC);
    }

    /**
     * Tests that invert(char) and invert(int) both correctly map letters.
     */
    @Test
    public void testTransformationInvert() {
        Alphabet alph = new Alphabet();
        Permutation permNavalaI = new Permutation(navalaI, alph);
        assertEquals('I', permNavalaI.invert('V'));
        assertEquals('R', permNavalaI.invert('U'));
        assertEquals('Z', permNavalaI.invert('J'));
        assertEquals(17, permNavalaI.invert(20));
        assertEquals(25, permNavalaI.invert(9));
        assertEquals(8, permNavalaI.invert(21));
        assertEquals(20, permNavalaI.invert(-26));
        assertEquals(20, permNavalaI.invert(26));
        assertEquals(7, permNavalaI.invert(-10));
        assertEquals(24, permNavalaI.invert(28));

        Permutation permNavalaIII = new Permutation(navalaIII, alph);
        assertEquals('I', permNavalaIII.invert('R'));
        assertEquals('R', permNavalaIII.invert('W'));
        assertEquals('Z', permNavalaIII.invert('O'));
        assertEquals(8, permNavalaIII.invert(17));
        assertEquals(17, permNavalaIII.invert(22));
        assertEquals(25, permNavalaIII.invert(14));
    }

    /**
     * Tests that permute(char) and permute(int) both correctly map letters.
     */
    @Test
    public void testTransformationPermute() {
        Alphabet alph = new Alphabet();
        Permutation permNavalaI = new Permutation(navalaI, alph);
        assertEquals('V', permNavalaI.permute('I'));
        assertEquals('U', permNavalaI.permute('R'));
        assertEquals('J', permNavalaI.permute('Z'));
        assertEquals(20, permNavalaI.permute(17));
        assertEquals(9, permNavalaI.permute(25));
        assertEquals(21, permNavalaI.permute(8));
        assertEquals(23, permNavalaI.permute(-10));
        assertEquals(4, permNavalaI.permute(-26));
        assertEquals(4, permNavalaI.permute(26));
        assertEquals(12, permNavalaI.permute(28));

        Permutation permNavalaIII = new Permutation(navalaIII, alph);
        assertEquals('R', permNavalaIII.permute('I'));
        assertEquals('W', permNavalaIII.permute('R'));
        assertEquals('O', permNavalaIII.permute('Z'));
        assertEquals(17, permNavalaIII.permute(8));
        assertEquals(22, permNavalaIII.permute(17));
        assertEquals(14, permNavalaIII.permute(25));
    }

    /**
     * Calculates the cycles and positioning for tests. (utility)
     */
    void getSampleAlphabets() {
        Alphabet alph = getNewAlphabet();

        System.out.println("Original Alphabet:");
        System.out.println(alph.toIndexedString());
        System.out.println();

        Permutation permNavalaI = new Permutation(navalaI, alph);
        Permutation permNavalaII = new Permutation(navalaII, alph);
        Permutation permNavalaIII = new Permutation(navalaIII, alph);
        Permutation permNavalaIV = new Permutation(navalaIV, alph);
        Permutation permNavalaV = new Permutation(navalaV, alph);
        Permutation permNavalaVII = new Permutation(navalaVII, alph);
        Permutation permNavalaC = new Permutation(navalaC, alph);

        Permutation[] examples = {permNavalaI, permNavalaII, permNavalaIII,
                                     permNavalaIV, permNavalaV, permNavalaVII,
                                     permNavalaC};

        String[] titles = {"permNavalaI", "permNavalaII", "permNavalaIII",
                              "permNavalaIV", "permNavalaV", "permNavalaVII",
                              "permNavalaC"};

        for (int x = 0; x < examples.length; x += 1) {
            String print = String.format("%s Permuted Alphabet:", titles[x]);
            System.out.println(print);
            print = examples[x].permutation().toIndexedString();
            System.out.println(print);
            System.out.println();
        }
    }


    /* TESTING UTILITIES */
    private String navalaI = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
            + "(IV) (JZ) (S)";
    private String navalaII = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
            + "(GR) (NT) (A) (Q)";
    private String navalaIII = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";
    private String navalaIV = "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)";
    private String navalaV = "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)";
    private String navalaVII = "(ANOUPFRIMBZTLWKSVEGCJYDHXQ) ";
    private String navalaC = "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) "
        + "(LM) (PW) (QZ) (SX) (UY)";

}
