import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String args[]) {

        if (args.length != 3) {
            System.err.println("Error: the amount of arguments is more or less than three.");
            System.exit(-1);
        }

        try {
            Socket socket = new Socket(args[1], Integer.parseInt(args[2]));
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            File target = new File(args[0]);
            FileInputStream in = new FileInputStream(target);

            int byteAmountRead;
            String fileName = target.getName();
            //String fileSize = String.valueOf(target.length());
            long fileSize = target.length();

            outputStream.writeUTF(fileName);
            outputStream.writeLong(fileSize);

            while(!inputStream.readBoolean()) {
                continue;
            }

            boolean status = false;

            while(!socket.isOutputShutdown() && in.available() > 0) {
                int arraySize;
                if (in.available() >= 4096) {
                    arraySize = 4096;
                }
                else {
                    arraySize = in.available();
                }
                outputStream.writeInt(arraySize);
                byte[] readArray = new byte[arraySize];
                byteAmountRead = in.read(readArray, 0, arraySize);
                outputStream.write(readArray, 0, arraySize);
                outputStream.flush();
            }

            boolean stopClient = inputStream.readBoolean();
            if(stopClient) {
                System.out.println("File " + fileName + " has been successfully passed.");
            }
            else {
                System.err.println("Something wrong with sending file, abort.");
            }

            inputStream.close();
            outputStream.close();
            in.close();
            socket.close();

        } catch (FileNotFoundException eFileNotFound) {
            System.err.println(eFileNotFound.getMessage());
            System.exit(-2);
        }
        catch (UnknownHostException eUnknownHost) {
            System.err.println(eUnknownHost.getMessage());
            System.exit(-3);
        } catch (IOException eIO) {
            System.err.println(eIO.getMessage());
            System.exit(-4);
        }
    }
}
