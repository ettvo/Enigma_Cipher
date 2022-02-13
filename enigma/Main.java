package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Evelyn Vo
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        _config.close();
        setUp(M, _input.nextLine());
        while (_input.hasNextLine()) {
            String nextln = _input.nextLine();
            if (!checkIfSettings(M, nextln)) {
                String translate = M.convert(nextln);
                String ret = partitionMessage(translate);
                _output.println(ret);
            } else {
                M.setRotors(_initialPos);
            }
        }
        _input.close();
        _output.close();
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alph = _config.nextLine();
            for (char letter: alph.toCharArray()) {
                boolean isWS = Character.isWhitespace(letter);
                if (letter == ')' || letter == '(' || letter == '*' || isWS) {
                    throw new EnigmaException("Invalid alphabet "
                            + "massed to Machine.");
                }
            }
            _alphabet = new Alphabet(alph);
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            _config.nextLine();
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                Rotor currRotor = readRotor();
                if (allRotors.contains(currRotor)) {
                    throw new EnigmaException("Rotor repeated in "
                            + "conf file sent to Main.");
                }
                allRotors.add(currRotor);
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorName = _config.next();
            char[] rotorSettings = _config.next().toCharArray();
            String notches = "";
            for (int index = 1; index < rotorSettings.length; index += 1) {
                notches += rotorSettings[index];
            }
            String cycles = "";
            Pattern cyc = Pattern.compile("\\s*\\([^()*]+\\)");
            while (_config.hasNext(cyc)) {
                cycles += _config.next(cyc);
            }
            if (_config.hasNext()) {
                _config.nextLine();
            }
            while (_config.hasNext(cyc)) {
                cycles += _config.next(cyc);
            }
            Permutation rotorPerm = new Permutation(cycles, _alphabet);
            char type = rotorSettings[0];
            if (type == 'M') {
                return new MovingRotor(rotorName, rotorPerm, notches);
            } else if (type == 'N') {
                if (!notches.equals("")) {
                    throw new EnigmaException("Fixed rotor "
                            + "cannot have notches.");
                }
                return new FixedRotor(rotorName, rotorPerm);
            } else if (type == 'R') {
                if (!notches.equals("")) {
                    throw new EnigmaException("Reflector "
                            + "cannot have notches.");
                }
                return new Reflector(rotorName, rotorPerm);
            } else {
                throw new EnigmaException("Rotor must be a "
                        + "moving rotor (M), fixed rotor (N) "
                        + "or a reflector (R).");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment.
     *  In other words, settings should already have been checked in
     *  process(). */
    private void setUp(Machine M, String settings) {
        String[] rotorsSel = new String[M.numRotors()];
        String initialPos = "";
        String plgbdCycles = "";
        _settings = "* ";
        Scanner readSettings = new Scanner(settings);
        if (!readSettings.hasNext()) {
            throw new EnigmaException("Cannot have an empty settings line"
                    + " or only spaces as input.");
        }
        if (!readSettings.next().equals("*")) {
            throw new EnigmaException("Settings line must have"
                    + " an asterisk in the first column.");
        }
        for (int index = 0; index < M.numRotors(); index += 1) {
            if (!readSettings.hasNext()) {
                throw new EnigmaException("Incorrect number of rotors sent to "
                        + " settings.");
            }
            String rotor = readSettings.next();
            rotorsSel[index] = rotor;
            _settings += rotor + " ";
        }
        if (!readSettings.hasNext()) {
            throw new EnigmaException("Does not have initial settings.");
        }
        initialPos = readSettings.next();
        _initialPos = initialPos;
        _settings += initialPos;
        String rings = "";

        if (readSettings.hasNext(Pattern.compile("\\s*[^()*]+\\s*"))) {
            rings = readSettings.next();
        }

        if (readSettings.hasNext(Pattern.compile("\\s*\\([^()*]\\)\\s*"))) {
            plgbdCycles = readSettings.nextLine();
            _settings += " " + plgbdCycles;
        }
        M.insertRotors(rotorsSel);
        M.setRotors(initialPos);
        M.setRings(rings);
        if (!plgbdCycles.equals("")) {
            M.setPlugboard(new Permutation(plgbdCycles, _alphabet));
        }
    }

    /** Checks if the input is a settings line and returns True if it is.
     * If the input line IS a settings line, ignores it if it matches with
     * the settings of the machine and errors otherwise. Does not error
     * if there is a variable n >= 1 number of whitespace characters
     * between entries.
     * @param input The input from _input
     * @param M The machine
     */
    private boolean checkIfSettings(Machine M, String input) {
        if (input.equals(_settings)) {
            return true;
        } else if (input.matches("\\s+") || input.equals("")) {
            return false;
        }
        Scanner entries = new Scanner(input);
        if (!entries.next().equals("*")) {
            return false;
        }
        setUp(M, input);
        entries.close();
        return true;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String remainingString = msg;
        String currentString = "";
        for (int indices = 0; indices < msg.length(); indices += 5) {
            currentString = remainingString.substring(0, 5);
            System.out.println(currentString);
            remainingString = remainingString.substring(5);
        }
        System.out.println(remainingString);
        System.out.println();
    }

    /** Returns MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private String partitionMessage(String msg) {
        String remainingString = msg;
        String currentString = "";
        String ret = "";
        for (int indices = 0; indices < msg.length(); indices += 5) {
            if (remainingString.length() < 5) {
                currentString = remainingString;
                remainingString = "";
            } else {
                currentString = remainingString.substring(0, 5);
                remainingString = remainingString.substring(5);
            }
            if (indices == 0) {
                ret += currentString;
            } else {
                ret += " " + currentString;
            }
        }
        ret += remainingString;
        return ret;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Configurations at start. Are ignored if passed again
     * but errors if it changed later. */
    private String _settings;

    /** The rotors selected for the machine, ordered. */
    private String[] _rotorsSel;

    /** The cycles selected for the machine plugboard. */
    private String _plgbdCycles;

    /** The initial settings selected for the machine. */
    private String _initialPos;


}
