package com.gmail.breninsul.jd2.service;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}