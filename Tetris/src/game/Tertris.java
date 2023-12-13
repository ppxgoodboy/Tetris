package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Tertris extends JFrame implements KeyListener {
    private static final int game_x = 26;
    private static final int game_y = 12;
    //文本域数组
    JTextArea[][] text;
    //二维数组
    int[][] data;
    //声明游戏状态标签
    JLabel label1;
    //显示游戏分数标签
    JLabel label;
    //判断游戏是否结束
    boolean isrunning;
    //用于存放所有的方块
    int[] allRect;
    //用于存储当前方块
    int rect;
    //表示方块坐标
    int x, y;
    //定义休眠时间
    int time = 500;
    //计算得分
    int score = 0;
    //判断游戏是否变形
    boolean game_paused = false;
    //记录按下暂停键的次数
    int pause_time = 0;

    public void initWindow() {
        //窗口大小
        this.setSize(600, 850);
        //是否可见
        this.setVisible(true);
        //窗口居中显示
        this.setLocationRelativeTo(null);
        //释放窗口选项
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口不可变
        this.setResizable(false);
        //设置标题
        this.setTitle("俄罗斯方块");
    }

    //初始化游戏
    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x, game_y, 1, 1));
        //初始化面板
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                //设置文本域的行列数
                text[i][j] = new JTextArea(game_x, game_y);
                //设置文本域的背景色
                text[i][j].setBackground(Color.white);
                //设置键盘监听事件
                text[i][j].addKeyListener(this);
                //设置游戏边界
                if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.BLACK);
                    data[i][j] = 1;
                }
                //设置文本区域不可编辑
                text[i][j].setEditable(false);
                //文本区域添加到面板上
                game_main.add(text[i][j]);
            }
        }
        //添加到窗口中
        this.setLayout(new BorderLayout());
        this.add(game_main, BorderLayout.CENTER);
    }

    //初始化游戏说明面板
    public void initExplainPanel() {
        //创建游戏的左说明面板
        JPanel explain_left = new JPanel();
        //创建游戏的右说明面板
        JPanel explain_right = new JPanel();

        explain_left.setLayout(new GridLayout(4, 1));
        explain_right.setLayout(new GridLayout(2, 1));
        //初始化左说明面板
        //在左说明面板添加说明文字
        explain_left.add(new JLabel("空格键为方块变形"));
        explain_left.add(new JLabel("左箭头为方块左移"));
        explain_left.add(new JLabel("右箭头为方块右移"));
        explain_left.add(new JLabel("下箭头为方块下落"));
        //设置标签的内容为红色字体
        label1.setForeground(Color.red);
        //将设置好的标签添加到右说明面板中
        explain_right.add(label1);
        explain_right.add(label);
        //将设置好的左说明面板添加到屏幕左侧
        this.add(explain_left, BorderLayout.WEST);
        //将设置好的右说明面板添加到屏幕右侧
        this.add(explain_right, BorderLayout.EAST);
    }

    public Tertris() {
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //初始化表示游戏状态的标签
        label1 = new JLabel("显示状态:正在游戏中");
        //初始化表示游戏分数的标签
        label = new JLabel("游戏得分为: 0'");
        initGamePanel();
        initExplainPanel();
        initWindow();
        //初始化游戏标志
        isrunning = true;
        //初始化存放方块的数组
        allRect = new int[]{0x00cc, 0x8888, 0x000f, 0x888f, 0xf888, 0xf111, 0x111f, 0x0eee, 0xffff, 0x0008, 0x0888,
                0x000e, 0x0088, 0x000c, 0x08c8, 0x00e4, 0x04c4, 0x004e, 0x08c4, 0x006c, 0x04c8, 0x00c6};
    }

    //开始游戏的方法
    public void game_begin() {
        while (true) {
            //判断游戏是否结束
            if (!isrunning) {
                break;
            }
            //进行游戏
            game_run();
        }
        //改变标签的显示
        label1.setText("游戏状态:游戏结束!");
    }

    //随机生成下落方块形状的方法
    public void ranRect() {
        Random random = new Random();
        rect = allRect[random.nextInt(22)];
    }

    //游戏运行的方法
    public void game_run() {
        ranRect();
        //方块下落位置
        x = 0;
        y = 5;
        for (int i = 0; i < game_x; i++) {
            try {
                Thread.sleep(time);
                if (game_paused) {
                    i--;
                } else {
                    //判断方块是否可以下落
                    if (!canFall(x, y)) {
                        //将data值为1,表示有方块占用
                        changData(x, y);
                        //循环遍历四层看是否可以消除
                        for (int j = x; j < x + 4; j++) {
                            int sum = 0;
                            for (int k = 1; k <= (game_y - 2); k++) {
                                if (data[j][k] == 1) {
                                    sum++;
                                }
                            }
                            //判断是否有一行可以被消除
                            if (sum == (game_y - 2)) {
                                //消除一行
                                removeRow(j);
                            }
                        }
                        //判断游戏是否失败
                        for (int j = 1; j <= (game_y - 2); j++) {
                            if (data[3][j] == 1) {
                                isrunning = false;
                                break;
                            }
                        }
                        break;
                    } else {
                        //层数加一
                        x++;
                        //方块下落一层
                        fall(x, y);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //判断方块是否可以继续下落的方法
    public boolean canFall(int m, int n) {
        //定义一个变量
        int temp = 0x8000;
        //变量4X4的方格
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    //判断该位置的下一行是否有方块
                    if (data[m + 1][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
        //可以下落
        return true;
    }

    //改变不可下降的方块对应的区域信息值
    public void changData(int m, int n) {
        //定义一个变量
        int temp = 0x8000;
        //遍历整个4X4的方块
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    data[m][n] = 1;
                }
                //先列后行的比较
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //移除某一行的所有方块令以上的方块掉落
    public void removeRow(int row) {
        int temp = 100;
        for (int i = row; i >= 1; i--) {
            for (int j = 1; j <= (game_y-1); j++) {
                //进行覆盖
                data[i][j] = data[i - 1][j];
            }
        }
        //刷新游戏区域
        reflesh(row);
        //方块加速
        if (time > temp) {
            time -= temp;
        }
        score += temp;
        //显示变化后的分数
        label.setText("游戏得分:" + score);
    }

    //刷新移除某一行后的游戏界面的方法
    public void reflesh(int row) {
        //遍历row行以上的区域
        for (int i = row; i >= 1; i--) {
            for (int j = 1; j <= (game_y - 2); j++) {
                if (data[i][j] == 1) {
                    text[i][j].setBackground(Color.cyan);
                } else {
                    text[i][j].setBackground(Color.white);
                }
            }
        }
    }

    //方块向下掉落一层的方法
    public void fall(int m, int n) {
        if (m > 0) {
            //清楚上一层方块
            clear(m - 1, n);
        }
        //重新绘制方块
        draw(m, n);
    }

    //清除方块掉落后上一层有颜色的地方
    public void clear(int m, int n) {
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.white);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //重新绘制掉落后方块的方法
    public void draw(int m, int n) {
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.cyan);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //游戏暂停
        if (e.getKeyChar() == KeyEvent.VK_P){
            if (!isrunning){
                return;
            }
            pause_time++;
            //判断是否暂停
            if (pause_time==1){
                game_paused=true;
                label1.setText("游戏状态:暂停中");
            }
            if (pause_time==2){
                game_paused=false;
                pause_time=0;
                label1.setText("游戏状态:正在运行中");
            }
        }
        //控制方块进行变形(空格)
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            if (!isrunning) {
                return;
            }
            if (game_paused){
                return;
            }
            //存储目前方块的索引
            int old;
            for (old = 0; old < allRect.length; old++) {
                if (rect == allRect[old]) {
                    break;
                }
            }
            //存储变形后的方块
            int next;
            //判断是否为方块
            if (old == 0 || old == 7 || old == 8 || old == 9) {
                return;
            }
            //清除当前方块
            clear(x, y);
            if (old == 1 || old == 2) {
                next = allRect[old == 1 ? 2 : 1];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old >= 3 && old <= 6) {
                next = allRect[old + 1 > 6 ? 3 : old + 1];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old == 10 || old == 11) {
                next = allRect[old == 10 ? 11 : 10];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old == 12 || old == 13) {
                next = allRect[old == 12 ? 13 : 12];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old >= 14 && old <= 17) {
                next = allRect[old + 1 > 17 ? 14 : old + 1];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old == 18 || old == 19) {
                next = allRect[old == 18 ? 19 : 18];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            if (old == 20 || old == 21) {
                next = allRect[old == 20 ? 21 : 20];
                if (canTurn(next, x, y)) {
                    rect = next;
                }
            }
            //重绘
            draw(x, y);
        }
    }

    //判断方块是否可以变形
    public boolean canTurn(int a, int m, int n) {
        int temp = 0x8000;
        //遍历整个方块
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
        //可以变形
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //方块进行左移(37==->)
        if (e.getKeyCode() == 37) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }
            if (game_paused){
                return;
            }
            //判断方块是否碰到左墙壁
            if (y <= 1) {
                return;
            }
            int temp = 0x8000;
            //判断是否碰到了别的方块
            for (int i = x; i < x + 4; i++) {
                for (int j = y; j < y + 4; j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j - 1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            //首先清楚目标方块
            clear(x, y);
            y--;
            draw(x, y);
        }

        //方块进行右移
        if (e.getKeyCode() == 39) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }
            if (game_paused){
                return;
            }
            //获取右边的坐标
            int temp = 0x8000;
            int m = x;
            int n = y;
            int mn = 1;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if ((temp & rect) != 0) {
                        if (n > mn) {
                            mn = n;
                        }
                    }
                    n++;
                    temp >>= 1;
                }
                m++;
                n = n - 4;
            }
            //碰到右墙壁
            if (mn >= (game_y - 2)) {
                return;
            }
            //是否碰到别的方块
            temp = 0x8000;
            for (int i = x; i < x + 4; i++) {
                for (int j = y; j < y + 4; j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j + 1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            //清楚当前方块
            clear(x, y);
            y++;
            draw(x, y);
        }
        //方块进行下落
        if (e.getKeyCode() == 40) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }
            if (game_paused){
                return;
            }
            //是否可以下落
            if (!canFall(x, y)) {
                return;
            }
            clear(x, y);
            //改变坐标
            x++;
            draw(x, y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
