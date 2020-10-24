package micasa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.util.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

    /*
        Controls the contact form, ensuring that all information is entered correctly
        before an email is sent to the developer.
        Author: Adam Cook
    */

public class ContactController implements Initializable {
    
    @FXML
    public AnchorPane toolbar; //Represents the toolbar
    @FXML
    private TextField userEmail; //Represents the field to enter the user email
    @FXML
    private PasswordField password; //Represetns the field to enter the user password
    @FXML
    private TextField emailSubject; //Represents the field to enter the user subject
    @FXML
    private TextArea emailMessage; //Represents the field to enter the message
    @FXML
    private Text sendResponse; //Text to denote if successful send
    @FXML
    private Button sendEmail; //Button to send the email
    @FXML
    private RadioButton r1; //Bug Report
    @FXML
    private RadioButton r2; //Crash Report
    @FXML
    private RadioButton r3; //Suggestion
    @FXML
    private RadioButton r4; //Need a Friend
    
    Delta d = new Delta();

    //Handles closing the window
    @FXML
    private void handleClose(ActionEvent event) {
        //Gets the source window and hides it
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    //Sends an email composed of the information in the contact form
    @FXML
    private void sendEmail(ActionEvent event){
        try {
            String host = "smtp.gmail.com";
            String user = userEmail.getText(); //USER EMAIL
            String pass = password.getText(); //USER PASSWORD
            String to = "ajc14654@gmail.com"; //OWNER EMAIL
            String from = user; //SENDER
            String subject = emailSubject.getText(); //GET SUBJECT RB
            String messageText = emailMessage.getText(); //GET MESSAGE TA
            boolean sessionDebug = false;
            
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", true);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", user);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "auth");
            
            //Ensures security in email session
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            
            //Changes text of subject depending on entered title and radio button selected
            if(r1.isSelected())
                msg.setSubject(r1.getText() + " | " + subject);
            else if(r2.isSelected())
                msg.setSubject(r2.getText() + " | " + subject);
            else if(r3.isSelected())
                msg.setSubject(r3.getText() + " | " + subject);
            else if(r4.isSelected())
                msg.setSubject(r4.getText() + " | " + subject);
            
            msg.setSentDate(new Date()); //Sets the date of the message
            msg.setText(messageText); //Sets the text of the message
            
            //Sends message once all information is entered correctly
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            sendResponse.setText("Message successfully sent!");
            
            //Resets all text fields
            userEmail.setText("");
            password.setText("");
            emailSubject.setText("");
            emailMessage.setText("");
            r1.setSelected(false);
            r2.setSelected(false);
            r3.setSelected(false);
            r4.setSelected(false);

        }catch(Exception e){
            //Prints an error message to the form, negates mail session
            sendResponse.setText("Error sending message. Please make sure all \ninformation is entered correctly.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Retrieves x and y mouse coordinates when mouse pressed on toolbar
        toolbar.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent evt) {
                d.setX(evt.getX());
                d.setY(evt.getY());
            }
        });
        
        //Moves the window based on mouse x and y
        toolbar.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent evt) {
            toolbar.getScene().getWindow().setX( evt.getScreenX() - d.getX());
            toolbar.getScene().getWindow().setY( evt.getScreenY() - d.getY());
            }
            
        });
    }       
}