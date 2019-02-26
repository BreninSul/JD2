package com.gmail.breninsul.jd2.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Product extends BaseEntity {
    private String productName = "";
    private String manufacturer = "";
    private int status = -1;
    private int type = -1;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Certificate> certificates = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Image image;
    @ManyToOne
    @PrimaryKeyJoinColumn
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User user;
    @Formula("(select max(r.F_requirements) from T_Requirements r where r.F_type = F_type)")
    private Integer requirements;

    public static void addCertificate(Product product,Certificate certificate) {
        certificate.setProduct(product);
        product.certificates.add(certificate);
    }

    public static void removeCertificate(Certificate certificate, Product product) {
        product.certificates.remove(certificate);
    }



    @Override
    public String toString() {
        return "Product{" + super.toString() +
                "product_name='" + productName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
