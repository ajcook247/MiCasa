package com.gluontest;

import com.gluonhq.particle.application.ParticleApplication;
import javafx.scene.Scene;
import static org.controlsfx.control.action.ActionMap.actions;

public class GluonTest extends ParticleApplication {

    public GluonTest() {
        super("Gluon Desktop Application");
    }

    @Override
    public void postInit(Scene scene) {
        scene.getStylesheets().add(GluonTest.class.getResource("style.css").toExternalForm());

        setTitle("Gluon Desktop Application");

        getParticle().buildMenu("File -> [exit]", "Help -> [about]");
        
        getParticle().getToolBarActions().addAll(actions("---", "about", "exit"));
    }
}