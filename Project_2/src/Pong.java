import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pong extends JPanel implements KeyListener {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 60;
    public static final int RADIUS = 50;
    double positionX;
    double positionY;
    double rec1PosX;
    double rec2PosX;
    double rec1PosY;
    double rec2PosY;
    double rec1v;
    double rec2v;
    double velocityX;
    double velocityY;

    double accelerationX;
    double accelerationY;

    class Runner implements Runnable{
        public Runner()
        {
            //Feel free to change these default values
            positionX = 275;
            positionY = HEIGHT - 475;
            rec1PosX = 20;
            rec1PosY = 300;
            rec2PosY = 300;
            rec2PosX = WIDTH -20;
            velocityX = 400;
            velocityY = -100;

            accelerationY = 98;
            rec1v = 0;
            rec2v = 0;

            //your code here for adding the second sphere

        }
        public void run()
        {
            while(true){

                if(positionX>980){
                    velocityX = -velocityX;
                }
                if(positionX<0){
                    velocityX= 400;
                }

                positionX += velocityX / (double)FPS;
                rec1PosY += rec1v / (double) FPS;

                repaint();
                try{
                    Thread.sleep(1000/FPS);
                }
                catch(InterruptedException e){}
            }
        }
    }

    public Pong(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        Thread mainThread = new Thread(new Runner());
        mainThread.start();
    }
    public static void main(String[] args){
        JFrame frame = new JFrame("Pong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Pong world = new Pong();
        frame.setContentPane(world);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);


        g.setColor(Color.WHITE);
        g.drawOval((int)positionX, (int)positionY,  RADIUS,  RADIUS);
        //your code here for drawing the second sphere
        g.fillRect((int)rec1PosX,(int)rec1PosY, 10, 40);
        g.fillRect((int)rec2PosX,(int)rec2PosY, 10, 40);
    }
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("You pressed down: " + c);
        if(c== 'r'){
            rec1v +=250;
        }
        if(c=='v'){
            rec1v -= 250;
        }
        if(c== 'f'){
            rec1v = 0;
        }
    }
    public void keyReleased(KeyEvent e) {
        char c=e.getKeyChar();
    }


    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
    }
}
