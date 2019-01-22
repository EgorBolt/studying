
import java.net.*;
import java.util.UUID;
import java.util.Vector;

public class Node {
    Vector<byte[]> messageQueue = new Vector<>();
    Vector<Child> childrenList = new Vector<>();
    Vector<String> uuidList = new Vector<>();
    private InetAddress parentIP;
    private int parentPort;
    private String nodeName;
    private int nodePort;
    private int packageLoss;

    Node() { }

    /*Порядок получения агрументов:
     * название узла
     * процент потерь
     * собственный порт узла
     * (opt)IP родителя узла
     * (opt)порт родителя узла*/


    void start(String args[]) {
        Thread sender;
        Thread receiver;
        Thread input;

        setNodeName(args[0]);
        setPackageLoss(Integer.parseInt(args[1]));
        setNodePort(Integer.parseInt(args[2]));

        input = new Thread(new Input(messageQueue));
        input.start();


            switch (args.length) {
                case 3:
                    receiver = new Thread(new Receiver(nodePort, packageLoss, messageQueue, childrenList, uuidList));
                    sender = new Thread(new Sender(nodeName, messageQueue, childrenList, uuidList));
                    receiver.start();
                    sender.start();
                    break;
                case 5:
                    setParentIP(args[3]);
                    setParentPort(Integer.parseInt(args[4]));
                    receiver = new Thread(new Receiver(nodePort, packageLoss, messageQueue, childrenList, uuidList));
                    sender = new Thread(new Sender(nodeName, messageQueue, childrenList, uuidList, nodePort, parentIP, parentPort));
                    receiver.start();
                    sender.start();
                    break;
                default:
                    System.err.println("Error: wrong amount of arguments, terminating the program.");
                    System.exit(-1);
            }


//        sender = new Sender(args[0], Integer.parseInt(args[2]), args[3], Integer.parseInt(args[4]));
    }

    void setParentIP(String parentIP) {
        try {
            this.parentIP = InetAddress.getByName(parentIP);
        } catch (UnknownHostException eCreateSender) {
            System.err.println(eCreateSender.getMessage());
            System.exit(-2);
        }
    }

    void setParentPort(int parentPort) {
        this.parentPort = parentPort;
    }

    void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    void setPackageLoss(int packageLoss) {
        this.packageLoss = packageLoss;
    }
}
