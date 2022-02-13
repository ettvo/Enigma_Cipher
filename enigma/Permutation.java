package enigma;

import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Evelyn Vo
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        testCycles(cycles, alphabet);
        _cycles = cycles;
        _alphabet = alphabet;
        char[] permutedAlph = new char[alphabet.size()];
        char[] inverseAlph = new char[alphabet.size()];
        for (int index = 0; index < _alphabet.size(); index += 1) {
            char letter = _alphabet.toChar(index);
            boolean isWS = Character.isWhitespace(letter);
            if (cycles.indexOf(letter) == -1 && !isWS) {
                permutedAlph[index] = letter;
                inverseAlph[index] = letter;
            } else if (!isWS) {
                int ltIndex = cycles.indexOf(letter);
                if (cycles.charAt(ltIndex + 1) == ')') {
                    if (cycles.charAt(ltIndex - 1) == '(') {
                        permutedAlph[index] = letter;
                        inverseAlph[index] = letter;
                    } else {
                        inverseAlph[index] = cycles.charAt(ltIndex - 1);
                        int parenth = ltIndex;
                        while (cycles.charAt(parenth) != '(') {
                            parenth -= 1;
                        }
                        permutedAlph[index] = cycles.charAt(parenth + 1);
                    }
                } else {
                    permutedAlph[index] = cycles.charAt(ltIndex + 1);
                    if (cycles.charAt(ltIndex - 1) == '(') {
                        int parenth = ltIndex;
                        while (cycles.charAt(parenth) != ')') {
                            parenth += 1;
                        }
                        inverseAlph[index] = cycles.charAt(parenth - 1);
                    } else {
                        inverseAlph[index] = cycles.charAt(ltIndex - 1);
                    }
                }
            }
        }
        _permuted = new Alphabet(String.valueOf(permutedAlph));
        _inverse = new Alphabet(String.valueOf(inverseAlph));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        Permutation newPerm = new Permutation(_cycles.concat(cycle), _alphabet);
        _permuted = newPerm._permuted;
        _inverse = newPerm._inverse;
        _cycles = newPerm._cycles;
    }

    /** Tests if the provided cycles are in the correct form.
     * Cycles in the correct form must be a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET.
     * Every letter in the alphabet must appear in CYCLES, and every parenthesis
     * must be closed correctly.
     * */
    private void testCycles(String cycles, Alphabet alphabet) {
        if (cycles.equals("()")) {
            throw new EnigmaException("Can't have a cycle with no characters.");
        }
        boolean closed = true;
        ArrayList<Character> letters = new ArrayList<Character>();
        boolean insideCycle = false;
        for (int index = 0; index < cycles.length(); index += 1) {
            char currLetter = cycles.charAt(index);
            if (currLetter != '(' && currLetter != ')' && insideCycle) {
                if (letters.contains(currLetter)) {
                    throw new EnigmaException("Duplicate chars in cycles.");
                } else if (Character.isWhitespace(currLetter)) {
                    throw new EnigmaException("Can't have WS within a cycle.");
                } else {
                    letters.add(currLetter);
                }
            } else if (currLetter == '(') {
                if (closed) {
                    closed = false;
                    insideCycle = true;
                } else {
                    throw new EnigmaException("Improperly closed cycs ((.");
                }
            } else if (currLetter == ')') {
                if (!closed) {
                    closed = true;
                    insideCycle = false;
                } else {
                    throw new EnigmaException("Improperly closed cycs )).");
                }
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. In other words, the index of the NEW letter that
     *  the original letter is permuted to. */
    int permute(int p) {
        int newIndex = wrap(p);
        char permLetter = _permuted.toChar(newIndex);
        return _alphabet.toInt(permLetter);
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. In other words, the index of
     *  the NEW letter that the original letter is permuted to. */
    int invert(int c) {
        char invLetter = _inverse.toChar(wrap(c));
        return _alphabet.toInt(invLetter);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_permuted.contains(p)) {
            throw new EnigmaException("Letter not in permuted alphabet.");
        }
        return _permuted.toChar(_alphabet.toInt(p));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_inverse.contains(c)) {
            throw new EnigmaException("Letter not in inverse alphabet.");
        }
        return _inverse.toChar(_alphabet.toInt(c));
    }

    /** Returns whether this permutation contains a given letter.
     * @param letter The letter to be checked. */
    boolean contains(char letter) {
        return _alphabet.contains(letter);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int index = 0; index < _alphabet.size(); index += 1) {
            if (_alphabet.toChar(index) == _permuted.toChar(index)) {
                return false;
            }
        }
        return true;
    }

    /** Return the permutation of the entire alphabet used
     * to initialize this Permutation. */
    Alphabet permutation() {
        return _permuted;
    }

    /** Return the inverse permutation of the entire alphabet used
     *  to initialize this Permutation. */
    Alphabet inversePermutation() {
        return _inverse;
    }

    /** Returns a String representation of the permuted alphabet. */
    @Override
    public String toString() {
        return _permuted.toString();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** The permuted form of _alphabet. */
    private Alphabet _permuted;

    /** The inversely permuted form of _alphabet. */
    private Alphabet _inverse;

    /** The cycles used to permute _alphabet. Each row is a cycle. */
    private String _cycles;

}
