package rssreadtest.com.rssreadtest.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rssreadtest.com.rssreadtest.MainActivity;
import rssreadtest.com.rssreadtest.Module.RssFeed;
import rssreadtest.com.rssreadtest.Module.RssItem;

public class RssReadService extends Service {
    private RssFeed rssFeed = new RssFeed();
    private RssItem item = null;
    private Handler handler;
    private boolean bGetFlag;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                URL url = null;
                try {
                    url = new URL("https://www.cwb.gov.tw/rss/forecast/36_08.xml");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                bGetFlag = true;
                getRssData(url);
                sendData();

                Looper.loop();
            }
        }).start();
    }

    // 讀取傳入的URL之XML
    private void getRssData(URL url) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String str = "";
            StringBuffer buffer = new StringBuffer();
            while((str = reader.readLine()) != null) {
                buffer.append(str);
            }

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(buffer.toString()));
            parser.nextTag();
            parser.nextTag();
            while (parser.nextTag() != XmlPullParser.END_TAG || !parser.getName().equals("channel")) {
                String name1 = parser.getName();
                if (name1.equals("item")) {
                    item = new RssItem();

                    while (parser.nextTag() != XmlPullParser.END_TAG || !parser.getName().equals("item")) {
                        String name2 = parser.getName();

                        if (name2.equals("description") && bGetFlag) {
                            item.setDescription(parser.nextText());
                            bGetFlag = false;
                        } else {
                            skipUnknownTag(parser);
                        }
                    }
                    rssFeed.addItem(item);
                } else {
                    skipUnknownTag(parser);
                }
            }
        } catch(Exception e) {

        }
    }

    // 略過除了rss之外的tag
    private static void skipUnknownTag(XmlPullParser parser) throws Exception {
        while (parser.next() > 0) {
            if (parser.getEventType() == XmlPullParser.END_TAG)
                break;
        }
    }

    private void sendData() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.BROADCAST_ACTION_DATA, rssFeed.getItem(0).getDescription());
                intent.setAction(MainActivity.BROADCAST_ACTION_TAG);
                sendBroadcast(intent);
                handler.removeCallbacks(this);
            }
        });
    }
}
