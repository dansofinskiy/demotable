package com.example.demotable;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class PlayerEditor extends VerticalLayout {
    private final PlayerRepository repository;

    /**
     * The currently edited player
     */
    private Player player;

    private TextField name = new TextField("Name");
    private TextField points = new TextField("Points");
    private TextField description = new TextField("Description");

    private Button save = new Button("Save", VaadinIcons.CHECK);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcons.TRASH);
    private CssLayout actions = new CssLayout(save, cancel, delete);

    private Binder<Player> binder = new Binder<>(Player.class);

    @Autowired
    public PlayerEditor(PlayerRepository repository) {
        this.repository = repository;

        addComponents(name, points, description, actions);

        binder.forField(points)
                .withConverter(new StringToIntegerConverter("Enter a number"))
                .bind(Player::getPoints, Player::setPoints);


        binder.bindInstanceFields(this);


        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);


        save.addClickListener(e -> repository.save(player));
        delete.addClickListener(e -> repository.delete(player));
        cancel.addClickListener(e -> editPlayer(player));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editPlayer(Player p) {
        if (p == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = p.getId() != null;
        if (persisted) {
            player = repository.findById(p.getId()).get();
        }
        else {
            player = p;
        }
        cancel.setVisible(persisted);


        binder.setBean(player);

        setVisible(true);


        save.focus();
        name.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
