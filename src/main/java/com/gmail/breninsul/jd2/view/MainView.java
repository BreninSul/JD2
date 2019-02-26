package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("")
@Theme(Lumo.class)
public class MainView extends Div  implements RouterLayout{

    private final ICertificateSpringDataDAO repo;
    //private final UI ui =UI.getCurrent();

    public MainView(ICertificateSpringDataDAO repo) {

        this.repo = repo;
        //Menu menu=new Menu();
       /* UI ui=UI.getCurrent();
        Menu menu=new Menu();
        add(menu);*/
    }



}
