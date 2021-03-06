package eu.timhuebert.jweb._core.connection;

import lombok.Getter;

import java.io.*;
import java.net.Socket;

@Getter
public class Connection {

    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dataOut;

    public Connection(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        dataOut = new BufferedOutputStream(socket.getOutputStream());
    }

    public void close() throws IOException {
        in.close();
        out.close();
        dataOut.close();
    }
}
