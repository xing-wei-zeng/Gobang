package net.zxw.qypt_wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxw
 * @Email 18316275391@163.com
 */

public class WuziqiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    //画笔
    private Paint mPaint = new Paint();

    //引入两个棋子
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    //棋子大小比例
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;

    //用户点击的坐标
    private List<Point> mWhiteArray = new ArrayList<>();
    private List<Point> mBlackArray = new ArrayList<>();
    //当前轮到白子
    private boolean mIsWhite = true;

    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    /**
     * 初始化画笔,棋子
     */
    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  宽度
     * @param heightMeasureSpec 高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //得到高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //用户使用确定值
        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    /**
     * 尺寸变化
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    /**
     * 手势
     * @param event The motion event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){

            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x , y);

            //判断当前点是否下过
            if(mWhiteArray.contains(p) || mBlackArray.contains(p)){
                return false;
            }

            if(mIsWhite){
                mWhiteArray.add(p);
            }else{
                mBlackArray.add(p);
            }
            //重绘
            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight), (int)(y / mLineHeight));
    }

    /**
     * 绘制
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
    }

    /**
     * 绘制棋盘方法
     *
     * @param canvas
     */
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX, y, endX, y, mPaint);

            canvas.drawLine(y , startX , y ,endX ,mPaint);
        }
    }
}
