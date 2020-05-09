package org.inmediart.gui.vaadin.view.order;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.inmediart.gui.client.OrderResourceClient;
import org.inmediart.gui.client.ProductResourceClient;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.vaadin.listener.VaadinMQListener;
import org.inmediart.gui.vaadin.view.ButtonLabelConfig;
import org.inmediart.gui.vaadin.view.product.OrderLabelConfig;
import org.inmediart.gui.vaadin.view.product.ProductLabelConfig;
import org.inmediart.model.entity.type.ActionType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Push
@Route
@PageTitle("InMediArt - Orders")
public class OrdersView extends VerticalLayout implements KeyNotifier {
    private final ProductResourceClient productResourceClient;
    private final OrderResourceClient orderResourceClient;
    private final ProductLabelConfig productLabelConfig;
    private final OrderLabelConfig orderLabelConfig;
    private final ButtonLabelConfig buttonLabelConfig;
    private final Checkbox paid;
    private final VaadinMQListener mqListener;

    final Grid<OrderDTO> grid;
    private final Button usersBtn, productBtn, logoutBtn;
    private final Button openDocument, save, delete;

    public OrdersView(ProductResourceClient productResourceClient, OrderResourceClient orderResourceClient, ProductLabelConfig productLabelConfig, OrderLabelConfig orderLabelConfig, ButtonLabelConfig buttonLabelConfig, VaadinMQListener mqListener) {
        this.productResourceClient = productResourceClient;
        this.orderResourceClient = orderResourceClient;
        this.productLabelConfig = productLabelConfig;
        this.orderLabelConfig = orderLabelConfig;
        this.buttonLabelConfig = buttonLabelConfig;
        this.mqListener = mqListener;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("static/logo.png");
        StreamResource resource = new StreamResource("logo.png", () -> inputStream);
        Image logo = new Image(resource, "InMediArt Logo");
        logo.setMaxWidth("370px");
        add(logo);

        this.grid = new Grid<>(OrderDTO.class);

        this.usersBtn = new Button(buttonLabelConfig.getUserManagement(), VaadinIcon.USERS.create());
        usersBtn.addClickListener(e ->
                usersBtn.getUI().ifPresent(ui ->
                        ui.navigate("users"))
        );

        this.productBtn = new Button(buttonLabelConfig.getProductManagement(), VaadinIcon.BOOK_DOLLAR.create());
        productBtn.addClickListener(e ->
                productBtn.getUI().ifPresent(ui ->
                        ui.navigate("products"))
        );

        this.logoutBtn = new Button("Logout", VaadinIcon.EXIT.create());
        this.logoutBtn.addClickListener(e -> {
            SecurityContextHolder.clearContext();
            UI.getCurrent().getPage().setLocation("logout");
        });

        /* Item Action buttons */
        openDocument = new Button(buttonLabelConfig.getOpenDocument(), VaadinIcon.BULLSEYE.create());
        save = new Button(buttonLabelConfig.getSave(), VaadinIcon.CHECK.create());
        delete = new Button(buttonLabelConfig.getDelete(), VaadinIcon.TRASH.create());
        openDocument.getElement().getThemeList().add("success");
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        paid = new Checkbox(orderLabelConfig.getPaid());

        openDocument.setVisible(false);
        paid.setVisible(false);
        save.setVisible(false);
        delete.setVisible(false);

        this.openDocument.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()) {
                OrderDTO orderDTOSelected = grid.getSelectedItems().iterator().next();
                this.openDocument.getUI().ifPresent(ui ->
                        ui.getPage().open(orderDTOSelected.getProduct().getUrl())
                );
            }
        });

        this.save.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()) {
                OrderDTO orderDTOSelected = grid.getSelectedItems().iterator().next();
                orderDTOSelected.setPaid(paid.getValue());
                orderResourceClient.updateOrder(orderDTOSelected.getOrderId(), orderDTOSelected);
                refreshOrdersGrid();
            }
        });

        this.delete.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()) {
                OrderDTO orderDTOSelected = grid.getSelectedItems().iterator().next();
                orderResourceClient.deleteOrder(orderDTOSelected.getOrderId());
                grid.deselectAll();
                refreshOrdersGrid();
            }
        });

        // build layout
        HorizontalLayout itemActions = new HorizontalLayout(save,delete,openDocument);
        VerticalLayout orderActions = new VerticalLayout(paid,itemActions);


        // build layout
        HorizontalLayout actions = new HorizontalLayout(usersBtn, productBtn, logoutBtn);
        add(actions, grid, orderActions);

        refreshOrdersGrid(orderResourceClient);
        grid.setHeight("300px");

        grid.setColumns("product.name", "product.url", "product.password", "product.delivery", "user", "actionType", "paid", "quantity", "address", "paymentExternalDateTime", "amount");
        grid.getColumnByKey("product.name").setHeader(productLabelConfig.getName());
        grid.getColumnByKey("product.url").setHeader(productLabelConfig.getUrl());
        grid.getColumnByKey("product.password").setHeader(productLabelConfig.getPassword());
        grid.getColumnByKey("product.delivery").setHeader(productLabelConfig.getDelivery());
        grid.getColumnByKey("user").setHeader(orderLabelConfig.getUser());
        grid.getColumnByKey("actionType").setHeader(orderLabelConfig.getActionType());
        grid.getColumnByKey("paid").setHeader(orderLabelConfig.getPaid());
        grid.getColumnByKey("quantity").setHeader(orderLabelConfig.getQuantity());
        grid.getColumnByKey("amount").setHeader(orderLabelConfig.getAmount());
        grid.getColumnByKey("address").setHeader(orderLabelConfig.getAddress());
        grid.getColumnByKey("paymentExternalDateTime").setHeader(orderLabelConfig.getPaymentExternalDateTime());

        // Connect selected Product to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            openDocument.setVisible(false);
            paid.setVisible(false);
            save.setVisible(false);
            delete.setVisible(false);
            if (e.getValue()!=null) {
                openDocument.setVisible(true);
                paid.setVisible(true);
                paid.setValue(e.getValue().getPaid());
                save.setVisible(true);
                delete.setVisible(true);
            }
        });
    }

    private void refreshOrdersGrid(OrderResourceClient orderResourceClient) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (ActionType actionType: ActionType.values()) {
            orderDTOS.addAll(orderResourceClient.findOrdersByActionType(actionType));
        }

        grid.setItems(orderDTOS);
    }

    public void refreshOrdersGrid(){
        OrderDTO orderDTOSelected = null;
        if(!grid.getSelectedItems().isEmpty()) {
            orderDTOSelected = grid.getSelectedItems().iterator().next();
        }
        refreshOrdersGrid(orderResourceClient);

        if(orderDTOSelected != null){
            grid.select(orderDTOSelected);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        this.mqListener.setUIAndOrdersViewToUpdate(attachEvent.getUI(), this);
    }
}
