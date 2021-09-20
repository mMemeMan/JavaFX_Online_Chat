package v2.client.model;

import v2.client.model.criteries.Criterion;
import v2.client.model.criteries.Message;
import v2.client.model.criteries.NowOnline;

import java.io.DataInputStream;
import java.io.IOException;

public class MessegeReader implements Runnable {
    DataInputStream dataInputStream;

    public MessegeReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = dataInputStream.readUTF();
                ChatManager chatManager = new ChatManager();
                Criterion criterion = chatManager.cutCriterion(message);
                if (criterion instanceof Message) {
                    message = chatManager.cutMessege(message);
                    saveMessage(message);
                } else if (criterion instanceof NowOnline) {
                    message = chatManager.cutMessege(message);
                    changeOnline(Integer.parseInt(message));
                }
            }
        } catch (IOException e) {
        }
    }

    private void saveMessage(String message) {
        Main.getController().getTextArea()
                .appendText(message + "\n");
    }

    private void changeOnline(int nowOnline) {
        Main.getController().setOnlineText(nowOnline);
    }
}
