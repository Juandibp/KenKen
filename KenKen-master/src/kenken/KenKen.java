package kenken;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.KenKenFrame;

public class KenKen {

    public static void main(String[] args) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(KenKen.class.getName()).log(Level.SEVERE, null, ex);
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            new KenKenFrame();
        });

    }

}
