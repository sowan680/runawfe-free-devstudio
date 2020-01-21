package ru.runa.gpd.lang.model.bpmn;

import java.util.List;

public interface ConnectableViaDottedTransition {
    void addLeavingDottedTransition(DottedTransition transition);

    default boolean canAddArrivingDottedTransition(ConnectableViaDottedTransition source) {
        return getArrivingDottedTransitions().size() == 0 && !this.getClass().equals(source.getClass());
    }

    default boolean canAddLeavingDottedTransition() {
        return getLeavingDottedTransitions().size() == 0;
    }

    List<DottedTransition> getLeavingDottedTransitions();

    List<DottedTransition> getArrivingDottedTransitions();
}
