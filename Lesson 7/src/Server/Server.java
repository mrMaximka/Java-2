package Server;

import Client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;

    Client client;

    public Server() {               // В конструкторе ничего нового
        AuthService.connect();
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(8183);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
            AuthService.disconnect();
        }
    }

    public void broadcastMsg(String msg) {  // Тут тоже
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public void lichka(ClientHandler id, String nick, String msg) {     // Обработка личных сообщений
                                                                        // Передали отправителя, ник и сообщение
        boolean isFind = false;     // Флаг на нахождение пользователя с указанным ником
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)){  // Ищем пользователя с нужным ником
                o.sendMsg(id.getNick() + ": " + msg);   // Если находим, то отправляем с указанием ника отправителя
                isFind = true;
                break;
            }
        }
        if (!isFind) id.sendMsg("Ошибка! В сети нет пользователя с таким ником");   // Если не нашли ник
    }


    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        //newOnline();              // Тут должен был формироваться список пользователей в сети
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        //newOnline();              // Тут тоже
    }

    public void newOnline(){        // Для всех пользователей делаем reOnline
        for (ClientHandler o : clients) {
            client.reOnline();
        }
    }

    public ArrayList<String> getOnlineClients(){
        ArrayList<String> list = new ArrayList<>();     // Создали лист
        for (ClientHandler o : clients) {               // Для каждого пользователя
            list.add(o.getNick());                      // Котороый заполнили никами
        }
        return list;                                    // И отправили
    }
}
