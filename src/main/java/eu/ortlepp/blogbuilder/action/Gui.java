package eu.ortlepp.blogbuilder.action;

import eu.ortlepp.blogbuilder.gui.Window;

/**
 * Action: Start the program in GUI mode.
 *
 * @author Thorsten Ortlepp
 * @since 0.8
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
