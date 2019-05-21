package Java2D;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CanvasPanel extends JPanel implements Runnable {
    
    //variável posição pontos fundo
    double pxponto = 0;
    double pyponto = 100;
    
    //meninas
    Boops[] meninas = new Boops[2];
    //variável Imagens meninas
    Image[] Haruka = new Image[3];
    Image[] Ikaruga = new Image[3];
    //posição inicial do vetor de imagens
    int indicepeitos = 0;
    //variaveis de escolha das meninas
    boolean selec_rosa = true;
    boolean selec_roxo = false;
    int meninaselec = 0;
    
    //bonecas tela inicial
    double pymenina1 = 100;
    double pymenina2 = 500;
    boolean testesobedesce;
    
    //nivel
    public static Level level;
    
    //contadores - vidas, energia e pontos
    public static int pontos;
    public static int vidas;
    public static int energia;
    public static boolean escudo; 
    int carregaescudo;
    int highscore;
    Image ienergia;
    Image ienergiavazia;
    Image iescudo;
    Image iescudogrande;
    
    //tiro
    int ntiro;
    String temtiro = "";
   
    //Inimigos
    Inimigo[] inimigos = new Inimigo[15];
        
    //contadores auxiliares
    int contpeitos = 0; //contagem para mudança da imagem
    int contspace = 0; //contador para pressionamento da barra de espaço
    int contpause = 0; //contador para pressionamento da letra P para pausar
    int continimigo = 0; //contagem pra criação de novos inimigos
    int contescudo = 0; //contagem pra carregar o escudo
    
    //qual tela selecionar
    int tela = 0;
    
    //barra de seleção tela gameover
    double pxbarra = 620;

    //teclas
    private final boolean[] key_states = new boolean[256];
    
    //atribuição cores
    Color amarelo = new Color(255, 242, 0);
    Color azulclaro = new Color(152, 209, 250);
    Color azulescuro = new Color(9, 9, 100);
    Color branco = new Color (255, 255, 255);
    Color cinzaclaro = new Color(100, 100, 100);
    Color cinzaescuro = new Color(50, 50, 50);
    Color laranja = new Color(255, 136, 0);
    Color marrom = new Color(120, 76, 0);
    Color preto = new Color(0, 0, 0);
    Color rosa = new Color (224, 143, 175);
    Color roxo = new Color (156, 0, 234);
    Color verde = new Color(40, 155, 0);
    Color corbarra;
    
    //fonte
    Font novafonte = new Font("Corbel", Font.PLAIN, 18);
    Font fontebold = new Font("Corbel", Font.BOLD, 23);
    Font fontegrande = new Font("Corbel", Font.BOLD, 100);
    
    //arquivo
    BufferedWriter file;
    BufferedReader fileread;
    
    public CanvasPanel() {

        //Carregando Imagens Boneca 1
        Haruka[0] = new ImageIcon(this.getClass().getResource("Imagens/Haruka1.png")).getImage();
        Haruka[1] = new ImageIcon(this.getClass().getResource("Imagens/Haruka2.png")).getImage();
        Haruka[2] = new ImageIcon(this.getClass().getResource("Imagens/Haruka3.png")).getImage();        
        
        //Carregando Imagens Boneca 2
        Ikaruga[0] = new ImageIcon(this.getClass().getResource("Imagens/Ikaruga1.png")).getImage();
        Ikaruga[1] = new ImageIcon(this.getClass().getResource("Imagens/Ikaruga2.png")).getImage();
        Ikaruga[2] = new ImageIcon(this.getClass().getResource("Imagens/Ikaruga3.png")).getImage();
        
        //carregando vetor de inimigos
        for (int i = 0; i < inimigos.length; i++) {
            inimigos[i] = new Inimigo();
        }
        
        //inimigos tela inicial
        inimigos[0] = new Inimigo("roxo", 350, 250);
        inimigos[1] = new Inimigo("rosa", 480, 150);
        inimigos[2] = new Inimigo("rosa", 510, 400);
        inimigos[3] = new Inimigo("roxo", 600, 300);

        meninas[0] = new Boops(Haruka, "Haruka");
        meninas[1] = new Boops(Ikaruga, "Ikaruga");
 
        setDoubleBuffered(true);
        setFocusable(true);
        load();
        new Thread(this).start();
        
    }
    
    private class KeyboardAdapter extends KeyAdapter {
        
        @Override
        public void keyReleased(KeyEvent e) {
           key_states[e.getKeyCode()] = false;
        }
        
        @Override    
        public void keyPressed(KeyEvent e) {
            key_states[e.getKeyCode()] = true;
        }
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    @Override
    public void run() {
        
        double btime, dtime = 1;
        btime = System.currentTimeMillis();
        while(true) {
            update(dtime/1000);
            repaint();                       
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            dtime = (System.currentTimeMillis() - btime); 
            btime = System.currentTimeMillis();
        }
        
    }

    private void load() {  
        setBackground(preto);
        ienergia = new ImageIcon(getClass().getResource("Imagens/EnergiaCheia.png")).getImage(); //imagem energia
        ienergiavazia = new ImageIcon(getClass().getResource("Imagens/EnergiaVazia.png")).getImage(); //imagem energia vazia
        iescudo = new ImageIcon(getClass().getResource("Imagens/BubbleShield.png")).getImage(); //imagem escudo
        iescudogrande = new ImageIcon(getClass().getResource("Imagens/BubbleShield350.png")).getImage(); //imagem escudo tela inicial
        addKeyListener(new KeyboardAdapter());
        try { //arquivo highscore
            fileread = new BufferedReader(new InputStreamReader(new FileInputStream("highscore.txt")));
            String shighscore = fileread.readLine();
            if (shighscore != null) {
                highscore = Integer.parseInt(shighscore);
            } else {
                highscore = 0;
            }
            fileread.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro: " + ex.getMessage());
            highscore = 0;
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
            highscore = 0;
        }
        tocarMusica("src/Java2D/Sons/AbreEscudo.wav", 0);
    }

    private void update(double dt) {
        
        //posição dos pontos de fundo
        pxponto = randInt(0, 1100);
        pyponto = randInt(40, 600);
        
        switch (tela) {
            case 1: //jogo iniciado
                //inicio implementação das teclas
                if (key_states[KeyEvent.VK_RIGHT] || key_states[KeyEvent.VK_D]) {
                    meninas[meninaselec].MovimentaBoops("direita", dt);
                }
                if (key_states[KeyEvent.VK_LEFT] || key_states[KeyEvent.VK_A]) {
                    meninas[meninaselec].MovimentaBoops("esquerda", dt);
                }
                if (key_states[KeyEvent.VK_UP]|| key_states[KeyEvent.VK_W]) {
                    meninas[meninaselec].MovimentaBoops("cima", dt);
                }
                if (key_states[KeyEvent.VK_DOWN] || key_states[KeyEvent.VK_S]) {
                    meninas[meninaselec].MovimentaBoops("baixo", dt);
                }
                //troca boneca
                if (key_states[KeyEvent.VK_Q] || key_states[KeyEvent.VK_X]) {
                    selec_rosa = true;
                    selec_roxo = false;
                }
                if (key_states[KeyEvent.VK_E] || key_states[KeyEvent.VK_C]) {
                    selec_rosa = false;
                    selec_roxo = true;
                }
                //escudo
                if (key_states[KeyEvent.VK_F]) {
                    if (carregaescudo >= 10) {
                        escudo=true;
                        tocarMusica("src/Java2D/Sons/AbreEscudo.wav", 0);
                    }
                }
                //pausa o jogo
                if (key_states[KeyEvent.VK_P] && contpause == 0) {
                    contpause++; //evita que mude de tela mais de uma vez
                    tela = 4;
                } else if (key_states[KeyEvent.VK_P] == false) {
                    contpause = 0;
                }
                //atira
                if (key_states[KeyEvent.VK_SPACE] && contspace == 0) {
                    contspace++; //evita que atire mais de um tiro a cada clique
                    try {
                        ntiro = Boops.Atirar(meninas[meninaselec].GetTiros());
                        if (selec_rosa) {   
                            meninas[meninaselec].GetTiros(ntiro).AdicionaTiro((meninas[meninaselec].GetxBoops()+62), (meninas[meninaselec].GetyBoops()+30), "Imagens/TiroRosa.png", "rosa");
                        }
                        if (selec_roxo) {
                            meninas[meninaselec].GetTiros(ntiro).AdicionaTiro((meninas[meninaselec].GetxBoops()+62), (meninas[meninaselec].GetyBoops()+30), "Imagens/TiroRoxo.png", "roxo");
                        }
                        tocarMusica("src/Java2D/Sons/Tiro.wav", 0);
                    } catch (SemTirosEx e) {
                        tocarMusica("src/Java2D/Sons/SemTiro.wav", 0);
                    }
                } else if (key_states[KeyEvent.VK_SPACE] == false) {
                    contspace = 0;
                }
                
                //mostra na tela se o tiro acabou
                try {
                    Boops.Atirar(meninas[meninaselec].GetTiros());
                    temtiro = "";
                } catch (SemTirosEx e) {
                    temtiro = e.getMessage();
                }
                
                //movimentação contínua da boneca
                contpeitos++;
                if(contpeitos>=150) {    
                    indicepeitos++;
                    if(indicepeitos>=(meninas[meninaselec].corboops).length) {
                        indicepeitos=0;
                    }
                    contpeitos=0;
                }

                //muda imagem da boneca selecionada
                if (selec_rosa) {
                    meninaselec = 0;
                    corbarra = rosa;
                }
                if (selec_roxo) {
                    meninaselec = 1;
                    corbarra = roxo;                    
                }

                //perdendo vida
                if (energia <= 0) {
                    vidas--;
                    energia = 10;
                }
                //fim do jogo
                if (vidas <= 0) {
                    //zera tiros e inimigos
                    for ( Inimigo inimigo : inimigos ) {
                        inimigo.ZerarInimigo();
                        inimigo.ZerarTiro();
                    }
                    for ( Tiro tiro : meninas[meninaselec].GetTiros() ) {                      
                        tiro.ZerarTiro();
                    }
                    //salva pontuação no arquivo
                    try {
                        file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("highscore.txt", false)));
                        if (highscore <= pontos) {
                            highscore = pontos;
                            file.append(String.valueOf(highscore));
                        } else {
                            file.append(String.valueOf(highscore));
                        }
                        file.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("Erro: " + ex.getMessage());
                    } catch (IOException ex) {
                        System.out.println("Erro: " + ex.getMessage());
                    }
                    tocarMusica("src/Java2D/Sons/FimDeJogo.wav", 0);
                    tela = 3;
                }
                
                //carrega escudo
                contescudo++;
                if (contescudo >= 1500 && carregaescudo <10 && escudo==false) {
                    carregaescudo++;
                    contescudo = 0;
                } else if (contescudo >= 1000 && carregaescudo >-5 && escudo==true) {
                    carregaescudo--;
                    contescudo = 0;
                }
                if (carregaescudo <= 0) {
                    escudo = false;
                }
                
                //nivel
                level.SetLevel();
                
                //cria inimigo
                continimigo = continimigo + (randInt(0, 3));
                if (continimigo >= level.tempoinimigo) {
                    if ((randInt(1,2)) == 1) {
                        Inimigo.AdicionaInimigo(inimigos, randInt(700, 1100), randInt(20, 510), "rosa");
                    } else {
                        Inimigo.AdicionaInimigo(inimigos, randInt(700, 1100), randInt(20, 510), "roxo");
                    }
                    continimigo = 0;
                }      
                //cria tiro inimigo
                for (Inimigo inimigo : inimigos) {
                    if (inimigo.ativo) {
                        inimigo.tempodetiro = inimigo.tempodetiro + (randInt(0, 3));
                        if (inimigo.tempodetiro >= level.tempotiro && inimigo.atirando == false) {
                            inimigo.AdicionaTiro(inimigo.pxinimigo, inimigo.pyinimigo+25);
                            inimigo.tempodetiro = 0;
                            inimigo.AtualizaTiro(dt);
                            inimigo.TestaTiro(meninas[meninaselec].GetxBoops(), meninas[meninaselec].GetyBoops());
                        }
                    }
                }
                //Atualiza posição inimigo e tiro inimigo
                for (Inimigo inimigo : inimigos) {
                    if (inimigo.ativo) {
                        inimigo.MovimentaInimigo(dt);
                    }
                    if (inimigo.atirando) {
                        inimigo.AtualizaTiro(dt);
                        inimigo.TestaTiro(meninas[meninaselec].GetxBoops(), meninas[meninaselec].GetyBoops());
                    }
                }
                //atualiza posição tiro
                for ( Tiro tiro : meninas[meninaselec].GetTiros() ) {
                    if (tiro.atirando) {
                        tiro.AtualizaTiro(dt);
                        tiro.TestaTiro(inimigos);
                    }
                }
                break;
                
            case 2: //Sobre o jogo
                if (key_states[KeyEvent.VK_ESCAPE]) {
                    tela = 0;
                }
                break;
                
            case 3://fim de jogo
                if (key_states[KeyEvent.VK_RIGHT]) {
                    pxbarra = 620;
                    tocarMusica("src/Java2D/Sons/Select.wav", 0);
                } 
                if (key_states[KeyEvent.VK_LEFT]) {
                    pxbarra = 380;
                    tocarMusica("src/Java2D/Sons/Select.wav", 0);
                }
                if (key_states[KeyEvent.VK_ENTER]) {
                    if (pxbarra == 380) { //reinicia jogo
                        IniciaJogo();
                    } else { //volta a tela inicial
                        inimigos[0] = new Inimigo("roxo", 350, 250);
                        inimigos[1] = new Inimigo("rosa", 480, 150);
                        inimigos[2] = new Inimigo("rosa", 510, 400);
                        inimigos[3] = new Inimigo("roxo", 600, 300);
                        indicepeitos = 0;
                        tela = 0;
                    }
                }
                break;
                
            case 4: //jogo pausado
                if (key_states[KeyEvent.VK_P] && contpause == 0) {
                    contpause++; //evita que mude de tela mais de uma vez
                    tela = 1;
                } else if (key_states[KeyEvent.VK_P] == false) {
                    contpause = 0;
                }
                break;
                
            default: //tela inicial
                contpeitos++;
                if(contpeitos>=150) {    
                    indicepeitos++;
                    if(indicepeitos>=(meninas[meninaselec].corboops).length) {
                        indicepeitos=0;
                    }
                    contpeitos=0;
                }
                if (testesobedesce == true && pymenina1 > 100) {
                pymenina1 = pymenina1 - (150 * dt);
                pymenina2 = pymenina2 + (150 * dt);
                } else if (pymenina1 <= 100) {
                    testesobedesce=false;
                }
                if (testesobedesce==false && pymenina1 < 500) {
                    pymenina1 = pymenina1 + (150 * dt);
                    pymenina2 = pymenina2 - (150 * dt);
                } else if (pymenina1 >= 500) {
                    testesobedesce=true;
                }
                for (int i = 0; i < 4; i++) {
                    inimigos[i].MovimentaInimigoPreso(dt);
                }
                if (key_states[KeyEvent.VK_SPACE]) {
                    contspace++;
                    IniciaJogo();
                }
                if (key_states[KeyEvent.VK_A]) {
                    tela = 2;
                }
                break;
        }

    }
    
    private void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        //barra superior
        g2d.setColor(branco);
        g2d.fill(new Rectangle2D.Double(0, 40, 1100, 3));
        //Pontuação
        g2d.setColor(branco);
        g2d.setFont(novafonte);
        g2d.drawString("Pontuação:", 25, 25);
        g2d.drawString(String.valueOf(pontos), 150, 25);
        g2d.drawString("HighScore:", 900, 25);
        g2d.drawString(String.valueOf(highscore), 1000, 25);
        //barra inferior
        g2d.setColor(branco);
        g2d.fill(new Rectangle2D.Double(0, 600, 1100, 3));
        //pontos fundo
        for (int i = 0; i < 100; i++) {
            g2d.setColor(branco);
            g2d.fill(new Ellipse2D.Double((int)pxponto, (int)pyponto, 4, 4));
        }
        
        switch (tela) {
            case 1: //jogo iniciado
                //nivel
                g2d.setColor(branco);
                g2d.setFont(novafonte);
                g2d.drawString("Level " + (String.valueOf(level.level)), 500, 25);
                //informação de tiro
                g2d.setColor(branco);
                g2d.setFont(novafonte);
                g2d.drawString(temtiro, 20, 590);
                //barra inferior cor
                g2d.setColor(corbarra);
                g2d.fill(new Rectangle2D.Double(0, 603, 1100, 50));
                //dados barra inferior
                g2d.setColor(preto);
                g2d.setFont(fontebold);
                g2d.drawString(meninas[meninaselec].nomeboops, 50, 635);
                g2d.draw(new Rectangle2D.Double(160, 620, 100, 20));
                int pxescudo = 160;
                for (int i = 1; i <= carregaescudo; i++) {
                    g2d.fill(new Rectangle2D.Double((int)pxescudo, 620, 10, 20));
                    pxescudo +=10;
                }
                int pxenerg = 460;
                for (int i = 1; i <= 10; i++) {
                    if (i > energia) {
                        g2d.drawImage(ienergiavazia, (int)pxenerg, 620, this);
                        pxenerg += 15;    
                    } else {
                        g2d.drawImage(ienergia, (int)pxenerg, 620, this);
                        pxenerg += 15;
                    }
                }
                g2d.drawString("Vidas: ", 900, 635);
                g2d.drawString(String.valueOf(vidas), 1000, 635);
                //boneca
                g2d.drawImage(meninas[meninaselec].corboops[indicepeitos], (int)meninas[meninaselec].GetxBoops(), (int)meninas[meninaselec].GetyBoops(), this);
                if (escudo) {
                    g2d.drawImage(iescudo, (int)(meninas[meninaselec].GetxBoops() - 15), (int)(meninas[meninaselec].GetyBoops() - 15), this);
                }
                //inimigo
                for ( Inimigo inimigo : inimigos ) {
                    g2d.drawImage(inimigo.cordoinimigo, (int)inimigo.pxinimigo, (int)inimigo.pyinimigo, this);
                }
                //tiro
                for ( Tiro tiro : meninas[meninaselec].GetTiros() ) {
                    g2d.drawImage(tiro.cordotiro, (int)tiro.pxtiro, (int)tiro.pytiro, this);
                }
                //tiro do inimigo
                for (Inimigo inimigo : inimigos) {
                    g2d.drawImage(inimigo.cordotiro, (int) inimigo.pxtiro, (int) inimigo.pytiro, this);
                }
                break;
                
            case 2: //Sobre o jogo
                g2d.setColor(branco);
                g2d.setFont(fontebold);
                g2d.drawString("COMO JOGAR", 470, 80);
                g2d.drawString("As Boops só eliminam inimigos de mesma cor", 310, 120);
                g2d.drawString("Q ou X seleciona Haruka (Boops Rosa)", 340, 160);
                g2d.drawString("E ou C seleciona Ikaruga (Boops Roxa)", 340, 200);
                g2d.drawString("F ativa o escudo quando a barra estiver cheia", 310, 240);
                g2d.drawString("Atire usando a barra de ESPAÇO", 380, 280);
                g2d.drawString("Para mover as Boops use as SETAS ou as letras WASD", 280, 320);
                g2d.drawString("Aperte P para pausar o jogo", 400, 360);
                g2d.drawLine(0, 390, 1100, 390);
                g2d.drawString("Jogo desenvolvido por:", 420, 430);
                g2d.drawString("Danielle Taborda", 450, 470);
                g2d.drawString("Renata Emanuelle", 445, 510);
                g2d.drawLine(0, 530, 1100, 530);
                g2d.drawString("Aperte ESC para voltar a tela inicial", 380, 570);
                break;
                
            case 3: //fim de jogo
                g2d.setColor(branco);
                g2d.setFont(fontebold);
                g2d.drawString("FIM DE JOGO", 450, 240);
                g2d.drawString("VOCÊ PERDEU!", 435, 300);
                g2d.drawString("Deseja iniciar um novo jogo?", 380, 360);
                g2d.drawString("SIM", 380, 420);
                g2d.drawString("NÃO", 620, 420);
                g2d.fill(new Rectangle2D.Double((int)pxbarra, 430, 20, 5));
                break;
                
            case 4: //pause
                g2d.setColor(branco);
                g2d.setFont(fontebold);
                g2d.drawString("JOGO PAUSADO", 460, 350);
                //nivel
                g2d.setColor(branco);
                g2d.setFont(novafonte);
                g2d.drawString("Level " + (String.valueOf(level.level)), 500, 25);
                //barra inferior cor
                g2d.setColor(corbarra);
                g2d.fill(new Rectangle2D.Double(0, 603, 1100, 50));
                //dados barra inferior
                g2d.setColor(preto);
                g2d.setFont(fontebold);
                g2d.drawString(meninas[meninaselec].nomeboops, 50, 635);
                g2d.draw(new Rectangle2D.Double(160, 620, 100, 20));
                pxescudo = 160;
                for (int i = 1; i <= carregaescudo; i++) {
                    g2d.fill(new Rectangle2D.Double((int)pxescudo, 620, 10, 20));
                    pxescudo +=10;
                }
                pxenerg = 460;
                for (int i = 1; i <= 10; i++) {
                    if (i > energia) {
                        g2d.drawImage(ienergiavazia, (int)pxenerg, 620, this);
                        pxenerg += 15;    
                    } else {
                        g2d.drawImage(ienergia, (int)pxenerg, 620, this);
                        pxenerg += 15;
                    }
                }
                g2d.drawString("Vidas: ", 900, 635);
                g2d.drawString(String.valueOf(vidas), 1000, 635);
                break;
                
            default: //menu inicial
                int pytitulo = 140;
                int pxtitulo = 200;
                g2d.setFont(fontegrande);
                g2d.setColor(rosa);
                g2d.drawString("S", pxtitulo, pytitulo);
                pxtitulo += 60;
                g2d.setColor(roxo);
                g2d.drawString("P", pxtitulo, pytitulo);
                pxtitulo += 60;
                g2d.setColor(rosa);
                g2d.drawString("A", pxtitulo, pytitulo);
                pxtitulo += 65;
                g2d.setColor(roxo);
                g2d.drawString("C", pxtitulo, pytitulo);
                pxtitulo += 60;
                g2d.setColor(rosa);
                g2d.drawString("E", pxtitulo, pytitulo);
                pxtitulo += 120;
                g2d.setColor(roxo);
                g2d.drawString("B", pxtitulo, pytitulo);
                pxtitulo += 65;
                g2d.setColor(rosa);
                g2d.drawString("O", pxtitulo, pytitulo);
                pxtitulo += 70;
                g2d.setColor(roxo);
                g2d.drawString("O", pxtitulo, pytitulo);
                pxtitulo += 70;
                g2d.setColor(rosa);
                g2d.drawString("P", pxtitulo, pytitulo);
                pxtitulo += 60;
                g2d.setColor(roxo);
                g2d.drawString("S", pxtitulo, pytitulo);
                
                g2d.setFont(fontebold);
                g2d.setColor(branco);
                g2d.drawString("Aperte A para Ajuda", 420, 530);
                g2d.drawString("Aperte ESPAÇO para iniciar um novo jogo", 320, 570);
                
                g2d.drawImage(meninas[0].corboops[indicepeitos], 250, (int)pymenina1, this);
                g2d.drawImage(meninas[1].corboops[indicepeitos], 
                              770, (int)pymenina2, (meninas[1].corboops[indicepeitos]).getWidth(this)+770, (meninas[1].corboops[indicepeitos]).getHeight(this)+(int)pymenina2, //coordenadas da imagem
                              (meninas[1].corboops[indicepeitos]).getWidth(this), 0, 0, (meninas[1].corboops[indicepeitos]).getHeight(this), this); //imagem invertida
                for (int i = 0; i < 4; i++) {
                    g2d.drawImage(inimigos[i].cordoinimigo, (int)inimigos[i].pxinimigo, (int)inimigos[i].pyinimigo, this);
                }
                g2d.drawImage(iescudogrande, 350, 160, this);
                break;
        }

    }
    
    public void IniciaJogo() {
        selec_rosa = true;
        selec_roxo = false;
        for ( Inimigo inimigo : inimigos ) {
            inimigo.ZerarInimigo();
        }       
        meninas[0].ZerarBoops();
        indicepeitos = 0;
        level = new Level();
        carregaescudo = 0;
        escudo = false;   
        pontos = 0;
        vidas = 5;
        energia = 10;
        tela = 1;
        //tocarMusica("Sons/IniciaJogo.mid", 0);
    }
    
    public static void tocarMusica(String urlmusica, int repetir) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(urlmusica).getAbsoluteFile());
            Clip clip = AudioSystem.getClip(); 
            clip.open(audio);
            clip.loop(repetir);
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static int randInt(int min, int max) {
        java.util.Random rand = new java.util.Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
}