package ru.muctr;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 8189;
    private static String SERVER_ADDR = null;

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Client started");
        SERVER_ADDR  ="192.168.1.105";
        try(var socket = new Socket(InetAddress.getByName("192.168.1.104"), SERVER_PORT)){   //в конструктор клиентского сокета передается адрес сервера и порт
            System.out.println("Client connected");
            Scanner socket_in = new Scanner(socket.getInputStream()); //поток для чтения сообщений от сервера
            Scanner console_in = new Scanner(System.in);   // поток для чтения сообщений из консоли
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); //поток для отправки сообщений серверу

            new Thread(new Runnable() {   //создаем поток, получающий сообщения от сервера
                @Override
                public void run() {
                    try {
                        while(true){
                            String fromServer = socket_in.nextLine();  //записать сообщения от сервера в переменную fromServer
                            if (fromServer.equals("/end")){
                                System.out.println("Server disconnected");
                                break;
                            }
                            System.out.println("Server: " + fromServer);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true){                  // основной поток читает сообщения из консоли и отправляет их на сервер
                System.out.println("Введите сообщение для сервера");
                String s = console_in.nextLine();
                if (s.equals("/end")){
                    System.out.println("Client disconnected");
                    out.println(s);
                    break;
                }
                out.println(s);            //отправка сообщения на сервер
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
