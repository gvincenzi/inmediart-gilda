package org.inmediart.gui.vaadin.listener;

import com.vaadin.flow.component.UI;
import org.inmediart.commons.binding.GUIMQBinding;
import org.inmediart.commons.binding.GassmanMessage;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.vaadin.view.order.OrdersView;
import org.inmediart.gui.vaadin.view.product.ProductsView;
import org.inmediart.gui.vaadin.view.user.UsersView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(GUIMQBinding.class)
public class VaadinMQListener {
    UI ui;
    UsersView usersView;
    ProductsView productsView;
    OrdersView ordersView;

    @Value("${gassman.instance.id}")
    private String instanceId;

    @Value("${gassman.instance.botName}")
    private String botName;

    public void setUIAndUsersViewToUpdate(UI ui, UsersView usersView){
        this.ui = ui;
        this.usersView = usersView;
    }

    public void setUIAndOrdersViewToUpdate(UI ui, OrdersView ordersView){
        this.ui = ui;
        this.ordersView = ordersView;
    }

    @StreamListener(target = GUIMQBinding.USER_REGISTRATION)
    public void processUserRegistration(GassmanMessage<UserDTO> msg) {
        if(checkInstance(msg)) {
            if (usersView != null) {
                ui.access(() -> usersView.refreshUserGrid());
            }
        }
    }

    @StreamListener(target = GUIMQBinding.USER_ORDER)
    public void processUserOrderRegistration(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)) {
            if (productsView != null) {
                ui.access(() -> productsView.refreshProductGrid());
                ui.access(() -> productsView.refreshProductOrdersGrid(msg.getPayload().getProduct().getProductId()));
            }
            if (ordersView != null) {
                ui.access(() -> ordersView.refreshOrdersGrid());
            }
        }
    }

    @StreamListener(target = GUIMQBinding.ORDER_PAYMENT_CONFIRMATION)
    public void processOrderPaymentConfirmation(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)) {
            if (usersView != null) {
                ui.access(() -> usersView.refreshUserGrid());
            }
            if (productsView != null) {
                ui.access(() -> productsView.refreshProductOrdersGrid(msg.getPayload().getProduct().getProductId()));
            }
            if (ordersView != null) {
                ui.access(() -> ordersView.refreshOrdersGrid());
            }
        }
    }

    @StreamListener(target = GUIMQBinding.USER_CANCELLATION)
    public void processUserCancellation(GassmanMessage<UserDTO> msg) {
        if(checkInstance(msg)) {
            if (usersView != null) {
                ui.access(() -> usersView.refreshUserGrid());
            }
        }
    }

    @StreamListener(target = GUIMQBinding.RECHARGE_USER_CREDIT)
    public void processRechargeUserCredit(GassmanMessage<UserDTO> msg) {
        if(checkInstance(msg)) {
            if (usersView != null) {
                ui.access(() -> usersView.refreshUserGrid());
            }
        }
    }

    @StreamListener(target = GUIMQBinding.ORDER_CANCELLATION)
    public void processOrderCancellation(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)) {
            if (productsView != null) {
                ui.access(() -> productsView.refreshProductGrid());
                ui.access(() -> productsView.refreshProductOrdersGrid(msg.getPayload().getProduct().getProductId()));
            }
            if (ordersView != null) {
                ui.access(() -> ordersView.refreshOrdersGrid());
            }
            if (usersView != null) {
                ui.access(() -> usersView.refreshUserGrid());
            }
        }
    }

    public void setUIAndProductsViewToUpdate(UI ui, ProductsView productsView) {
        this.ui = ui;
        this.productsView = productsView;
    }

    private boolean checkInstance(GassmanMessage msg) {
        return msg.getParams() != null && msg.getParams().length>=2
                && instanceId.equalsIgnoreCase(msg.getParams()[0])
                && botName.equalsIgnoreCase(msg.getParams()[1]);
    }
}
