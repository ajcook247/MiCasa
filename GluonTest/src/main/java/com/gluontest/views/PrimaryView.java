package com.gluontest.views;

import com.gluonhq.particle.annotation.ParticleView;
import com.gluonhq.particle.application.ParticleApplication;
import com.gluonhq.particle.state.StateManager;
import com.gluonhq.particle.view.View;
import com.gluonhq.particle.view.ViewManager;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionMap;
import org.controlsfx.control.action.ActionProxy;

@ParticleView(name = "primary", isDefault = true)
public class PrimaryView implements View {

    @Inject ParticleApplication app;
    
    @Inject private ViewManager viewManager;
    
    @Inject private StateManager stateManager;
    
    private final VBox controls = new VBox(15);
    private Label label;
    
    @Inject private ResourceBundle resourceBundle;

    private Action actionSignin;

    @Override
    public void init() {
        ActionMap.register(this);
        actionSignin =  ActionMap.action("signin");
        
        label = new Label();

        Button button = new Button(resourceBundle.getString("button.text"));
        button.setOnAction(e -> viewManager.switchView("secondary"));
        
        controls.getChildren().addAll(label, button);
        controls.setAlignment(Pos.CENTER);
        
        stateManager.setPersistenceMode(StateManager.PersistenceMode.USER);
        addUser(stateManager.getProperty("UserName").orElse("").toString());
    }

    @Override
    public Node getContent() {
        return controls;
    }
    
    @Override
    public void start() {
        app.getParticle().getToolBarActions().add(0, actionSignin);
    }
    
    @Override
    public void stop() {
        app.getParticle().getToolBarActions().remove(actionSignin);
    }
    
    public void addUser(String userName) {
        label.setText(resourceBundle.getString("label.text") + (userName.isEmpty() ? "" :  ", " + userName) + "!");
        stateManager.setProperty("UserName", userName);
    }

    @ActionProxy(text="Sign In")
    private void signin() {
        TextInputDialog input = new TextInputDialog(stateManager.getProperty("UserName").orElse("").toString());
        input.setTitle("User name");
        input.setHeaderText(null);
        input.setContentText("Input your name:");
        input.showAndWait().ifPresent(this::addUser);
    }
        
}