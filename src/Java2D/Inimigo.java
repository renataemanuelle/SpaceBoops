
package Java2D;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Inimigo extends Tiro {

    boolean ativo;
    Image cordoinimigo;
    double pxinimigo;
    double pyinimigo;
    int tempodetiro;
    boolean testemovx = true;
    boolean testemovy = true;
    
    public Inimigo() {
        
        ativo = false;
        cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy1.png")).getImage();
        pxinimigo = 1300;
        pyinimigo = 1300;
        atirando = false;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroVazio.png")).getImage();
        pxtiro = 1300;
        pytiro = 1300;
        cor = "";
        tempodetiro = 0;
        
    }
    
    public Inimigo(String c, double x, double y) {
        
        ativo = true;
        switch (c) {
            case "rosa": cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy1.png")).getImage(); break;
            case "roxo": cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy2.png")).getImage(); break;
        }
        pxinimigo = x;
        pyinimigo = y;
        atirando = false;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroVazio.png")).getImage();
        pxtiro = 1300;
        pytiro = 1300;
        cor = c;
        tempodetiro = 0;
        
    }
    
    public static void AdicionaInimigo(Inimigo[] inimigos, double px, double py, String c) {
        
        for ( Inimigo inimigo : inimigos ) {
            if (inimigo.ativo == false) {
                inimigo.AdicionaInimigo(px, py, c);
                break;
            }
        }
    }
    
    private void AdicionaInimigo(double px, double py, String c) {
        
        ativo = true;
        switch (c) {
            case "rosa": cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy1.png")).getImage(); break;
            case "roxo": cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy2.png")).getImage(); break;
        }
        pxinimigo = px;
        pyinimigo = py;
        cor = c;
        
    }
    
    public void AdicionaTiro(double px, double py) {
        atirando = true;
        pxtiro = px;
        pytiro = py;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroInimigo.png")).getImage(); 
    }
    
    @Override
    public void AtualizaTiro(double dt) {
        pxtiro = (pxtiro - (800 * dt));
    }
    
    public void MovimentaInimigo(double dt) {

        int numero = CanvasPanel.randInt(1, 3);
        
        switch (numero) {
            case 1: //move na horizontal
                if (testemovx == true && pxinimigo > 500)    pxinimigo = (pxinimigo - (300 * dt));
                else if (pxinimigo <= 500)   testemovx=false;
                if (testemovx==false && pxinimigo < 1050) pxinimigo = pxinimigo + (300  * dt);
                else if (pxinimigo >= 1050) testemovx=true;
                break;      
            case 2: //move na vertical
                if (testemovy == true && pyinimigo < 535)    pyinimigo = (pyinimigo + (300  * dt));
                else if (pyinimigo >= 535)   testemovy=false;
                if (testemovy == false && pyinimigo > 15)     pyinimigo = (pyinimigo - (300  * dt));
                else if (pyinimigo <= 15)   testemovy=true;
                break;
            case 3: //move na diagonal
                if (testemovx == true && pxinimigo > 500)    pxinimigo = (pxinimigo - (300  * dt));
                else if (pxinimigo <= 500)   testemovx=false;
                if (testemovx==false && pxinimigo < 1050) pxinimigo = pxinimigo + (300  * dt);
                else if (pxinimigo >= 1050) testemovx=true;
                if (testemovy == true && pyinimigo < 535)    pyinimigo = (pyinimigo + (300  * dt));
                else if (pyinimigo >= 535)   testemovy=false;
                if (testemovy == false && pyinimigo > 15)     pyinimigo = (pyinimigo - (300 * dt));
                else if (pyinimigo <= 15)   testemovy=true;  
                break;
        }
        
    }
    
    public void MovimentaInimigoPreso(double dt) {        
        if (testemovx == true && pxinimigo > 360)    pxinimigo = (pxinimigo - (100 * dt));
        else if (pxinimigo <= 360)   testemovx=false;
        if (testemovx==false && pxinimigo < 630) pxinimigo = pxinimigo + (100  * dt);
        else if (pxinimigo >= 630) testemovx=true;
        if (testemovy == true && pyinimigo > 150)    pyinimigo = (pyinimigo - (100 * dt));
        else if (pyinimigo <= 150)   testemovy=false;
        if (testemovy==false && pyinimigo < 410) pyinimigo = pyinimigo + (100  * dt);
        else if (pyinimigo >= 410) testemovy=true;  
    }
    
    public void ZerarInimigo () {
        ativo = false;
        cordoinimigo = new ImageIcon(getClass().getResource("Imagens/Enemy1.png")).getImage();
        pxinimigo = 1300;
        pyinimigo = 1300;
        cor = "";
        tempodetiro = 0;
    }
    
    @Override
    public void ZerarTiro() {
        atirando = false;
        cordotiro = new ImageIcon(getClass().getResource("Imagens/TiroVazio.png")).getImage();
        pxtiro = 1300;
        pytiro = 1300;
    }
    
    
    public void TestaTiro(double pxboneca, double pyboneca) {
        
        if (pxtiro < -10) {
            ZerarTiro();
        }
        if (CanvasPanel.escudo == false) {
            if (pxtiro >= pxboneca && pxtiro <= (pxboneca+61) && (pytiro+12) >= pyboneca && (pytiro+12) <= (pyboneca+73)) {
                ZerarTiro();
                CanvasPanel.energia--;
                CanvasPanel.tocarMusica("src/Java2D/Sons/Dano.wav", 0);
            }   
        } else {
            if (pxtiro >= (pxboneca-15) && pxtiro <= (pxboneca+78) && (pytiro+12) >= pyboneca-15 && (pytiro+12) <= (pyboneca+88)) {
                ZerarTiro();
            }
        }
        
    }

}