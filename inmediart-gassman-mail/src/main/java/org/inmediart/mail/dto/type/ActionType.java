package org.inmediart.mail.dto.type;

public enum ActionType {
    BUY("Acquisto"),
    BUY_WITH_DELIVERY("Acquisto con consegna");

    private String label;

    ActionType(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
