package net.zxw.qypt_wuziqi;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zxw
 * @Email 18316275391@163.com
 */

public class WuziqiPanel extends View{
    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0x44ff0000);
    }

    /**
     * 测量
     * @param widthMeasureSpec 宽度
     * @param heightMeasureSpec 高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //得到宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //得到高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    }
}
