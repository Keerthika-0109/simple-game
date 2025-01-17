import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreaker extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int ballX = 250, ballY = 300, ballDX = 2, ballDY = -2;
    private int paddleX = 200, paddleWidth = 100;
    private int score = 0;
    private int totalBricks = 21;
    private Brick[] bricks = new Brick[totalBricks];

    public BrickBreaker() {
        this.setFocusable(true);
        this.addKeyListener(this);
        initializeBricks();
        timer = new Timer(5, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.setColor(Color.black);
        g.fillRect(0, 0, 500, 400);

        // Draw bricks
        for (int i = 0; i < totalBricks; i++) {
            if (bricks[i].isVisible()) {
                g.setColor(bricks[i].getColor());
                g.fillRect(bricks[i].getX(), bricks[i].getY(), 50, 20);
            }
        }

        // Draw ball
        g.setColor(Color.white);
        g.fillOval(ballX, ballY, 20, 20);

        // Draw paddle
        g.setColor(Color.blue);
        g.fillRect(paddleX, 350, paddleWidth, 10);

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Score: " + score, 10, 20);

        // Game Over message
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.red);
            g.drawString("Game Over", 150, 200);
        }

        // Game Won message
        if (gameWon) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.green);
            g.drawString("You Won!", 150, 200);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || gameWon) {
            return;
        }

        // Ball movement
        ballX += ballDX;
        ballY += ballDY;

        // Ball collision with walls
        if (ballX <= 0 || ballX >= 480) {
            ballDX = -ballDX;
        }
        if (ballY <= 0) {
            ballDY = -ballDY;
        }

        // Ball collision with paddle
        if (ballY >= 340 && ballY <= 350 && ballX + 20 >= paddleX && ballX <= paddleX + paddleWidth) {
            ballDY = -ballDY;
        }

        // Ball collision with bricks
        for (int i = 0; i < totalBricks; i++) {
            if (bricks[i].isVisible() && ballX + 20 > bricks[i].getX() && ballX < bricks[i].getX() + 50 &&
                ballY + 20 > bricks[i].getY() && ballY < bricks[i].getY() + 20) {
                ballDY = -ballDY;
                bricks[i].setVisible(false);
                score += 10;
                totalBricks--;
                if (totalBricks == 0) {
                    gameWon = true;
                }
                break;
            }
        }

        // Ball fall off the screen
        if (ballY >= 400) {
            gameOver = true;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Move paddle with left and right arrow keys
        if (keyCode == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 15;
        }
        if (keyCode == KeyEvent.VK_RIGHT && paddleX < 400) {
            paddleX += 15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void initializeBricks() {
        int x = 50, y = 50;
        for (int i = 0; i < totalBricks; i++) {
            bricks[i] = new Brick(x, y);
            x += 60;
            if (x >= 450) {
                x = 50;
                y += 30;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker");
        BrickBreaker game = new BrickBreaker();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(game);
        frame.setVisible(true);
    }

    // Inner class for bricks
    class Brick {
        private int x, y;
        private boolean visible;
        private Color color;

        public Brick(int x, int y) {
            this.x = x;
            this.y = y;
            this.visible = true;
            this.color = new Color((int) (Math.random() * 0x1000000));
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public Color getColor() {
            return color;
        }
    }
}
