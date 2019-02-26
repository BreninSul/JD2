package com.gmail.breninsul.jd2.dao.registry;

import com.gmail.breninsul.jd2.pojo.Certificate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InfoEntity {
    public static final int BELARUS_DECL=0;
    public static final int BELARUS_CERT=1;
    public static final int RF_DECL=2;
    public static final int RF_CERT=3;
    public static final int KZ_DECL=4;
    public static final int KZ_CERT=5;
    public static final int KGZ_DECL=6;
    public static final int KGZ_CERT=7;
    public static final int ARM_DECL=8;
    public static final int ARM_CERT=9;
    List<Certificate> armenianDeclarations = new ArrayList();
    List<Certificate> armenianCertificates = new ArrayList();
    List<Certificate> russianDeclarations = new ArrayList();
    List<Certificate> russianCertificates = new ArrayList();
    List<Certificate> kazakhstanDeclarations = new ArrayList();
    List<Certificate> kazakhstanCertificates = new ArrayList();
    List<Certificate> belarusCertificates = new ArrayList();
    List<Certificate> belarusDeclarations = new ArrayList();
    List<Certificate> kirgizhstanCeritificates = new ArrayList();
    List<Certificate> kirgizhstanDelarations = new ArrayList();

    @Override
    public String toString() {
        return
                "armenianDeclarations=" + armenianDeclarations + "\n" +
                 "armenianCertificates=" + armenianCertificates + "\n" +
                 "russianDeclarations=" + russianDeclarations + "\n" +
                 "russianCertificates=" + russianCertificates + "\n" +
                 "kazakhstanDeclarations=" + kazakhstanDeclarations + "\n" +
                 "kazakhstanCertificates=" + kazakhstanCertificates + "\n" +
                 "belarusCertificates=" + belarusCertificates + "\n" +
                 "belarusDeclarations=" + belarusDeclarations + "\n" +
                 "kirgizhstanCeritificates=" + kirgizhstanCeritificates + "\n" +
                 "kirgizhstanDelarations=" + kirgizhstanDelarations + "\n";
    }

    public InfoEntity(List<Certificate> armenianDeclarations, List<Certificate> armenianCeritificates, List<Certificate> russianDeclarations, List<Certificate> russianCeritificates, List<Certificate> kazakhstanDeclarations, List<Certificate> kazakhstanCeritificates, List<Certificate> belarusCeritificates, List<Certificate> belarusDeclarations, List<Certificate> kirgizhstanCeritificates, List<Certificate> kirgizhstanDelarations) {
        this.armenianDeclarations = armenianDeclarations;
        this.armenianCertificates = armenianCeritificates;
        this.russianDeclarations = russianDeclarations;
        this.russianCertificates = russianCeritificates;
        this.kazakhstanDeclarations = kazakhstanDeclarations;
        this.kazakhstanCertificates = kazakhstanCeritificates;
        this.belarusCertificates = belarusCeritificates;
        this.belarusDeclarations = belarusDeclarations;
        this.kirgizhstanCeritificates = kirgizhstanCeritificates;
        this.kirgizhstanDelarations = kirgizhstanDelarations;
    }

    public int getSize() {
        return 10;
    }

    public List<Certificate> get(int i) {
        switch (i) {
            case 0:
                return belarusDeclarations;
            case 1:
                return belarusCertificates;
            case 2:
                return russianDeclarations;
            case 3:
                return russianCertificates;
            case 4:
                return kazakhstanDeclarations;
            case 5:
                return kazakhstanCertificates;
            case 6:
                return kirgizhstanDelarations;
            case 7:
                return kirgizhstanCeritificates;
            case 8:
                return armenianDeclarations;
            case 9:
                return armenianCertificates;

        }
        return new ArrayList<Certificate>();
    }
}
