package newspring.forinject;

import newspring.Inject;

public class Patient {

    @Inject
    private Glucose glucose;

    public Glucose getGlucose() {
        return glucose;
    }
}
