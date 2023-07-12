import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Paddle {
    Pair position;
    Pair velocity;
    Pair acceleration;
    Color color;

    public Paddle(int x, int y) {
        position = new Pair(x, y);
        velocity = new Pair(0, 0);
        acceleration = new Pair(0, 0);

        color = new Color(255, 255, 255);
    }
    public Rectangle getBound(){
        Rectangle ball = new Rectangle((int)position.x, (int)position.y , 10, 80);
        return ball;
    }

    public void update(World w, double time) {
        position = position.add(velocity.times(time));
        velocity = velocity.add(acceleration.times(time));
    }

    public void setPosition(Pair p) {
        position = p;
    }

    public void setVelocity(Pair v) {
        velocity = v;
    }

    public void setAcceleration(Pair a) {
        acceleration = a;
    }

    public Pair getPosition() {
        return position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    public Pair getAcceleration() {
        return acceleration;
    }

    public double flipX() {
        acceleration.flipX();
        return 0.0;
    }

    public double flipY() {
        acceleration.flipY();
        return 0.0;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(color);
        g.fillRect((int) position.x, (int) position.y, 10, 80);
        g.setColor(c);
    }
}
class Pair{
    public double x;
    public double y;

    public Pair(double initX, double initY){
        x = initX;
        y = initY;
    }


    public Pair add(Pair toAdd){
        return new Pair(x + toAdd.x, y + toAdd.y);
    }

    public Pair divide(double denom){
        return new Pair(x / denom, y / denom);
    }

    public Pair times(double val){
        return new Pair(x * val, y * val);
    }

    public void flipX(){
        x = -x;
    }

    public void flipY(){
        y = -y;
    }
}

class Sphere{
    Pair position;
    Pair velocity;
    Pair acceleration;
    double radius;
    double dampening;
    Color color;
    public Sphere() {
        Random rand = new Random();
        position = new Pair(500.0, 300.0);
        velocity = new Pair(250,0);
        acceleration = new Pair(0,0);
        radius = 25;
        dampening = 1.3;
        color = new Color(255,255,255);
    }

    public void update(World w, double time){
        position = position.add(velocity.times(time));
        velocity = velocity.add(acceleration.times(time));
        bounce(w);
    }
    public Rectangle getBound(){
        Rectangle ball = new Rectangle((int)position.x, (int)position.y , (int)radius, (int)radius);
        return ball;
    }
    public void setPosition(Pair p){
        position = p;
    }
    public void setVelocity(Pair v){
        velocity = v;
    }
    public void setAcceleration(Pair a){
        acceleration = a;
    }
    public Pair getPosition(){
        return position;
    }
    public Pair getVelocity(){
        return velocity;
    }
    public Pair getAcceleration(){
        return acceleration;
    }
    public void draw(Graphics g){
        Color c = g.getColor();
        g.setColor(color);
        g.fillOval((int)(position.x - radius), (int)(position.y - radius), (int)(2*radius), (int)(2*radius));
        g.setColor(c);
        g.setColor(Color.WHITE);
    }

    private void bounce(World w){
        Boolean bounced = false;
        if (position.x - radius < 0){
            velocity.flipX();
            position.x = radius;
            bounced = true;
        }
        else if (position.x + radius > w.width){
            velocity.flipX();
            position.x = w.width - radius;
            bounced = true;
        }
        if (position.y - radius < 0){
            velocity.flipY();
            position.y = radius;
            bounced = true;
        }
        else if(position.y + radius >  w.height){
            velocity.flipY();
            position.y = w.height - radius;
            bounced = true;
        }
        if (bounced){
//            velocity = velocity.divide(dampening);
        }
        getBound();
    }

}


class World{
    int p1Score;
    int p2Score;

    int height;
    int width;

    int numSpheres;
    Sphere sphere;

    Paddle lPaddle = new Paddle(120,300);
    Paddle rPaddle = new Paddle (900,300);

    public World(int initWidth, int initHeight, int initNumSpheres){
        width = initWidth;
        height = initHeight;
        numSpheres = 1;
        sphere  = new Sphere();

    }

    public void drawSpheres(Graphics g){
            sphere.draw(g);
        lPaddle.draw(g);
        rPaddle.draw(g);
        g.setColor(Color.WHITE);
        g.drawString(""+ p1Score, 15, 20);
        g.drawString(""+ p2Score, 900, 20);

    }

    public void updateSpheres(double time){
        for (int i = 0; i < numSpheres; i ++)
            sphere.update(this, time);

    }

}

public class Pong2 extends JPanel implements KeyListener{
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 60;
    World world;
    class Runner implements Runnable{
        public void collision(){
            Random rand = new Random();
            if(world.sphere.getBound().intersects(world.lPaddle.getBound())) {
                world.sphere.velocity.flipX();
                world.sphere.velocity.y += world.lPaddle.velocity.y;
                world.sphere.color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
            }
            if(world.sphere.getBound().intersects(world.rPaddle.getBound())) {
                world.sphere.velocity.flipX();
                world.sphere.velocity.y += world.rPaddle.velocity.y;
                world.sphere.color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
            }
        }
        public void move() {
            collision();
            if(world.sphere.position.x <= 30){
                world.p2Score+= 1;
                world.sphere.position.x = 500;
                world.sphere.position.y =300;
                world.sphere.velocity.x = -250;
                world.sphere.velocity.y = 0;
            }
            if(world.sphere.position.x >= WIDTH-30){
                world.p1Score+= 1;
                world.sphere.position.x = 500;
                world.sphere.position.y =300;
                world.sphere.velocity.x = 250;
                world.sphere.velocity.y = 0;
            }

        }
        public void run() {

            while(true){

                world.updateSpheres(1.0 / (double)FPS);
                world.lPaddle.update(world, 1.0/(double)FPS);
                world.rPaddle.update(world, 1.0/(double)FPS);
                repaint();
                if(world.lPaddle.position.y <10) world.lPaddle.position.y =10;
                if(world.lPaddle.position.y >HEIGHT-220) world.lPaddle.position.y =HEIGHT-220;
                if(world.rPaddle.position.y <10) world.rPaddle.position.y =10;
                if(world.rPaddle.position.y >HEIGHT-220) world.rPaddle.position.y =HEIGHT-220;
                try{
                    Thread.sleep(1000/FPS);
                    move();
                }
                catch(InterruptedException e){}
            }

        }

    }
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("You pressed down: " + c);
        if(c=='r'){
            world.lPaddle.velocity.y = -250;
                }
        if(c=='f'){
            world.lPaddle.velocity.y = 0 ;
        }
        if(c == 'v'){
            world.lPaddle.velocity.y = 250;
        }
        if(c=='u'){
            world.rPaddle.velocity.y = -250;
        }
        if(c=='n'){
            world.rPaddle.velocity.y = 250;
        }
        if (c == 'j'){
            world.rPaddle.velocity.y = 0;
        }

    }

    public void keyReleased(KeyEvent e) {
        char c=e.getKeyChar();
    }


    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public Pong2(){
        world = new World(WIDTH, HEIGHT, 50);
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        Thread mainThread = new Thread(new Runner());
        mainThread.start();
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Pong!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Pong2 mainInstance = new Pong2();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);


        world.drawSpheres(g);

    }


}