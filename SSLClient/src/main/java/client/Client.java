package client;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.Security;
import java.util.Scanner;

public class Client {
    static final int port = 8000;
    public static void main(String args[])
    {
        //The Port number through which the server will accept this clients connection
        int serverPort = 35786;
        //The Server Address
        String serverName = "localhost";
        /*Adding the JSSE (Java Secure Socket Extension) provider which provides SSL and TLS protocols
        and includes functionality for data encryption, server authentication, message integrity,
        and optional client authentication.*/
        Security.addProvider(new BouncyCastleProvider());
        //specifing the trustStore file which contains the certificate & public of the server
        System.setProperty("javax.net.ssl.keyStore","/home/apex/keyStore/client/clientKeyStore.jks");
        //specifing the password of the keystore file
        System.setProperty("javax.net.ssl.keyStorePassword","SvbDub");

        System.setProperty("javax.net.ssl.trustStore","/home/apex/keyStore/client/clientTrustStore.jts");
        //specifing the password of the trustStore file
        System.setProperty("javax.net.ssl.trustStorePassword","SvbDub1");
        //This optional and it is just to show the dump of the details of the handshake process
        System.setProperty("javax.net.debug","all");
        try
        {
            //SSLSSocketFactory establishes the ssl context and and creates SSLSocket
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            //Create SSLSocket using SSLServerFactory already established ssl context and connect to server
            SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(serverName,serverPort);
            //Create OutputStream to send message to server
            DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
            //Create InputStream to read messages send by the server
            DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
            //read the first message send by the server after being connected
            System.out.println(inputStream.readUTF());
            //Keep sending sending the server the message entered by the client unless the it is "close"
            while (true)
            {
                System.out.println("Write a Message : ");
                Thread.sleep(5000L);
                String messageToSend = "i am svb!";
                outputStream.writeUTF(messageToSend);
                System.err.println(inputStream.readUTF());
                if(messageToSend.equals("close"))
                {
                    break;
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Error Happened : "+ex.toString());
        }
    }
}
