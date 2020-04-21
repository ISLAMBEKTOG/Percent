package sample;

public class ModelTableSong {
    private int id;
    private String title;
    private int year;
    private int popular;
    private int style_id;
    private int singer_id;
    private String path;

    public ModelTableSong(int id,String title, int year, int popular, int style_id, int singer_id, String path){
        this.id = id;
        this.title = title;
        this.year = year;
        this.popular = popular;
        this.style_id = style_id;
        this.singer_id = singer_id;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public int getStyle_id() {
        return style_id;
    }

    public int getId() {
        return id;
    }

    public Integer getPopular() {
        return popular;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(Integer singer_id) {
        this.singer_id = singer_id;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setPopular(Integer popular) {
        this.popular = popular;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStyle_id(int style_id) {
        this.style_id = style_id;
    }
}
