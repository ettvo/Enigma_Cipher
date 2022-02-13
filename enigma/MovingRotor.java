package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor (pawl) in the enigma machine.
 *  @author Evelyn Vo
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        checkNotches(notches);
        _notches = notches;
    }

    /** Checks if every letter in the notches is in
     * the permutation's alphabet.
     * @param notches The notches of a given permutation. */
    private void checkNotches(String notches) {
        Permutation perm = this.permutation();
        for (char ch: notches.toCharArray()) {
            if (!perm.contains(ch)) {
                throw new EnigmaException("Invalid notches.");
            }
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        char currLetter = permutation().alphabet().toChar(setting());
        if (_notches.indexOf(currLetter) != -1) {
            return true;
        }
        return false;
    }

    /** Return true iff I have a ratchet and can move.
     * In other words, returns true if this rotor is a Moving Rotor. */
    @Override
    boolean rotates() {
        return true;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    @Override
    void advance() {
        set(wrap(setting() + 1));
    }

    /** The notches of the rotor. */
    private String _notches;

}
