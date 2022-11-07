package com.accesodatos;
import java.io.Serializable;

public class Address  implements  Serializable{
    //148 size of  each Address Object
    private  int id;
    private String name;
    private String phone;
    private String email;
    private int age;


    public Address (int id, String name, String phone,String email,  int age)throws Exception  {

        if (name.length() < 0 && name.length() > 25 ){
            throw new Exception("El nombre debe tener mínimo 1 caracter y máximo 25 caracteres");
        }

        if (phone.length() < 0 && phone.length() > 15){
                throw new Exception("El teléphono debe tener mínimo 1 caracter y máximo 15 caracteres");
        }

        if (email.length() < 0 && email.length() > 30){
            throw new Exception("El email debe tener mínimo 1 caracter y máximo 15 caracteres");
        }
        this.id = id;
        this.age = age;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
