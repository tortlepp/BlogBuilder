package eu.ortlepp.blogbuilder.model;

/**
 * An Enum for all parameters / command line arguments.
 *
 * @author Thorsten Ortlepp
 */
public enum Parameter {

    /** Parameter to build a project. */
    BUILD("--build", "<DIRECTORY>" , "Build the project in <DIRECTORY>"),

    /** Parameter to initialize a project. */
    INITIALIZE("--init", "<DIRECTORY>", "Initialize a new project in <DIRECTORY>"),

    /** Parameter to start the program in GUI mode. */
    GUI("--gui", "", "Show the graphical interface");


    /** The name of the parameter. */
    private final String name;

    /** The value of the parameter. */
    private final String value;

    /** The description of the parameter. */
    private final String description;


    /**
     * Constructor to create a single Enum element. All fields are initialized with
     * the given values.
     *
     * @param name The name of the parameter
     * @param value The value of the parameter
     * @param description The description of the parameter
     */
    Parameter(final String name, final String value, final String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }


    /**
     * Constructor to create a single Enum element. Name and description are initialized
     * with the given values. Value is initialized as empty string to indicate that the
     * parameter does not expect a value.
     *
     * @param name The name of the parameter
     * @param description The description of the parameter
     */
    Parameter(final String name, final String description) {
        this.name = name;
        this.value = "";
        this.description = description;
    }


    /**
     * Return a parameter info. The info consists of the name and
     * value of a parameter and its description.
     *
     * @return The parameter info
     */
    public String getParamInfo() {
        final String usage = String.format(" %s %s", name, value);
        return String.format("%-22s - %s", usage, description);
    }


    /**
     * Custom implementation of the toString method, returns the name of the
     * parameter (because it identifies the Parameter).
     *
     * @return The name of the parameter
     */
    @Override
    public String toString() {
        return name;
    }


    /**
     * Check if an input string is a valid parameter.
     *
     * @param input The string to check
     * @return The result of the check; true = valid parameter, false = no valid parameter
     */
    public static boolean isValidParam(final String input) {
        for (final Parameter parameter : Parameter.values()) {
            if (parameter.toString().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

}
