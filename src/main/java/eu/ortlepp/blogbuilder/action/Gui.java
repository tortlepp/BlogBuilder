package eu.ortlepp.blogbuilder.action;

import eu.ortlepp.blogbuilder.gui.Window;

/**
 * Action: Start the program in GUI mode.
 *
 * @author Thorsten Ortlepp
 */
public class Gui implements Action {

    /**
     * Initialize and show the GUI.
     */
    @Override
    public void run() {
        new Window().showWindow();
    }

}
