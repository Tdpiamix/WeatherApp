package com.example.weatherapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by Lian on 2017/2/11.
 */

public class testScrollView extends AppCompatActivity {

    private PullToRefreshScrollView pullToRefreshScrollView;
    private ScrollView scrollView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        textView = (TextView) findViewById(R.id.test_scroll_text);
        textView.setText("5、常用的一些属性\n" +
                "\n" +
                "当然了，pull-to-refresh在xml中还能定义一些属性：\n" +
                "\n" +
                "ptrMode，ptrDrawable，ptrAnimationStyle这三个上面已经介绍过。\n" +
                "\n" +
                "ptrRefreshableViewBackground 设置整个mPullRefreshListView的背景色\n" +
                "\n" +
                "ptrHeaderBackground 设置下拉Header或者上拉Footer的背景色\n" +
                "\n" +
                "ptrHeaderTextColor 用于设置Header与Footer中文本的颜色\n" +
                "\n" +
                "ptrHeaderSubTextColor 用于设置Header与Footer中上次刷新时间的颜色\n" +
                "\n" +
                "ptrShowIndicator如果为true会在mPullRefreshListView中出现icon，右上角和右下角，挺有意思的。\n" +
                "\n" +
                "ptrHeaderTextAppearance ， ptrSubHeaderTextAppearance分别设置拉Header或者上拉Footer中字体的类型颜色等等。\n" +
                "\n" +
                "ptrRotateDrawableWhilePulling当动画设置为rotate时，下拉是是否旋转。\n" +
                "\n" +
                "\n" +
                "ptrScrollingWhileRefreshingEnabled刷新的时候，是否允许ListView或GridView滚动。觉得为true比较好。\n" +
                "\n" +
                "ptrListViewExtrasEnabled 决定了Header，Footer以何种方式加入mPullRefreshListView，true为headView方式加入，就是滚动时刷新头部会一起滚动。\n" +
                "\n" +
                "最后2个其实对于用户体验还是挺重要的，如果设置的时候考虑下~。 其他的属性自己选择就好。\n" +
                "\n" +
                "注：上述属性很多都可以代码控制，如果有需要可以直接mPullRefreshListView.set属性名 查看\n" +
                "\n" +
                "以上为pull-to-refresh所有支持的属性~~\n" +
                "\n" +
                "定义了一堆的效果：");
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        scrollView = pullToRefreshScrollView.getRefreshableView();
    }

}
