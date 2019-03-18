package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.dao.registry.ServerNotAvailableException;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.view.menu.Menu;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Route(value = "log", layout = Menu.class)
public class LogView extends Div {
    private Grid<String> grid = new Grid<>();
    private Grid.Column<String> idColumn = grid.addColumn(String::toString)
            .setHeader("LOG");

    public LogView() {
        Button refrash = new Button();
        refrash.setIcon(VaadinIcon.RECYCLE.create());
        refrash.addClickListener(e -> {
            initGrid();
        });
        StreamResource resource = new StreamResource("log.log",this::initGrid);
        Anchor downloadLink = new Anchor(resource, "Download"); downloadLink.getElement().setAttribute("download", true);
        add(new HorizontalLayout(refrash, downloadLink));
        add(grid);
        grid.getStyle().set("overflow", "scroll");
        idColumn.getElement().getStyle().set("overflow", "scroll");
        idColumn.setWidth("200vw");
//        grid.setHeightByRows(true);
        initGrid();
        idColumn.setResizable(true);

    }

    private InputStream initGrid() {
        List<String> logs = new ArrayList<>();
        File file = new File("/etc/temp/JD1/infoDZ.log");
        InputStream targetStream=null;
        try {
             targetStream = new FileInputStream(file);
            Scanner scanner = new Scanner(targetStream);
            while (true) {
                if (scanner.hasNextLine()) {
                    logs.add(scanner.nextLine());
                } else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            log.info("" + e);
        }
        grid.setItems(logs);
        return targetStream;
    }


}

