package enigma;

import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Evelyn Vo
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        if (chars == null || chars.equals("")) {
            throw new EnigmaException("Cannot have an empty alphabet.");
        }
        ArrayList<Character> uniqueLetters = new ArrayList<Character>();
        for (char letter: chars.toCharArray()) {
            if (uniqueLetters.contains(letter)) {
                throw new EnigmaException("Duplicate letters in alphabet.");
            } else {
                if (!Character.isWhitespace(letter)) {
                    uniqueLetters.add(letter);
                }
            }
        }
        _alph = new char[uniqueLetters.size()];
        for (int index = 0; index < uniqueLetters.size(); index += 1) {
            _alph[index] = uniqueLetters.get(index);
        }
        _strAlph = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alph.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        if (_strAlph.indexOf(ch) == -1) {
            return false;
        }
        return true;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Incorrect index for calling toChar.");
        }
        return _alph[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar().
     *  Returns -1 if not found. */
    int toInt(char ch) {
        return (new String(_alph)).indexOf(ch);
    }

    @Override
    public String toString() {
        return _strAlph;
    }

    /** Returns the String form of the alphabet with each letter indexed.
     * Used just for testing. */
    public String toIndexedString() {
        int index = 0;
        String ret = "";
        for (char ch: this.toString().toCharArray()) {
            ret += String.format("%d. %c,   ", index, ch);
            if (index == 13) {
                ret += "\n";
            }
            index += 1;
        }
        return ret;
    }

    /** The list of characters representing this alphabet. */
    private char[] _alph;

    /** The String form of the alphabet. */
    private String _strAlph;

}
