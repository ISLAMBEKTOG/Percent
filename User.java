package sample;


public class User {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String gender;
    private Integer age;
    private Integer country_id;

    public User(){}

    public User(String name, String phone, String email, String password, String gender, Integer age, Integer country_id){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.country_id = country_id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
