package com.gmail.breninsul.jd2.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class User extends BaseEntity {
    private String name ;
    private String role ;
    private String pass ;
    private String email ;
    private boolean enabled;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Image image;
    public static void addProduct(User user, Product product) {
        product.setUser(user);
        user.products.add(product);
    }

    public static void removeCertificate(User user, Product product) {
        user.products.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        if (role != user.role) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (pass != null ? !pass.equals(user.pass) : user.pass != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" + super.toString() +
                "name='" + name + '\'' +
                ", role=" + role +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
