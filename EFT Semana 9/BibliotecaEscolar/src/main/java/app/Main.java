package app;

import vista.LoginVista;
import javax.swing.SwingUtilities;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginVista().setVisible(true));
        }
    }