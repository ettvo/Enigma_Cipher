package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Evelyn Vo
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            String error = "Reflector's permutation should be a derangement.";
            throw new EnigmaException(error);
        }
    }

    /** Return true iff I reflect. */
    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("Reflector can only have posn = 0.");
        }
    }

}
