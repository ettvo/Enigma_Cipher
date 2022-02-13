package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Evelyn Vo
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        validateName(name);
        _name = name;
        _permutation = perm;
        _currPos = 0;
    }

    /** Checks whether the rotor name is valid.
     * @param name The given name for a rotor. */
    private void validateName(String name) {
        if (name.equals("")) {
            throw new EnigmaException("Invalid rotor name.");
        }
        for (int index = 0; index < name.length(); index += 1) {
            char ch = name.charAt(index);
            boolean isWS = Character.isWhitespace(ch);
            if (isWS || ch == '(' || ch == ')') {
                throw new EnigmaException("Invalid rotor name.");
            }
        }
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _currPos;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (posn < 0) {
            throw new EnigmaException("Can't have a negative rotor position.");
        } else if (posn > _permutation.size()) {
            String error = "Can't have a rotor posn be > permutation.size().";
            throw new EnigmaException(error);
        }
        _currPos = posn;
    }

    /** Set setting() to character CPOSN. In other words, the position is set
     * to the index of CPOSN in the alphabet of _permutation. */
    void set(char cposn) {
        int permuteIndex = alphabet().toInt(cposn);
        if (permuteIndex == -1) {
            String error = "Can't have rotor posn not in the alphabet.";
            throw new EnigmaException(error);
        }
        _currPos = permuteIndex;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. P is the index of the original alphabet. */
    int convertForward(int p) {
        if (p < 0 || p >= size()) {
            throw new EnigmaException("Invalid parameters for convertForward.");
        }
        int contactEntered = p + setting();
        int contactExited = _permutation.permute(contactEntered);
        int positionExited = wrap(contactExited - setting());
        char transl = alphabet().toChar(positionExited);
        return positionExited;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if (e < 0 || e >= size()) {
            throw new EnigmaException("Invalid params for convertBackward.");
        }
        int contactEntered = e + setting();
        int contactExited = _permutation.invert(contactEntered);
        int positionExited = wrap(contactExited - setting());
        char transl = alphabet().toChar(positionExited);
        return positionExited;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current position of this rotor. */
    private int _currPos;
}
