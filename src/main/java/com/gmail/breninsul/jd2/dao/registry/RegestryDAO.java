package com.gmail.breninsul.jd2.dao.registry;

import com.gmail.breninsul.jd2.pojo.Certificate;

import java.util.List;

public interface RegestryDAO {
    List<Certificate> getDeclarations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException;

    List<Certificate> getCertificates(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException;
}
