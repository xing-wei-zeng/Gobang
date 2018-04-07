package net.zxw.qypt_wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    private int MAX_COUNT_IN_LINE = 5;

    //画笔
    private Paint mPaint = new Paint();

    //引入两个棋子
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    //棋子大小比例
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;

    //用户点击的坐标
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();
    //当前轮到白子
    private boolean mIsWhite = true;

    //判断游戏结束
    private boolean mIsGameOver;
    //确定谁是赢家
    private boolean mIsWhiteWinner;

    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(0x44ff0000);
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

        if(mIsGameOver) return false;
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
        drawPieces(canvas);

        //判断游戏是否结束
        checkGameOver();
    }

    /**
     * 判断游戏是否结束
     */
    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);

        if(whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;

            String text = mIsWhiteWinner?"白棋胜利":"黑棋胜利";

            Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查是否有五子连珠
     * @param points
     * @return
     */
    private boolean checkFiveInLine(List<Point> points) {
        for(Point p : points){
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x , y , points);
            if(win) return true;

            win = checkVertical(x , y , points);
            if(win) return true;

            win = checkLeftDiagonal(x , y , points);
            if(win) return true;

            win = checkRightDiagonal(x , y , points);
            if(win) return true;
        }
        return false;
    }

    /**
     * 检查横向是否有五子连珠
     * @param x 需要判断棋子的横坐标
     * @param y 需要判断棋子的纵坐标
     * @param points 整个棋子集合
     * @return true 有五子连珠
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //判断左边
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x - i, y))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //判断右边
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x + i, y))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    /**
     * 检查纵向是否有五子连珠
     * @param x 需要判断棋子的横坐标
     * @param y 需要判断棋子的纵坐标
     * @param points 整个棋子集合
     * @return true 有五子连珠
     */
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //判断上
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x, y - i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //判断右边
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x, y + i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    /**
     * 检查左斜是否有五子连珠
     * @param x 需要判断棋子的横坐标
     * @param y 需要判断棋子的纵坐标
     * @param points 整个棋子集合
     * @return true 有五子连珠
     */
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //判断左下
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x - i, y + i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //判断左上
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x + i, y - i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    /**
     * 检查右斜是否有五子连珠
     * @param x 需要判断棋子的横坐标
     * @param y 需要判断棋子的纵坐标
     * @param points 整个棋子集合
     * @return true 有五子连珠
     */
    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //判断右下
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x + i, y + i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;

        //判断右边
        for(int i = 1; i < MAX_COUNT_IN_LINE ;i++){
            if(points.contains(new Point(x - i, y - i))){
                count++;
            }else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE) return true;
        return false;
    }

    /**
     * 绘制棋子
     * @param canvas
     */
    private void drawPieces(Canvas canvas) {
        for(int i = 0,n = mWhiteArray.size(); i < n ; i++){
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,(whitePoint.x + (1 - radioPieceOfLineHeight) / 2)*mLineHeight,
                    (whitePoint.y + (1 - radioPieceOfLineHeight) / 2)*mLineHeight,null);
        }

        for(int i = 0,n = mBlackArray.size(); i < n ; i++){
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,(blackPoint.x + (1 - radioPieceOfLineHeight) / 2)*mLineHeight,
                    (blackPoint.y + (1 - radioPieceOfLineHeight) / 2)*mLineHeight,null);
        }
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

    /**
     * 在来一局
     */
    public void start(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWinner = false;
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        //默认系统存的
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        //自己存的
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
