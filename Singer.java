package sample;

public class Singer {
    public static Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Integer style_id;
    private Integer country_id;
    private String imagePath;

    public Singer(){}

    public Singer(String name, Integer age, String gender, Integer style_id,Integer country_id, String imagePath){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.style_id = style_id;
        this.country_id = country_id;
        this.imagePath = imagePath;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public Integer getCountry_id() {
        return country_id;
    }

    public Integer getStyle_id() {
        return style_id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public void setStyle_id(Integer style_id) {
        this.style_id = style_id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
