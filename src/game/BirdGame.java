package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class BirdGame extends JPanel{
    //背景图片
    BufferedImage backgroud;
    //开收图片
    BufferedImage startImage;
    //结束
    BufferedImage gameOverImage;
    //地面
    Ground ground;
    //柱子
    Column column1,column2;
    //小鸟
    Bird bird;

    //分数
    int score;
    //游戏状态
    int state;
    //状态常量
    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int GAME_OVER = 2;
    /*
    * 初始化游戏
    * */
    public BirdGame() throws Exception{
        //初始化背景图片
        backgroud = ImageIO.read(getClass().getResource("/resources/bg.png"));

        //初始化开始结束图片
        startImage = ImageIO.read(getClass().getResource("/resources/start.png"));
        gameOverImage = ImageIO.read(getClass().getResource("/resources/gameover.png"));

        //初始化地面，柱子，小鸟
        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        bird = new Bird();

        //初始化分数
        score = 0;

        state = START;
    }
    /*
    * 绘制界面
    * */
    public void paint(Graphics g) {
        //绘制背景
        g.drawImage(backgroud, 0, 0, null);

        g.drawImage(ground.image, ground.x, ground.y, null);

        g.drawImage(column1.image, column1.x - column1.width / 2, column1.y - column1.height / 2, null);
        g.drawImage(column2.image, column2.x - column2.width / 2, column2.y - column2.height / 2, null);

        //绘制小鸟
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(-bird.alpha, bird.x, bird.y);
        g.drawImage(bird.image,
                bird.x - bird.width / 2, bird.y - bird.height / 2, null);
        g2.rotate(bird.alpha, bird.x, bird.y);

        //绘制分数
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
        g.setFont(f);
        g.drawString("" + score, 40, 60);
        g.setColor(Color.WHITE);
        g.drawString("" + score, 40 - 3, 60 - 3);

        //绘制结束与开始
        switch (state) {
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;

            case GAME_OVER:
                g.drawImage(gameOverImage, 0, 0, null);
                break;
        }


    }
    public void  action() throws InterruptedException {
        MouseListener l = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                try {
                    switch (state) {
                        case START:
                            state = RUNNING;
                            break;
                        case RUNNING:
                            bird.flappy();
                            break;
                        case GAME_OVER:
                            column1 = new Column(1);
                            column2 = new Column(2);
                            bird = new Bird();
                            score = 0;
                            state = START;
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
// 将***添加到当前的面板上
        addMouseListener(l);
        //不断移动与重绘
        while (true){
            switch (state){
                case START:
                    bird.fly();
                    ground.step();
                    break;
                case RUNNING:
                    ground.step();
                    column1.step();
                    column2.step();
                    bird.fly();
                    bird.step();
                    if(bird.x == column1.x || bird.x == column2.x){
                        score++;
                    }
                    //监测是否碰撞
                   /* if(bird.hit(ground) || bird.hit(column1) || bird.hit(column2))
                    {
                        state = GAME_OVER;
                    }
                    break;*/
            }
            repaint();
            Thread.sleep(1000/60);
        }
    }

    /*
    启动方法
    * */
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
            BirdGame game = null;
            try {
                game = new BirdGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.add(game);
        frame.setSize(400,670);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.action();

    }
}
