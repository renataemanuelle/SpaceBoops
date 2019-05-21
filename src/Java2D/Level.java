
package Java2D;

public class Level {
    
    int level;
    int tempotiro;
    int tempoinimigo;
    
    public Level() {
        tempotiro = 8000;
        tempoinimigo = 7000;
        level = 1;
    }
    
    public void AumentaLevel(int n) {
        level = n;
    }
    
    public int GetLevel() {
        return level;
    }
    
    public void SetLevel() {
        tempotiro = (8000/level);
        tempoinimigo = (7000/level);
    }
    
    public int GetTempodeTiro() {
        return tempotiro;
    }
     
    public int GetTempodeInimigo() {
        return tempoinimigo;
    }
    
}
