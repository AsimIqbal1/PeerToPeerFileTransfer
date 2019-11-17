import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class Peer04 {

    private static Integer[] connectedPeers04Ports = new Connections().getPeer04ConnectionPorts();
    private static String actualFileName;
    private static TimerTask task = new TimerTask()
    {
        public void run()
        {
            System.out.println("Server did not respond..");
        }
    };


    public static void main(String[] args) throws IOException, InterruptedException {

//        RunnableThread R1 = new RunnableThread("Thread # 1");
//        R1.start();

        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("Enter you state\nServer: S\nClient: C\n\n**To stop type end.**");
            String input = sc.next();
            if(input.equalsIgnoreCase("S")){
                DatagramSocket serverSocket= new DatagramSocket(4444);
                server(serverSocket);
            }else if(input.equalsIgnoreCase("C")){



                /**********************CLIENT CODE*************************/


                System.out.print("Enter the name of file you want to receive: ");
                //name the file that is required
                String fileName = sc.next();
                requestFile(fileName, connectedPeers04Ports);


                /**********************CLIENT CODE*************************/



            }else if(input.equalsIgnoreCase("end")){
                break;
            }else{
                System.out.println("Wrong Input\n");
            }
        }


    }

    private static boolean requestFile(String fileName, Integer[] connectedPeers) throws IOException, InterruptedException {
        String serverData;

        int filePort = -1;
        boolean fileFound = false;

        //request to all the connected peers one by one
        for (int portNo : connectedPeers) {
            if(fileFound){
                break;
            }
            InetAddress IP = InetAddress.getByName("localhost");
            DatagramSocket clientSocket = new DatagramSocket();

            byte[] sendBuffer;
            byte[] receiverBuffer = new byte[1024];

            //convert string to bytes to transfer data in stream
            sendBuffer = fileName.getBytes();

            //make a sending data gram of packet

            DatagramPacket sendPacket
                    = new DatagramPacket(sendBuffer, sendBuffer.length, IP, portNo);
            clientSocket.send(sendPacket);


            //make a datagram to receive the data from peer. Here peer will reply if it has the requested file or not
            DatagramPacket receivePacketConfirm
                    = new DatagramPacket(receiverBuffer, receiverBuffer.length);
            try {
                clientSocket.setSoTimeout(3000);
                clientSocket.receive(receivePacketConfirm);
                serverData = new String(receivePacketConfirm.getData());
                System.out.println("Server response is: " +serverData);
                clientSocket.setSoTimeout(0);
            }catch(SocketTimeoutException ste){
                System.out.println(portNo+" is not responding..");
                continue;
            }
            DatagramPacket receivePacket
                    = new DatagramPacket(receiverBuffer, receiverBuffer.length);
            clientSocket.receive(receivePacket);

            serverData = new String(receivePacket.getData());
            System.out.println("\n Server: "+serverData);

            //if peer contains the file, send peer your port number so that it can send data to that port
            if(serverData.contains("Yes")){
                System.out.println("Server Data is yes");
                fileFound = true;
                filePort = portNo;

                int myPort = 4444;
                byte[] buff;
                buff = Integer.toString(myPort).getBytes();

                sendPacket = new DatagramPacket(buff, buff.length, IP, portNo);
                clientSocket.send(sendPacket);

                //receive the file here
                byte[] b = new byte[2048];
                Socket socket;
                while(true) {
                    try {
                        socket = new Socket("localhost", 4444);
                        break;
                    } catch (Exception e) {
                        System.out.println("Waiting for server.. ");
                        sleep(1000);
                    }
                }
                InputStream is = socket.getInputStream();

                //will write the received file to provided location
                String fileNameNew = "F:\\ItelliJ\\Projects\\P2PFileTransfer\\src\\Peer04Files\\"+fileName;
                FileOutputStream fos = new FileOutputStream(fileNameNew);

                is.read(b, 0, b.length);
                fos.write(b, 0, b.length);
                socket.close();

            }
            clientSocket.close();

        }

        if(fileFound){
            System.out.println("File is found on port: "+filePort);
            return true;
        }else {
            System.out.println("File not found");
        }

        return false;
    }

    private static void server(DatagramSocket serverSocket) throws IOException, InterruptedException {

        String clientData;
        String serverData;
        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;

        //make datagram to receive the requests from client/peers
        DatagramPacket receivedPacket
                = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        serverSocket.receive(receivedPacket);



        //get the IP of client/peer
        InetAddress IP = receivedPacket.getAddress();

        System.out.println("IP: "+IP);

        int portNo = receivedPacket.getPort();
        System.out.println("Port no: "+portNo);

        //inform client that you received it's request
        serverData = "r";
        sendBuffer = serverData.getBytes();
        DatagramPacket sendPacketConfirm
                = new DatagramPacket(sendBuffer, sendBuffer.length, IP, portNo);

        serverSocket.send(sendPacketConfirm);

        //////////////////////////////////////////////


        //convert packet to string
        clientData = new String(receivedPacket.getData());
        System.out.println("\nClient: "+clientData);

        //check if current peer(peer04) has the file or not
        String filename = "F:/ItelliJ/Projects/P2PFileTransfer/src/Peer04Files/"+clientData;
        actualFileName = clientData.trim();
        filename = filename.trim();
        File file = new File(filename);

        System.out.println("File: "+ Files.isReadable(file.toPath()));

        System.out.println("File exists => "+file.exists());

        if(file.exists()){
            //has the file
            serverData = "Yes";

            //inform client/peer that the file is found
            sendBuffer = serverData.getBytes();
            DatagramPacket sendPacket
                    = new DatagramPacket(sendBuffer, sendBuffer.length, IP, portNo);

            serverSocket.send(sendPacket);

            //receive client/peer port number
            byte[] receiveBuffer1 = new byte[1024];


            DatagramPacket receivedPacket2
                    = new DatagramPacket(receiveBuffer1, receiveBuffer1.length);
            serverSocket.receive(receivedPacket2);

            String clientPort =  new String(receivedPacket2.getData());
            System.out.println("ClientPort =>>>>>>> "+clientPort);
            clientPort = clientPort.trim();
            System.out.println(clientPort.length());
            int clientPortNumber = Integer.parseInt(clientPort);

            //send the file now
            ServerSocket serverSocketNew = new ServerSocket(clientPortNumber);
            Socket socket = serverSocketNew.accept();

            FileInputStream fis = new FileInputStream(filename);

            byte b[] = new byte[2048];

            fis.read(b, 0, b.length);

            OutputStream os = socket.getOutputStream();
            os.write(b, 0, b.length);

            socket.close();
            serverSocketNew.close();
        }else{
            boolean isFileFound = requestFile(actualFileName, connectedPeers04Ports);
            if(isFileFound){
                serverData = "Yes";
                System.out.println("Transferring file to client..");
                //inform client/peer that the file is found
                sendBuffer = serverData.getBytes();
                DatagramPacket sendPacket
                        = new DatagramPacket(sendBuffer, sendBuffer.length, IP, portNo);

                serverSocket.send(sendPacket);

                //receive client/peer port number
                int clientPortNumber;
                while(true) {
                    try {
                        byte[] receiveBuffer1 = new byte[1024];
                        DatagramPacket receivedPacket2
                                = new DatagramPacket(receiveBuffer1, receiveBuffer1.length);
                        serverSocket.receive(receivedPacket2);

                        String clientPort = new String(receivedPacket2.getData());
                        System.out.println("ClientPort =>>>>>>> " + clientPort);
                        clientPort = clientPort.trim();
                        System.out.println(clientPort.length());
                        clientPortNumber = Integer.parseInt(clientPort);
                        break;
                    } catch (NumberFormatException ignored) {
                    }
                }
                //send the file now
                ServerSocket serverSocketNew = new ServerSocket(clientPortNumber);
                Socket socket = serverSocketNew.accept();

                FileInputStream fis = new FileInputStream(filename);

                byte b[] = new byte[2048];

                fis.read(b, 0, b.length);

                OutputStream os = socket.getOutputStream();
                os.write(b, 0, b.length);
                socket.close();
                serverSocketNew.close();
            }else {
                //before saying no check connected peers for file..
                serverData = "No";
                sendBuffer = serverData.getBytes();
                DatagramPacket sendPacket
                        = new DatagramPacket(sendBuffer, sendBuffer.length, IP, portNo);
                serverSocket.send(sendPacket);
            }

        }

        serverSocket.close();
    }
}

//
//class RunnableThread implements Runnable{
//    private Thread t;
//    private String threadName;
//
//    RunnableThread(String name){
//        threadName = name;
//        System.out.println("Creating: "+threadName);
//    }
//
//    public void run(){
//        System.out.println("Running "+threadName);
//
//
//        //lookup for any file request
//
//
//
//
//    }
//
//    public void start(){
//        System.out.println("Starting: "+threadName);
//        if(t==null){
//            t = new Thread(this, threadName);
//            t.start();
//        }
//    }
//}