package application;

import controllers.MainController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.Server;

public class Application {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            MainController.getInstance().startServer(server);
            System.out.println("Server started!");
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
