package v2.server;


import v2.server.criteries.Criterion;
import v2.server.criteries.Message;
import v2.server.criteries.NowOnline;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    @Override
    public void run() {
        Server server = new Server();
        try {

            while (true) {
                String message = dataInputStream.readUTF();
                Criterion crit = server.cutCriterion(message);
                if (crit instanceof Message) {
                    message = server.cutMessege(message);
                    Criterion criterion = new Message();
                    server.distributionMessage(criterion, message);
                } else if (crit instanceof NowOnline) {
                }
            }
        } catch (IOException e) {
        } finally {
            server.removeHandler(this);
        }

    }
}
