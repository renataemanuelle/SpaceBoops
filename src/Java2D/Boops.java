
package Java2D;

import java.awt.Image;
import java.util.Arrays;


public class Boops extends Tiro{
    
    static double pxboops;
    static double pyboops;
    String nomeboops;
    Image[] corboops;
    static Tiro[] tiros = new Tiro[5];

    public Boops (Image[] imagens, String nome) {

        pxboops = 100;
        pyboops = 200;
        nomeboops = nome;
        corboops = new Image[imagens.length];
        corboops = Arrays.copyOf(imagens, imagens.length);
        tiros = new Tiro[5];
        for (int i = 0; i < tiros.length; i++) {
            tiros[i] = new Tiro();
        }
        
    }
    
    public void MovimentaBoops(String direcao, double dt) {
        
        switch (direcao) {
            case "cima":
                if (pyboops > 20) {
                        pyboops = pyboops - (750 * dt);
                }
                break;
            case "baixo":
                if (pyboops < 530) {
                    pyboops = pyboops + (750 * dt);
                }
                break;
            case "esquerda":
                if (pxboops > -30) {
                    pxboops = pxboops - (600 * dt);
                }
                break;
            case "direita":
                if (pxboops < 400) {
                    pxboops = pxboops + (750 * dt);
                }
                break;
        }
        
    }
    
    public void ZerarBoops() {
        pxboops = 100;
        pyboops = 200;
    }
    
    public double GetxBoops() {
        return pxboops;
    }
    
    public double GetyBoops() {
        return pyboops;
    }
    
    public Tiro[] GetTiros() {
        return tiros;
    }
    
    public Tiro GetTiros(int n) {
        return tiros[n];
    }
    
    
}
