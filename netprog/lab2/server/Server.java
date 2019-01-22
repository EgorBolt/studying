import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable {
    private static Socket client;

    Server(Socket client) {
        Server.client = client;
    }

    @Override
    public void run() {

        try (
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        ) {
            long timer = 0;
            double iteration = 0;
            double speed = 0;
            double averageSpeed = 0;
            long sentBytes = 0;
            double resultSpeed = 0;
            File result;

            String fileName = inputStream.readUTF();
            long fileSize = inputStream.readLong();

            File path = new File("./uploads");
            if (!path.exists()) {
                path.mkdir();
            }

            if (new File("." + File.separator + "uploads" + File.separator + fileName).isFile()) {
                int i = 1;
                while (new File("." + File.separator + "uploads" + File.separator + fileName.substring(0, fileName.indexOf('.')) + "(" + i + ")." + fileName.substring(fileName.indexOf('.') + 1)).isFile()) {
                    i++;
                }
                String newFileName = fileName.substring(0, fileName.indexOf('.')) + "(" + i + ")." + fileName.substring(fileName.indexOf('.') + 1);
                result = new File("." + File.separator + "uploads", newFileName);
            }
            else {
                result = new File("./uploads", fileName);
            }

            FileOutputStream resultStream = new FileOutputStream(result);
            outputStream.writeBoolean(true);

            while (result.length() < fileSize) {
                long startTime = System.currentTimeMillis();
                int arraySize = inputStream.readInt();
                sentBytes += arraySize;
                if (arraySize < 0) {
                    break;
                }
                byte[] readArray = new byte[arraySize];
                inputStream.readFully(readArray);
                long endTime = System.currentTimeMillis();
                timer += (endTime - startTime);
                if (timer >= 1000) {
                    speed = (double)sentBytes / timer / 1024;
                    System.out.println("Current network speed for IP " + fileName +  ": " + speed + " kb/s");
                    timer = 0;
                    sentBytes = 0;
                    averageSpeed += speed;
                    iteration++;
                }
                resultStream.write(readArray);
            }

            //TODO: Прикрутить Maven
            if (iteration == 0) {
                System.out.println("\nFile " + fileName + " has been successfully saved.\n" + sentBytes +
                        " bytes were sent in " + timer + " milliseconds.");
            }
            else {
                resultSpeed = averageSpeed / iteration;
                System.out.println("\nFile " + fileName + " has been successfully saved.\nAverage speed: " + resultSpeed + " kb/s");
            }

            outputStream.writeBoolean(true);

            client.close();

        } catch (FileNotFoundException eFileNotFound) {
            System.err.println(eFileNotFound.getMessage());
            System.exit(-2);
        } catch (UnknownHostException eUnknownHost) {
            System.err.println(eUnknownHost.getMessage());
            System.exit(-3);
        } catch (IOException eIO) {
            System.err.println("Client has been shut down, terminating the thread.");
        }
        finally {
            try {
                client.close();
                result.close();
            } catch (IOException eServerClose) {
                System.err.println("Error while closing server's resources.")
            }
        }
    }
}
