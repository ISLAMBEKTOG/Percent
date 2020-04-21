package sample;

public class ModelTable {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String gender;
    private Integer age;
    private Integer country_id;
    private String registered_time;

    public ModelTable(int id, String name, String phone, String email, String password, String gender, Integer age, Integer country_id, String registered_time) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.country_id = country_id;
        this.registered_time = registered_time;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public Integer getCountry_id() {
        return country_id;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getRegistered_time() {
        return registered_time;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setRegistered_time(String registered_time) {
        this.registered_time = registered_time;
    }
}
