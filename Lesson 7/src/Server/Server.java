package Server;

import Client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public void lichka(ClientHandler id, String nick, String msg) {     // Обработка личных сообщений
                                                                        // Передали отправителя, ник и сообщение
        boolean isFind = false;     // Флаг на нахождение пользователя с указанным ником
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)){  // Ищем пользователя с нужным ником
                if (o.checkBlackList(id.getNick())){            // Проверка на черный список
                    id.sendMsg("Вы не можете писать этому пользователю");
                    break;
                }
                o.sendMsg("from " + id.getNick() + ": " + msg);   // Если находим, то отправляем с указанием ника отправителя
                isFind = true;
                break;
            }
        }
        if (!isFind) id.sendMsg("Ошибка! В сети нет пользователя с таким ником");   // Если не нашли ник
    }


    public void broadcastMsg(ClientHandler from, String msg) {
        for (ClientHandler o : clients) {
            if (!o.checkBlackList(from.getNick()))          // Проверка на черный список
                o.sendMsg(msg);
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();

    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder();
        sb.append("/clientlist ");
        for (ClientHandler o : clients) {
            sb.append(o.getNick() + " ");
        }
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }
}
