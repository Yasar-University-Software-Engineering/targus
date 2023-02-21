package com.targus.ui;

public class Mediator {
    private InputsController inputsController;
    private InformativeController informativeController;
    private MapController mapController;
    private EmptyController emptyController;

    public void setInputsController(InputsController inputsController) {
        this.inputsController = inputsController;
    }

    public void setInformativeController(InformativeController informativeController) {
        this.informativeController = informativeController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public void setEmptyController(EmptyController emptyController) {
        this.emptyController = emptyController;
    }

    public void resizeMapPane(double width, double height) {
        mapController.resizePane(width, height);
    }
}
