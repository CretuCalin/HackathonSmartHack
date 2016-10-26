package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import remote.RemoteProcs;
import rpc.HiRpc;
import visual.LogInFrame;

public class Application {

    public static void main(String[] args) {
        String ip = "localhost";
        if (args.length > 0) {
            ip = args[0];
        }
        
        try {
            for (UIManager.LookAndFeelInfo inf : UIManager.getInstalledLookAndFeels()) {
                if (inf.getName().equalsIgnoreCase("Nimbus")) {
                    UIManager.setLookAndFeel(inf.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            RemoteProcs c = HiRpc.connectSimple(ip, RemoteProcs.PORT, RemoteProcs.class);
            new LogInFrame(c).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
