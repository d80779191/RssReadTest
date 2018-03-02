package rssreadtest.com.rssreadtest.ItemView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rssreadtest.com.rssreadtest.Module.WH;

public class DataListItem extends RelativeLayout {
    private Context context;
    private WH wh;
    private TextView tvDate;
    private TextView tvDayOrNight;
    private TextView tvTemp;
    private TextView tvWeather;
    private Button btnDelete;

    public DataListItem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        wh = new WH(context);
        setView();
    }

    private void setView() {
        {
            tvDate = new TextView(context);
            tvDate.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(wh.getW(45), LayoutParams.WRAP_CONTENT);
            tvDate.setLayoutParams(layoutParams);
            tvDate.setGravity(Gravity.CENTER);
            this.addView(tvDate);
        }
        {
            tvDayOrNight = new TextView(context);
            tvDayOrNight.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(wh.getW(45), LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RIGHT_OF, tvDate.getId());
            tvDayOrNight.setLayoutParams(layoutParams);
            tvDayOrNight.setGravity(Gravity.CENTER);
            this.addView(tvDayOrNight);
        }
        {
            tvTemp = new TextView(context);
            tvTemp.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(wh.getW(45), LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(BELOW, tvDate.getId());
            tvTemp.setLayoutParams(layoutParams);
            tvTemp.setGravity(Gravity.CENTER);
            this.addView(tvTemp);
        }
        {
            tvWeather = new TextView(context);
            tvWeather.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(wh.getW(45), LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(BELOW, tvDate.getId());
            layoutParams.addRule(RIGHT_OF, tvTemp.getId());
            tvWeather.setLayoutParams(layoutParams);
            tvWeather.setGravity(Gravity.CENTER);
            this.addView(tvWeather);
        }
        {
            btnDelete = new Button(context);
            btnDelete.setId(View.generateViewId());
            LayoutParams layoutParams = new LayoutParams(wh.getW(10), wh.getH(10));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(CENTER_VERTICAL);
            btnDelete.setLayoutParams(layoutParams);
            btnDelete.setText("刪除");
            this.addView(btnDelete);
        }
    }

    public TextView getTvDate() {return this.tvDate;}
    public TextView getTvDayOrNight() {return this.tvDayOrNight;}
    public TextView getTvTemp() {return this.tvTemp;}
    public TextView getTvWeather() {return this.tvWeather;}
    public Button getBtnDelete() {return this.btnDelete;}
}
