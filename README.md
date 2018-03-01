# Syntax-Highlighting-for-Android

基于EditText的Android端代码高亮库。

## 效果图

1. 显示行号，不换行：

   ![device-2018-03-01-113400](.\device-2018-03-01-113400.png)

2. 不显示行号，不换行：

   ![device-2018-03-01-113430](.\device-2018-03-01-113430.png)

3. 显示行号，自动换行：

   ![device-2018-03-01-114701](.\device-2018-03-01-114701.png)



## 使用方法

```xml
<com.skywalker.syntaxhighlighter.HighlightView
        android:id="@+id/HighlightView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:showLineNumber="true"
        app:zoom="true"
        app:wrapping="false"
        app:editable="false"
        app:zoomUpperLimit="10"
        />
```

```java
HighlightView mHighlightView = findViewById(R.id.HighlightView);
mHighlightView.setEditable(false);
mHighlightView.setShowLineNumber(true);
mHighlightView.setWrapping(false);
mHighlightView.setContent(mCode);
mHighlightView.render(new DefaultTheme(this), new JavaMode());
```

**参数说明：**

`setEditable(boolean)`：文本是否可编辑，默认为`false`;

`setShowLineNumber(boolean)`：是否显示行号，默认为`true`;

`setWrapping(boolean)`：代码是否自动换行，默认为`false`;

`setContent(String)`：要高亮显示的代码内容;

`render(Theme, Mode)`：渲染代码片段，`Theme`为显示的背景主题，`Mode`为代码的`language`类型，目前有`CMode`，`JavaMode`，`JavaScriptMode`三种可高亮的语言类型。

## 原理简介

1. 使用正则表达式进行词法分析，提取关键词，使用`ForegroundColorSpan`对不同类型的关键词进行代码着色。

2. 行号显示

   ```java
   @Override
       protected void onDraw(Canvas canvas) {
           int baseline = getBaseline();

           if (mShowLineNumber) {
               //画行号的背景
               canvas.drawRect(getScaleX(), 0, mNumberBarWidth + getScrollX(), getLineHeight() * getLineCount(), mPaint);

               if (!mWrapping) {
                   //绘制行号数
                   int color = getPaint().getColor();
                   getPaint().setColor(Color.WHITE);

                   for (int i = 1; i <= getLineCount(); i++) {
                       canvas.drawText(Integer.toString(i), NUMBER_OFFSET + getScrollX(), baseline, getPaint());
                       baseline += getLineHeight();
                   }

                   //方法1： 使用canvas.save()和canvas.restore()
                   /*canvas.save();
                   canvas.translate(mNumberBarWidth + 5, 0);
                   getPaint().setColor(color);
                   super.onDraw(canvas);
                   canvas.restore();*/

                   //方法2： 使用canvas.translate
                   canvas.translate(mNumberBarWidth + 5, 0);
                   getPaint().setColor(color);
                   super.onDraw(canvas);
                   canvas.translate(-mNumberBarWidth + 5, 0);
               } else {
                   super.onDraw(canvas);
               }


           } else {
               super.onDraw(canvas);
           }


       }
   ```

   先绘制行号部分的矩形背景条，`canvas.drawRect(getScaleX(), 0, mNumberBarWidth + getScrollX(), getLineHeight() * getLineCount(), mPaint);` `mNumberBarWidth` 为背景条宽度，`mNumberBarWidth + getScrollX()`为当单行显示时，左右滑动屏幕，背景条的位置也随之移动，使行号背景条部分能够一直显示在屏幕最左侧。然后将整个`canvas`向右平移，绘制代码文字部分，绘制完毕后，再将屏幕向左平移到原位置。

3. 自动换行：自动换行功能主要通过`LeadingMarginSpan`。先计算代码片段的行数，然后为每一行通过`LeadingMarginSpan`设置缩进量。