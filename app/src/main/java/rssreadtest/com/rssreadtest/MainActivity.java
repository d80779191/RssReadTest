package rssreadtest.com.rssreadtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rssreadtest.com.rssreadtest.ItemView.DataListItem;
import rssreadtest.com.rssreadtest.Layout.RssReadLayout;
import rssreadtest.com.rssreadtest.Module.RssReadDataItem;
import rssreadtest.com.rssreadtest.Module.RssReadSQLModule;
import rssreadtest.com.rssreadtest.Module.RssReadSQLite;
import rssreadtest.com.rssreadtest.Module.adapter2;
import rssreadtest.com.rssreadtest.Service.RssReadService;

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_ACTION_TAG = "BROADCAST_ACTION";
    public static final String BROADCAST_ACTION_DATA = "BROADCAST_ACTION_DATA";
    private RssReadLayout layout;
    private Boolean isServiceOn;
    private RssDataReceiver rssDataReceiver;
    private String[] receiveData;
    private RssReadSQLModule rssReadSQLModule;
    private RssReadDataItem readDataItem;
    private List<RssReadDataItem> readDataItems;
    private ArrayList<View> viewList;
    private DataListItem listItem;
    private int[] buttonId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout = new RssReadLayout(this));
        rssReadSQLModule = new RssReadSQLModule(MainActivity.this);
        isServiceOn = true;
        initReceiver();
        setButtonOnClick();
    }

    // 初始化BroadcastReceiver
    private void initReceiver() {
        rssDataReceiver = new RssDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.BROADCAST_ACTION_TAG);
        this.registerReceiver(rssDataReceiver, filter);
    }

    // 設定按鈕事件
    private void setButtonOnClick() {
        layout.getBtnGetRss().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceOn) {
                    Intent intent = new Intent(MainActivity.this, RssReadService.class);
                    startService(intent);
                    isServiceOn = false;
                }
            }
        });
    }

    // 接收讀取資料
    public class RssDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 先存入資料
            if (intent != null) {
                String string = intent.getStringExtra(BROADCAST_ACTION_DATA);
                receiveData = string.split("<BR>");
                if (rssReadSQLModule.getCount() == 0) {
                    insertDataToDatabase(receiveData);
                } else {
                    rssReadSQLModule.deleteAll();
                    insertDataToDatabase(receiveData);
                }
            } else {

            }
            // 再讀取資料放進ListView
            setListView();
        }
    }

    private void insertDataToDatabase(String[] string) {
        for (int i = 0; i < string.length; i++) {
            // 以日期當id
            String[] data = string[i].split(" ");
            String id = data[0].trim();
            String[] resetId = id.split("/");
            if (i % 2 == 0 && resetId.length > 1) {
                id = resetId[0] + resetId[1] + "0";
                readDataItem = new RssReadDataItem(Long.parseLong(id), id, data[1], data[2] + data[3] + data[4], data[5]);
                rssReadSQLModule.insert(readDataItem);
            } else if (resetId.length > 1) {
                id = resetId[0] + resetId[1] + "1";
                readDataItem = new RssReadDataItem(Long.parseLong(id), id, data[1], data[2] + data[3] + data[4], data[5]);
                rssReadSQLModule.insert(readDataItem);
            }
        }
    }

    private void setListView() {
        readDataItems = rssReadSQLModule.getAll();
        viewList = new ArrayList<>();
        buttonId = new int[readDataItems.size()];
        for (int i = 0; i < readDataItems.size(); i++) {
            listItem = new DataListItem(MainActivity.this);
            listItem.getTvDate().setText(String.valueOf(readDataItems.get(i).getDate().substring(0, 2) + "/" + readDataItems.get(i).getDate().substring(2, 4)));
            listItem.getTvDayOrNight().setText(readDataItems.get(i).getDayOrNight());
            listItem.getTvTemp().setText(readDataItems.get(i).getTemp());
            listItem.getTvWeather().setText(readDataItems.get(i).getWeather());
            buttonId[i] = listItem.getBtnDelete().getId();
            listItem.getBtnDelete().setOnClickListener(dataDeleteClick);
            viewList.add(listItem);
        }
        adapter2 adapter = new adapter2(viewList);
        layout.getLvData().setAdapter(adapter);
    }

    private View.OnClickListener dataDeleteClick = new View.OnClickListener() {
        private int position;
        @Override
        public void onClick(View v) {
            final View view = v;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("警告")
                    .setMessage("是否刪除此筆資料")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < buttonId.length; i++) {
                                if (view.getId() == buttonId[i]) {
                                    position = i;
                                    break;
                                }
                            }
                            readDataItems = rssReadSQLModule.getAll();
                            rssReadSQLModule.delete(readDataItems.get(position).getId());
                            setListView();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    };
}
