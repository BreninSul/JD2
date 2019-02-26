package com.gmail.breninsul.jd2.pojo;

import com.gmail.breninsul.jd2.managers.ToSimpleDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.cache.annotation.CacheResult;
import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
public class Certificate extends BaseEntity {
    private String number = "";
    private int power = -1;
    private Date beginDate = new Date();
    private Date endDate = new Date();
    private int alive = -1;
    private int status = -1;
    private int certType = -1;
    private String applicant = "";
    private String manufacturer = "";
    private String productName = "";
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        return  (that.getId()==this.getId());
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + power;
        result = 31 * result + alive;
        result = 31 * result + status;
        result = 31 * result + certType;
        result = 31 * result + (applicant != null ? applicant.hashCode() : 0);
        result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Certificate{" + super.toString() +
                "number='" + number + '\'' +
                ", power=" + power +
                ", alive=" + alive +
                ", status=" + status +
                ", certType=" + certType +
                ", applicant='" + applicant + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }

    public String getSimpleBeginDate() {
        return ToSimpleDate.get(beginDate);
    }

    public String getSimpleEndDate() {
        return ToSimpleDate.get(endDate);
    }

    public String getStringCertType() {
        if (certType>1) {
            return "Certificate";
        } else {
            return "Declaration";
        }
    }
}
