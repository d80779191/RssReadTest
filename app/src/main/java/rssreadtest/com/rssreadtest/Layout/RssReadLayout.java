package rssreadtest.com.rssreadtest.Layout;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import rssreadtest.com.rssreadtest.Module.MainParentLayout;
import rssreadtest.com.rssreadtest.Module.adapter2;

public class RssReadLayout extends MainParentLayout{
    private Button btnGetRss;
    private ListView lvData;
    private TextView tvDaily;
    public RssReadLayout(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        setMainView();
    }

    private void setMainView() {
        {
            btnGetRss = new Button(context);
            btnGetRss.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(WH.getW(20), WH.getH(10));
            btnGetRss.setLayoutParams(layoutParams);
            btnGetRss.setText("讀取");
            btnGetRss.setGravity(Gravity.CENTER);
            this.addView(btnGetRss);
        }
        {
            lvData = new ListView(context);
            lvData.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, WH.getH(80));
            layoutParams.addRule(BELOW, btnGetRss.getId());
            lvData.setLayoutParams(layoutParams);
            this.addView(lvData);
        }
        {
            tvDaily = new TextView(context);
            tvDaily.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, WH.getH(10));
            layoutParams.addRule(BELOW, lvData.getId());
            tvDaily.setLayoutParams(layoutParams);
            this.addView(tvDaily);
        }
    }

    public Button getBtnGetRss() {
        return this.btnGetRss;
    }
    public ListView getLvData() {return this.lvData;}
    public TextView getTvDaily() {return this.tvDaily;}
}
