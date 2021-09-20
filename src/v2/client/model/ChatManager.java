package v2.client.model;

import v2.client.model.criteries.Criterion;
import v2.client.model.criteries.Message;
import v2.client.model.criteries.NowOnline;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatManager {

    public void startConnection() {
        Main main = new Main();
        Socket socket = null;
        DataInputStream dataInputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            socket = new Socket(main.getHost(), Integer.parseInt(main.getPort()));
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            main.setSocket(socket);
            main.setDataInputStream(dataInputStream);
            main.setDataOutputStream(dataOutputStream);

            new Thread(new MessegeReader(dataInputStream)).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (socket != null) {
                    socket.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void sendMessage(Criterion criterion, String message) {
        try {
            Main main = new Main();

            if(criterion instanceof v2.client.model.criteries.Message) {
                message = main.getUserName() + ":" + message;
            }
            message = criterion.getCriter() + message;
            DataOutputStream dataOutputStream = main.getDataOutputStream();
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //вырезка критерия из сообщения
    public Criterion cutCriterion(String message) {
        Pattern pattern = Pattern.compile("^<\\w+?>");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            message = message.substring(matcher.start(), matcher.end());
        }
        if (message.equals(new Message().getCriter())) {
            return new Message();
        } else if (message.equals(new NowOnline().getCriter())) {
            return new NowOnline();
        } else {
            return null;
        }
    }

    //вырезка сообщения(без критерия)
    public String cutMessege(String message) {
        Criterion criterion = cutCriterion(message);
        int criterionLength = criterion.getCriter().length();
        String newMessage = message.substring(criterionLength);
        return newMessage;
    }
}
