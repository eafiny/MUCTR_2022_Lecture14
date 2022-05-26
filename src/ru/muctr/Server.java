package ru.muctr;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 8189;
    private static ServerSocket server;
    private static Socket socket;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(PORT,100, InetAddress.getByName("192.168.1.104")); //в конструктор серверного сокета передаем порт, который он будет слушать
            System.out.println("Server started");
            socket = server.accept();  //ожидание подключения клиента
            System.out.println("Client connected");

            Scanner scanner = new Scanner(socket.getInputStream());  //входящий поток для получения сообщений от клиента
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //исходящий поток для отправки сообщений клиенту

            while (true) {             // бесконечно ожидаем сообщения от клиента
                String str = scanner.nextLine();  //в перемнную str записываем сообщение от клиента
                if (str.equals("/end")) {
                    System.out.println("Client disconnected");
                    break;
                }
                System.out.println("Client: " + str);
                out.println("ECHO: " + str);  //эхо-сервер возвращает клиенту его сообщение
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
