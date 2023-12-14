# Tetris
- 一个简单的俄罗斯方块源码  
- 这个程序使用了一个非常新奇的写法(对于我个人而言)  
- 如果是正常思路来实现的话无疑是使用在屏幕上绘制方块,然后在数组中记录屏幕数据,包括移动等都是以最低一个方块大小为单位移动的,检测则是用数组记录屏幕信息,然后用1和0区分是否有方块填充...  
这个代码的新视角就是  
- 使用JTextArea[][] text;关键词将屏幕分成了可编辑的多行多列的可编辑文本,然后将它的属性改为不可编辑,因为我们不需要对此区域进行编辑而是当作一个个方块像素进行处理,这相当于用文本框的背景色作为一个像素在你的电脑上构成了一个新的显示屏幕,这么处理难以想象,不过在此游戏中恰到好处,屏幕划分出多少个区域来,用这种方法可以直接处理到这个麻烦的问题了  
- int[][] data;是一个记录的数组,和最初的思想一样,用数组记录哪里有方块,这里这样处理是因为文本框可以改变颜色在视觉上将此区域填充方块,但在业务逻辑处理方面则无法考颜色区分,使用引入这个概念来实现这一效果,后续也是这一数组中将后台数据进行修改更新的  
``` java
if (j == 0 || j == text[i].length - 1 || i == text.length - 1) {
                    text[i][j].setBackground(Color.BLACK);
                    data[i][j] = 1;
                }
```
- 这条语句可以看出即使是边框也使用了1来赋值,这可能和下落的方块的1混淆导致被清除掉,所以后面的遍历都是从列为1开始的(起始是0这是常识),但是看到后面作者考虑到整个结构是靠二进制进行判断处理的就意识到这是正确的做法了,简直是严丝合缝做的
``` java
allRect = new int[]{0x00cc, 0x8888, 0x000f, 0x888f, 0xf888, 0xf111, 0x111f, 0x0eee, 0xffff, 0x0008, 0x0888,
                0x000e, 0x0088, 0x000c, 0x08c8, 0x00e4, 0x04c4, 0x004e, 0x08c4, 0x006c, 0x04c8, 0x00c6};
```
这边是对所有方块进行赋值和编号,方块采用的是4x4的矩阵代替,不同于二维数组的是这边用的是一维数组,但是存储的是16进制的数字,拿0xf111举例,操作时是将其改为二进制所以可以解读为  
```
1 1 1 1
0 0 0 1
0 0 0 1
0 0 0 1
```
经过颜色的渲染后这就是一个俄罗斯方块  
``` java
for (int i = 0; i < game_x; i++) {
            try {
                Thread.sleep(time);
                if (game_paused) {
                    i--;
                }
```
截取局部代码,这是游戏暂停的设计,游戏暂停并没有将线程停止,而是i--,因为对方块进行渲染是依靠i的值来确定的,只要每一个循环里i的值不变,方块的纵坐标就不会改变,但这样存在一个问题就是纵坐标不能改变但是横坐标可以改变,所以程序对后面横坐标也有相应的逻辑处理  
- 程序中用到了大量的双层for循环,这边挑一个进行分析
``` java
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
```
temp的取值和上面是交相呼应的0x8000意味着只有左上角一个位置存在方块,  
>第一遍循环:先让方块和temp相与,判断当前位置是否存在一个小方块,存在,即为1(1&1==1否则为0)的情况下进行下一行的判断,m+1为纵坐标向下移动,直接用data的值来判断,因为data记录的是屏幕上已经固定的方块,正在下落和已经消掉的小方块对应的值都为0,可以理解为text的记录数据和data的记录数据有一定的延迟,这样判断更加方便,如果为1说明下方位置存在一个方块,因此不能下落return false,然后内层循环中n++(n是data数据的横坐标记录变量),temp右移一位,这没什么好解释的,接着继续判断,内层第一层结束后将纵坐标下移一位,横坐标恢复初值,接着循环如此反复,只有16个格子全部遍历一遍后可以下落才会return true
>
- 最后就是图形的变形,这就用到了上面的编号,巧妙的编号让变形方法写的时候利用三目运算和加一处理即可实现此目的
