package org.inmediart.commons.messaging;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GassmanMessage<T> {
    private T payload;
    private String[] params;

    public GassmanMessage(T payload, String ... params) {
        this.payload = payload;
        this.params = params;
    }
}
