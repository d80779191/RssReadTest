package rssreadtest.com.rssreadtest.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RssFeed {
    private String title = null;
    private String pubdate = null;
    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    private int itemCount;
    private List<RssItem> itemList;


    public RssFeed() {
        itemList = new ArrayList<RssItem>();
    }

    public int addItem(RssItem item) {
        itemList.add(item);
        itemCount ++;
        return itemCount;
    }

    public RssItem getItem(int location) {
        return itemList.get(location);
    }

    public List<RssItem> getAllItems() {
        return itemList;
    }

    public List<Map<String,Object>> getAllItemsForListView() {
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        int size = itemList.size();
        for(int i=0 ;i<size; i++) {
            HashMap<String,Object> item = new HashMap<String,Object>();
            item.put(RssItem.TITLE, itemList.get(i).getTitle());
            data.add(item);
        }
        return data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemCount() {
        return itemCount;
    }
}
