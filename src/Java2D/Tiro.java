
package Java2D;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Tiro {
    
    boolean atirando;
    double pxtiro;
    double pytiro;
    Image cordotiro;
    String cor;

    
    public Tiro() {
        
        atirando = false;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroVazio.png")).getImage();
        pxtiro = -10;
        pytiro = -10;
        cor = "";
        
    }
    
    public void AdicionaTiro(double px, double py, String img, String c) {
        atirando = true;
        cordotiro = new ImageIcon(getClass().getResource(img)).getImage();
        pxtiro = px;
        pytiro = py;
        cor = c;
    }
    
    public static int Atirar(Tiro[] tiros) throws SemTirosEx {
        
        int numero;
        for (int i = 0; i < tiros.length; i++) {
            if (tiros[i].atirando == false) {
                numero = i;
                return numero;
            }
        }
        throw new SemTirosEx("CABÔ TIRO!");
                
    }
    
    public void AtualizaTiro(double dt) {
        pxtiro = (pxtiro + 800 * dt);
    }
    
    public void ZerarTiro() {
        atirando = false;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroVazio.png")).getImage();
        pxtiro = -10;
        pytiro = -10;
        cor = "";
    }
    
    public void TestaTiro(Inimigo[] inimigos) {
        
        if (pxtiro > 1100) {
            ZerarTiro();
        }
        for (Inimigo inimigo : inimigos) {
            if (pxtiro >= inimigo.pxinimigo && pxtiro <= (inimigo.pxinimigo+inimigo.cordoinimigo.getWidth(null))
                && pytiro >=inimigo.pyinimigo  && pytiro <=(inimigo.pyinimigo+inimigo.cordoinimigo.getHeight(null))
                && inimigo.cor.equals(cor)) {
                inimigo.ZerarInimigo();
                ZerarTiro();
                CanvasPanel.tocarMusica("src/Java2D/SOns/Explode.wav", 0);
                CanvasPanel.pontos += 100;
                if (CanvasPanel.pontos > 0 && (CanvasPanel.pontos % 1000) == 0 ) {
                    CanvasPanel.level.AumentaLevel((int)(Math.log10(CanvasPanel.pontos)/Math.log10(2) - 7));
                }
            }
        }
           
    }
    
}
