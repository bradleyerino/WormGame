import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int dirtEaten;
    int dirtX;
    int dirtY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(129,74,60));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newDirt();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        if(running) {
            //for grid visuals
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
                g.setColor(new Color(87,39,12));
                g.fillOval(dirtX, dirtY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(251, 195, 167));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(251, 217, 177));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(new Color(57,57,57));
            g.setFont(new Font("Broadway", Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Worm", (SCREEN_WIDTH-metrics.stringWidth("Worm"))/2, (SCREEN_HEIGHT)/12);

            g.setColor(new Color(57,57,57));
            g.setFont(new Font("Broadway", Font.BOLD, 22));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: "+dirtEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score: "+dirtEaten))/12, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void newDirt(){
        dirtX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        dirtY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U':
                y[0] = y[0]-UNIT_SIZE;
                break;
            case'D':
                y[0] = y[0]+UNIT_SIZE;
                break;
            case'L':
                x[0] = x[0]-UNIT_SIZE;
                break;
            case'R':
                x[0] = x[0]+UNIT_SIZE;
                break;

        }
    }
    public void checkDirt(){
        if((x[0] == dirtX) && (y[0]==dirtY)){
            bodyParts++;
            dirtEaten++;
            newDirt();
        }
    }

    public void checkCollisions(){
        //checks if head collides with body
        for(int i = bodyParts; i>0;i--){
            if((x[0] == x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
        //checks if head collides left border
        if(x[0] < 0){
            running=false;
        }
        //checks if head collides right border
        if(x[0] > SCREEN_WIDTH-UNIT_SIZE){
            running=false;
        }
        //checks if head collides top border
        if(y[0] <0){
            running=false;
        }
        //checks if head collides bottom border
        if(y[0] >SCREEN_HEIGHT-UNIT_SIZE){
            running=false;
        }
        if(!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g) {
        g.setColor(new Color(57, 57, 57));
        g.setFont(new Font("Algerian", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        //Score
        g.setColor(new Color(57, 57, 57));
        g.setFont(new Font("Broadway", Font.BOLD, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + dirtEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + dirtEaten)) / 2, g.getFont().getSize());
        //Play Again?
//        g.setColor(new Color(57, 57, 57));
//        g.setFont(new Font("Algerian", Font.BOLD, 55));
//        FontMetrics metrics2 = getFontMetrics(g.getFont());
//        g.drawString("Play Again?[Y or N]", (SCREEN_WIDTH - metrics2.stringWidth("Play Again?[Y or N]")) / 2, SCREEN_HEIGHT / 6);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkDirt();
            checkCollisions();
        }
        repaint();


    }


    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){

                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }



}
