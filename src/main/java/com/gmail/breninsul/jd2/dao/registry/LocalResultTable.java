package com.gmail.breninsul.jd2.dao.registry;


import com.gmail.breninsul.jd2.pojo.Certificate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalResultTable {
    /**
     * Makes result table by Lists with info.
     *
     */
    List<Certificate> resultTable = new ArrayList();


    protected List<Certificate> get() {

        if (resultTable==null){
            return new ArrayList();
        }
        return resultTable;
    }
    protected   void add(int certType, String number, Date beginDate, Date endDate, String applicant, String manufacturer, String productName) {

        {
            Certificate entity=new Certificate();
            entity.setCertType(certType);
            entity.setNumber(number);
            entity.setBeginDate(beginDate);
            entity.setEndDate(endDate);
            entity.setApplicant(applicant);
            entity.setManufacturer(manufacturer);
            entity.setProductName(productName);
            resultTable.add(entity);
        }

    }
    protected void clear(){
        resultTable.clear();
    }
}
