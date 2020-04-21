package sample;

public class Song {
    private String title;
    private int year;
    private int popular;
    private int style_id;
    private int singer_id;
    private String path;

    public Song(String title, int year, int popular, int style_id, int singer_id, String path){
        this.title = title;
        this.year = year;
        this.popular = popular;
        this.style_id = style_id;
        this.singer_id = singer_id;
        this.path = path;
    }

    public int getStyle_id() {
        return style_id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getPopular() {
        return popular;
    }

    public int getSinger_id() {
        return singer_id;
    }

    public String getPath() {
        return path;
    }

    public void setStyle_id(int style_id) {
        this.style_id = style_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSinger_id(int singer_id) {
        this.singer_id = singer_id;
    }
}
