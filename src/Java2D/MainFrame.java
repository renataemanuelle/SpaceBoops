
package Java2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame { 


    public MainFrame() {
        
        setTitle("SpaceBoops");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new CanvasPanel());
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
    } 
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new MainFrame().setVisible(true);
            }
        });
    }
}
