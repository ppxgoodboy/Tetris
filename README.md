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
- 
