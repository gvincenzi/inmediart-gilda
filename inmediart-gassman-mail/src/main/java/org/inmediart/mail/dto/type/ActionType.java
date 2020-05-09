package org.inmediart.mail.dto.type;

public enum ActionType {
    BUY("Acquisto contenuto multimediale"),
    BUY_PHISICAL("Acquisto prodotto fisico");

    private String label;

    ActionType(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
