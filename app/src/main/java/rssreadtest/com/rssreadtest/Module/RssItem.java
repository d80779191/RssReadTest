package rssreadtest.com.rssreadtest.Module;

public class RssItem {
    public static final String TITLE = "title";
    public static final String PUBDATE = "pubDate";

    private String title = null;
    private String link = null;
    private String pubDate = null;
    private String description = null;
    private String category = null;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
