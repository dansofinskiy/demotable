package com.example.demotable;

import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.util.StringUtils;

@SpringUI
public class DemotableUI extends UI {
    private final PlayerRepository playerRepository;

    private final PlayerEditor editor;

    final Grid<Player> grid;

    final TextField filter;

    private final Button addNewBtn;

    public DemotableUI(PlayerRepository playerRepository, PlayerEditor editor) {
        this.playerRepository = playerRepository;
        this.editor = editor;
        this.grid = new Grid<>(Player.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New player");
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
        setContent(mainLayout);

        grid.setHeight(300, Unit.PIXELS);
        grid.setColumns("name", "points");
        grid.sort("points", SortDirection.DESCENDING);

        filter.setPlaceholder("Filter by name");


        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listPlayers(e.getValue()));


        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPlayer(e.getValue());
        });


        addNewBtn.addClickListener(e -> editor.editPlayer(new Player("", 0)));


        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPlayers(filter.getValue());
        });


        listPlayers(null);
    }


    private void listPlayers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(playerRepository.findAll());
        }
        else {
            grid.setItems(playerRepository.findByNameStartsWithIgnoreCase(filterText));
        }
    }

}
