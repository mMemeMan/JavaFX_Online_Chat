package v2.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import v2.client.model.ChatManager;
import v2.client.model.Main;
import v2.client.model.criteries.Message;


public class Controller {
    @FXML
    private TextField textField = new TextField();
    @FXML
    private TextArea textArea = new TextArea();
    @FXML
    private Text infoOnlineText = new Text();
    @FXML
    private AnchorPane menuPane = new AnchorPane();
    @FXML
    private AnchorPane chatPane = new AnchorPane();
    @FXML
    private TextField menuTextField = new TextField();

    public TextField getTextField() {
        return textField;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Controller() {
        //что бы не возникало ошибок при обращении к контроллеру из вне
        Main.setController(this);
    }

    public void actionTextField(ActionEvent event) {
        ChatManager chatManager = new ChatManager();
        chatManager.sendMessage(new Message(), textField.getText());
        textField.setText("");
    }

    public void setOnlineText(int online) {
        infoOnlineText.setText(String.valueOf(online));
    }

    public Text getInfoOnlineText() {
        return infoOnlineText;
    }

    public void actionGoToChat(ActionEvent event) {
        String userName = menuTextField.getText();
        Main main = new Main();
        main.setUserName(userName);
        chatPane.setVisible(true);
        menuPane.setVisible(false);

        ChatManager chatManager = new ChatManager();
        chatManager.startConnection();
    }
}
