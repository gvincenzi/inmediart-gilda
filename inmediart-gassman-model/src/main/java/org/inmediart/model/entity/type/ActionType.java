package org.inmediart.model.entity.type;

public enum ActionType {
    BUY("Acquisto");

    private String label;

    ActionType(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
