package v2.server;

import v2.server.criteries.Criterion;
import v2.server.criteries.Message;
import v2.server.criteries.NowOnline;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static List<ChatHandler> handlers = Collections.synchronizedList(new ArrayList<>());

    public static List<ChatHandler> getHandlers() {
        return handlers;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                ChatHandler chatHandler = new ChatHandler(socket);
                new Thread(chatHandler).start();
                new Server().addHandler(chatHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //отправка сообщения только с его критерием
    public void sendMessage(Criterion criterion, String message, DataOutputStream dataOutputStream) {
        try {
            synchronized (dataOutputStream) {
                dataOutputStream.writeUTF(criterion.getCriter() + message);
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //рассылка сообщения
    public void distributionMessage(Criterion criterion, String message) {
        //копирование массива
        List<ChatHandler> heandlers =
                Collections.synchronizedList(new ArrayList<>(Server.getHandlers()));
        Iterator iterator = heandlers.iterator();
        while (iterator.hasNext()) {
            ChatHandler chatHandler = (ChatHandler) iterator.next();
            sendMessage(criterion, message, chatHandler.getDataOutputStream());
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

    public void removeHandler(ChatHandler chatHandler) {
        handlers.remove(chatHandler);
        changeOnline();
    }

    public void addHandler(ChatHandler chatHandler) {
        handlers.add(chatHandler);
        changeOnline();
    }

    public void changeOnline() {
        distributionMessage(new NowOnline(), String.valueOf(handlers.size()));
    }
}
