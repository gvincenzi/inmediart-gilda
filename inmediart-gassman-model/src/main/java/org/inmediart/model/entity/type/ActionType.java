package org.inmediart.model.entity.type;

public enum ActionType {
    BUY("Acquisto"),USER_SEARCH("Ricerca Utente"),USER_MANAGEMENT("Gestione Utente"),USER_CREDIT("Ricarica credito di un utente");

    private String label;

    ActionType(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
