package com.gmail.breninsul.jd2.dao.registry;

import lombok.Getter;

@Getter
public class ServerNotAvailableException extends Exception {
    private String fromWhere = "";

    public ServerNotAvailableException(String fromWhere) {
        this.fromWhere = fromWhere;
    }
}
