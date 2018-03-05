package rssreadtest.com.rssreadtest.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
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
    private final int weatherData = 0;
    private final int dailyData = 1;
    private RssFeed rssFeed = new RssFeed();
    private RssItem item = null;
    private boolean bGetFlag;
    private String sDaily;
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

                // 讀取天氣資料
                URL url = null;
                try {
                    url = new URL("https://www.cwb.gov.tw/rss/forecast/36_08.xml");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                bGetFlag = true;
                getRssData(url);
                sendData(weatherData);

                // 讀取每日一句
                try {
                    URL urlDaily = new URL("https://tw.appledaily.com/index/dailyquote");
                    getDailyData(urlDaily);
                    sendData(dailyData);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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

    private void getDailyData(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String str = "";
            StringBuffer buffer = new StringBuffer();
            while((str = reader.readLine()) != null) {
                buffer.append(str);
            }
            Document doc = Jsoup.parse(buffer.toString());
            Elements element = doc.select("div[class=dphs]");
            if (element.size() > 0) {
                sDaily = element.get(0).text();
            } else {
                sDaily = "每日一句取得失敗";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData(final int sendType) {
        switch (sendType) {
            case weatherData:
                Log.e("送天氣",sendType + "");
                Intent intentWeather = new Intent();
                intentWeather.putExtra(MainActivity.BROADCAST_ACTION_WEATHER_DATA, rssFeed.getItem(0).getDescription());
                intentWeather.setAction(MainActivity.BROADCAST_ACTION_TAG);
                sendBroadcast(intentWeather);
                break;
            case dailyData:
                Log.e("送每日一句",sendType + "");
                Intent intentDaily = new Intent();
                intentDaily.putExtra(MainActivity.BROADCAST_ACTION_DAILY_DATA, sDaily);
                intentDaily.setAction(MainActivity.BROADCAST_ACTION_DAILY_TAG);
                sendBroadcast(intentDaily);
                break;
            default:
                break;
        }
    }
}
