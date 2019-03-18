package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("")
@Theme(Lumo.class)
public class MainView extends Div  implements RouterLayout{


    public MainView() {
        UI.getCurrent().navigate(SearchView.class);
    }

}
