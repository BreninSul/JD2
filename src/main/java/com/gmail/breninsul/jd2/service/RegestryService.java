package com.gmail.breninsul.jd2.service;


import com.gmail.breninsul.jd2.dao.registry.*;
import com.gmail.breninsul.jd2.pojo.Certificate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public interface RegestryService {


    /**
     * This method returns search result from third-party registrees
     *
     * @param searchValue   Name of the product we want to search in registrees
     * @param resultSparrow Number of results
     * @param timeOut       How long we can wait until result
     * @return null if there server not avalable
     * @throws ExecutionException
     * @throws InterruptedException
     */


    InfoEntity get(String searchValue, int resultSparrow, int timeOut)  ;

}
