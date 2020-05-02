package org.inmediart.gui.vaadin.view.product;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.inmediart.gui.client.ProductResourceClient;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.inmediart.gui.vaadin.view.ButtonLabelConfig;

import java.util.ArrayList;

@SpringComponent
@UIScope
public class ProductEditor extends HorizontalLayout implements KeyNotifier {
    private final ProductResourceClient productResourceClient;
    private final ProductLabelConfig productLabelConfig;
    private final OrderLabelConfig orderLabelConfig;
    private final ButtonLabelConfig buttonLabelConfig;

    private ProductDTO productDTO;

    private Grid<OrderDTO> grid;

    private OrderEditor orderEditor;

    private TextField name, description, url, password;
    private NumberField price;
    private Checkbox active;
    private Button save, reset, delete, openDocument;

    private Binder<ProductDTO> binder = new Binder<>(ProductDTO.class);
    private ChangeHandler changeHandler;

    public ProductEditor(ProductResourceClient productResourceClient, OrderEditor orderEditor, ProductLabelConfig productLabelConfig, ButtonLabelConfig buttonLabelConfig, OrderLabelConfig orderLabelConfig) {
        this.productResourceClient = productResourceClient;
        this.buttonLabelConfig = buttonLabelConfig;
        this.productLabelConfig = productLabelConfig;
        this.orderLabelConfig = orderLabelConfig;
        this.orderEditor = orderEditor;
        this.grid = new Grid<>(OrderDTO.class);

        /* Fields to edit properties in Product entity */
        name = new TextField(productLabelConfig.getName());
        description = new TextField(productLabelConfig.getDescription());
        url = new TextField(productLabelConfig.getUrl());
        password = new TextField(productLabelConfig.getPassword());
        price = new NumberField(productLabelConfig.getPrice());
        active = new Checkbox(productLabelConfig.getActive());

        /* Action buttons */
        save = new Button(buttonLabelConfig.getSave(), VaadinIcon.CHECK.create());
        reset = new Button(buttonLabelConfig.getReset());
        delete = new Button(buttonLabelConfig.getDelete(), VaadinIcon.TRASH.create());
        openDocument = new Button(buttonLabelConfig.getOpenDocument(), VaadinIcon.BULLSEYE.create());

        HorizontalLayout actions = new HorizontalLayout(save, openDocument);
        VerticalLayout editorFields = new VerticalLayout(name, description, url, password, price, active, actions);
        editorFields.setWidth("30%");
        grid.setColumns("user","actionType","paid","paymentExternalReference","paymentExternalDateTime","amount");
        grid.getColumnByKey("user").setHeader(orderLabelConfig.getUser());
        grid.getColumnByKey("actionType").setHeader(orderLabelConfig.getActionType());
        grid.getColumnByKey("paid").setHeader(orderLabelConfig.getPaid());
        grid.getColumnByKey("paymentExternalReference").setHeader(orderLabelConfig.getPaymentExternalReference());
        grid.getColumnByKey("paymentExternalDateTime").setHeader(orderLabelConfig.getPaymentExternalDateTime());
        grid.getColumnByKey("amount").setHeader(orderLabelConfig.getAmount());

        grid.setWidth("100%");

        // Connect selected Product to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            orderEditor.editOrder(e.getValue());
        });

        VerticalLayout gridOrders = new VerticalLayout();
        gridOrders.add(grid,orderEditor);
        gridOrders.setHeightFull();

        // Listen changes made by the editor, refresh data from backend
        orderEditor.setChangeHandler(() -> {
            orderEditor.setVisible(false);
            grid.setItems(productResourceClient.findProductOrders(productDTO.getProductId()));

            changeHandler.onChange();
        });

        add(editorFields, gridOrders);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        setWidthFull();

        openDocument.getElement().getThemeList().add("success");
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        reset.addClickListener(e -> editProduct(productDTO));
        openDocument.addClickListener(e ->
                        openDocument.getUI().ifPresent(ui ->
                                        ui.getPage().open(productDTO.getUrl())
                        )
        );

        setVisible(false);
    }

    void delete() {
        productResourceClient.deleteProduct(productDTO.getProductId());
        changeHandler.onChange();
    }

    void save() {
        if(productDTO.getProductId() != null){
            productResourceClient.updateProduct(productDTO.getProductId(), productDTO);
        } else {
            productResourceClient.postProduct(productDTO);
        }
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = productDTO.getProductId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.productDTO = productResourceClient.findById(productDTO.getProductId());
            grid.setItems(productResourceClient.findProductOrders(productDTO.getProductId()));
        }
        else {
            grid.setItems(new ArrayList(0));
            this.productDTO = productDTO;
        }

        reset.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(this.productDTO);

        setVisible(true);

        // Focus first name initially
        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    public void refreshProductOrdersGrid(Long productId){
        if(productId!=null && productDTO != null && productId.equals(productDTO.getProductId())){
            grid.setItems(productResourceClient.findProductOrders(productDTO.getProductId()));
        }
    }


}
