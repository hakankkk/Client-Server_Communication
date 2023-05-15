import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

public class TLSServer {
    private static final int PORT = 8443;
    private static final String KEYSTORE_LOCATION = "path/to/keystore.jks";
    private static final String KEYSTORE_PASSWORD = "keystore_password";
    private static final String KEY_PASSWORD = "key_password";
    private static final String TLS_PROTOCOL = "TLSv1.1";
    
    public static void main(String[] args) {
        try {
            // Set up the SSLContext with the necessary protocols and key material
            SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            char[] keystorePasswordArray = KEYSTORE_PASSWORD.toCharArray();
            char[] keyPasswordArray = KEY_PASSWORD.toCharArray();
            keyStore.load(new FileInputStream(KEYSTORE_LOCATION), keystorePasswordArray);
            keyManagerFactory.init(keyStore, keyPasswordArray);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Create the SSLServerSocket using the SSLContext
            ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            ServerSocket serverSocket = serverSocketFactory.createServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            // Wait for incoming connections from clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                // Set up the input/output streams for the client connection
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Read input from the client
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received message from client: " + inputLine);
                    out.println("Server received message: " + inputLine);
                }

                // Clean up
                out.close();
                in.close();
                clientSocket.close();
                System.out.println("Client disconnected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
