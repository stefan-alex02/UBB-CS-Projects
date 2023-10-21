import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String args[]) {
        if (args.length != 2) {
            System.err.println("Invalid number of arguments.\nUsage: ./[file] [SERVER_ADDRESS] [PORT]");
            return;
        }
        String SERVER_ADDRESS = args[0];
        if (!SERVER_ADDRESS.matches(
                "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5]).){3}" +
                        "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$")) {
            System.err.println("The first argument is not a valid server address.");
            return;
        }
        int SERVER_PORT = Integer.parseInt(args[1]);


        Socket socket = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            String s1 = readInputString("Type 1st string : ", reader);
            String s2 = readInputString("Type 2nd string : ", reader);
            writeStringsToSocket(s1, s2, socket);
            readAndHandleResponseFromSocket(socket);
        } catch (IOException e) {
            System.err.println("Caught exception " + e.getMessage());
        } finally {
            closeStreams(socket,reader);
        }
    }

    private static void readAndHandleResponseFromSocket(Socket c) throws IOException {
        DataInputStream socketIn = new DataInputStream(c.getInputStream());
        int statusCode = socketIn.readUnsignedShort();
        System.out.println("> Received status code : " + statusCode);
        switch (statusCode) {
            case 200:
                System.out.println("> Result string is : " + readStringFromStream(socketIn));
                break;
            case 401:
                System.out.println("Error : input strings must not be empty.");
                break;
            default:
                System.out.println("An unknown error occurred.");
                break;
        }
    }

    private static String readStringFromStream(DataInputStream inputStream) throws IOException {
        int length = inputStream.readShort();
        return new String(inputStream.readNBytes(length), StandardCharsets.UTF_8);
    }

    private static void writeStringsToSocket(String s1, String s2, Socket c) throws IOException {
        DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());
        socketOut.writeShort(s1.length());
        socketOut.write(s1.getBytes());
        socketOut.writeShort(s2.length());
        socketOut.write(s2.getBytes());
        socketOut.flush();
    }

    private static String readInputString(String message, BufferedReader reader) throws IOException {
        System.out.print(message);
        return reader.readLine();
    }

    private static void closeStreams(Socket socket, BufferedReader reader) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Could not close socket!");
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Could not close reader!");
            }
        }
    }
}