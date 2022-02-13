package enigma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Evelyn Vo
 */

class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        if (pawls < 0) {
            throw new EnigmaException("Cannot have a negative "
                    + "number of moving rotors.");
        } else if (pawls == 0) {
            throw new EnigmaException("Cannot have only fixed rotors.");
        } else if (pawls > numRotors) {
            throw new EnigmaException("Cannot have more moving "
                    + "rotors than total rotors.");
        } else if (numRotors <= 1) {
            throw new EnigmaException("Cannot have <= 1 rotors.");
        }

        checkRotors((ArrayList<Rotor>) allRotors, numRotors);

        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = (ArrayList<Rotor>) allRotors;
        _hasPlugboard = false;
        _hasRings = false;
    }

    /** Checks that allRotors is not empty, that every rotor shares
     * the same alphabet, that the number of rotors passed is not more than
     * the expected number of rotors, and that there is exactly 1 reflector.
     * @param allRotors All the rotors passed to the Machine constructor
     * @param numRotors The expected total number of rotors
     */
    private void checkRotors(ArrayList<Rotor> allRotors, int numRotors) {
        if (allRotors == null) {
            throw new EnigmaException("Cannot have an empty list of rotors.");
        }
        if (numRotors > allRotors.size()) {
            throw new EnigmaException("The list of all rotors is "
                    + "larger than the number of rotors.");
        }

        String alph = allRotors.get(0).alphabet().toString();

        for (int index = 1; index < allRotors.size(); index += 1) {
            Alphabet currAlph = allRotors.get(index).alphabet();
            if (!currAlph.toString().equals(alph)) {
                throw new EnigmaException("Cannot have rotors "
                        + "in the same machine with different alphabets.");
            }
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  In other words, this method selects rotors from the
     *  existing known rotors and orders them. rotors[rotors.length - 1]
     *  identifies fast moving rotor that always moves.
     *  rotors[0] names the reflector.
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw new EnigmaException("Incorrect number of rotors "
                    + "passed to insertRotors. Must be _numRotors.");
        }
        ArrayList<Rotor> orderedRotors = new ArrayList<Rotor>();
        int pawls = 0;
        Rotor currRotor = null;
        Rotor prevRotor;
        int numReflectors = 0;
        for (int index = 0; index < rotors.length; index += 1) {
            prevRotor = currRotor;
            if (prevRotor instanceof MovingRotor
                    && currRotor instanceof FixedRotor) {
                throw new EnigmaException("Fixed rot. before moving rot.");
            }
            for (int rotNum = 0; rotNum < _allRotors.size(); rotNum += 1) {
                Rotor selectRotor = _allRotors.get(rotNum);
                if (selectRotor.name().equals(rotors[index])) {
                    currRotor = selectRotor;
                    break;
                }
            }
            if (currRotor == null) {
                throw new EnigmaException("Rotors passed not in Machine.");
            } else if (index == 0 && !currRotor.reflecting()) {
                throw new EnigmaException("First rotor passed to "
                        + "insertRotors must be a reflector.");
            }  else if (index == rotors.length - 1 && !currRotor.rotates()) {
                throw new EnigmaException("Last rotor passed to "
                        + "insertRotors must be a moving rotor.");
            }
            if (currRotor instanceof MovingRotor) {
                pawls += 1;
                if (pawls > numPawls()) {
                    throw new EnigmaException("More moving rotors passed"
                            + "to insertRotors than in machine.");
                }
            } else if (currRotor instanceof Reflector) {
                numReflectors += 1;
                if (numReflectors > 1) {
                    throw new EnigmaException("Cannot have more "
                            + "than 1 reflector in use in a machine.");
                }
            }
            currRotor.set(0);
            if (orderedRotors.contains(currRotor)) {
                throw new EnigmaException("Cannot have duplicate "
                        + "rotors passed to insertRotors.");
            }
            orderedRotors.add(currRotor);
        }
        if (pawls != numPawls()) {
            throw new EnigmaException("Incorrect number of "
                    + "moving rotors passed to insertRotors.");
        } else if (numReflectors == 0) {
            throw new EnigmaException("Cannot have 0 reflectors.");
        }
        _allRotors = orderedRotors;
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Invalid length "
                    + "of parameter to setRotors.");
        }
        for (int index = 0; index < setting.length(); index += 1) {
            _allRotors.get(index + 1).set(setting.charAt(index));
        }
    }

    /** Set the plugboard to PLUGBOARD. Plugboard should
     * always be correct as it is instantiated in Main.java with
     * the alphabet of the given rotors. */
    void setPlugboard(Permutation plugboard) {
        Alphabet plgbd = plugboard.alphabet();
        for (int index = 0; index < plugboard.size(); index += 1) {
            if (!_alphabet.contains(plgbd.toChar(index))) {
                throw new EnigmaException("Plugboard contains a"
                        + " letter not in the machine's alphabet.");
            }
        }
        _plugboard = plugboard;
        _hasPlugboard = true;
    }

    /** Sets the rings to SETTINGS. */
    void setRings(String settings) {
        if (settings.length() != numRotors() - 1) {
            throw new EnigmaException("Incorrect number of settings");
        }
        for (int index = 1; index < _allRotors.size(); index += 1) {
            Rotor currRotor = _allRotors.get(index);
            int newPos = wrap(currRotor.setting() - _alphabet.toInt(settings.charAt(index - 1)));
            currRotor.set(newPos);
        }
        _hasRings = true;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % _alphabet.size();
        if (r < 0) {
            r += _alphabet.size();
        }
        return r;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. C is passed through the entire machine and may
     *  go through many permutations. */
    int convert(int c) {
        if (c < 0 || c > _alphabet.size() - 1) {
            throw new EnigmaException("Incorrect character "
                    + "value sent to Machine convert(...).");
        }
        int newC = c;
        if (_hasPlugboard) {
            newC = _plugboard.permute(c);
        }
        advanceMachine();
        for (int index = _allRotors.size() - 1; index >= 0; index -= 1) {
            Rotor chosRotor = _allRotors.get(index);
            newC = chosRotor.convertForward(newC);
        }
        for (int index = 1; index < _allRotors.size(); index += 1) {
            Rotor chosRotor = _allRotors.get(index);
            newC = chosRotor.convertBackward(newC);
        }
        if (_hasPlugboard) {
            newC = _plugboard.invert(newC);
        }
        return newC;
    }

    /** Advances the machine. */
    private void advanceMachine() {
        ArrayList<Rotor> canAdvance = new ArrayList<Rotor>();
        for (int index = _allRotors.size() - 1; index >= 0; index -= 1) {
            Rotor currRotor = _allRotors.get(index);
            if (index == _allRotors.size() - 1) {
                canAdvance.add(currRotor);
            } else if (currRotor.atNotch()) {
                Rotor nextRotor = _allRotors.get(index - 1);
                if (nextRotor.rotates()) {
                    if (!canAdvance.contains(currRotor)) {
                        currRotor.advance();
                    }
                    if (!canAdvance.contains(nextRotor)) {
                        nextRotor.advance();
                    }
                }
            } else {
                Rotor prevRotor = _allRotors.get(index + 1);
                if (prevRotor.atNotch()) {
                    currRotor.advance();
                }
            }
        }
        for (int index = 0; index < canAdvance.size(); index += 1) {
            canAdvance.get(index).advance();
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        if (msg.equals("")) {
            return msg;
        }
        String encodedMsg = "";
        for (int index = 0; index < msg.length(); index += 1) {
            char ch = msg.charAt(index);
            if (!Character.isWhitespace(ch)) {
                if (!_alphabet.contains(ch)) {
                    throw new EnigmaException("Msg sent to "
                             + "convert(...) has characters not in alphabet.");
                }
                int letterIndex = _alphabet.toInt(ch);
                encodedMsg += _alphabet.toChar(convert(letterIndex));
            } else if (ch == '\n') {
                encodedMsg += ch;
            }
        }
        return encodedMsg;
    }

    /** Returns the current settings of the rotors for this machine. */
    public HashMap<String, String> rotorSettings() {
        HashMap<String, String> ret = new HashMap<String, String>();
        for (int index = 0; index < _allRotors.size(); index += 1) {
            Rotor currRotor = _allRotors.get(index);
            int posn = currRotor.setting();
            ret.put(currRotor.name(), "" + posn);
        }
        return ret;
    }

    /** The rings of the machine as they correspond to some rotor. */
    private HashMap<String, String> _rings;

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotors in the machine. */
    private int _numRotors;

    /** The number of pawls in the machine. */
    private int _numPawls;

    /** A collection of all the rotors in the machine. */
    private ArrayList<Rotor> _allRotors;

    /** The plugboard of the machine, which is called
     * before conversion and after conversion. */
    private Permutation _plugboard;

    /** True if _plugboard defined for the machine.
     * False by default. */
    private boolean _hasPlugboard;

    /** True if _plugboard defined for the machine.
     * False by default. */
    private boolean _hasRings;

}
