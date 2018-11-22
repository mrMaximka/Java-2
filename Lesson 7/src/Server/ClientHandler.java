package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHandler {

    private DataOutputStream out;
    private DataInputStream in;
    private Socket socket;
    private Server server;
    private String nick;
    private List<String> blackList;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.blackList = new ArrayList<>();
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNickname(tokens[1], tokens[2]);
                                if (newNick != null) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    server.subscribe(ClientHandler.this);
                                    loadBlackList(nick);    // Загружаем черный список из БД
                                    break;
                                } else {
                                    sendMsg("/authError");  // В случае ошибка авторизации отправляем эту комнду
                                }
                            }else if (str.startsWith("/reg")){  // Поступление команды на решистрацию
                                String[] tokens = str.split(" ");
                                String regNick = AuthService.checkNickname(tokens[1]);  // Запрос ника с логина
                                String getLogin = AuthService.getLogin(tokens[3]);      // Запрос логина с ника
                                if (regNick == null){       // Если ник есть т.е логин уже зарегистрирован, то ошибка
                                    if (getLogin == null){  // Если логин есть т.е ник уже зарегистрирован, то ошибка
                                        AuthService.regNewName(tokens[1], tokens[2] ,tokens[3]);    // Записть в БД
                                        sendMsg("/regok ");
                                    }else {
                                        sendMsg("/nickError");
                                    }
                                }else{
                                    sendMsg("/regError");
                                }
                            }
                        }
                        while (true) {
                            String str = in.readUTF();
                            //служебные сообщения
                            if (str.startsWith("/")) {
                                if (str.equals("/end")) {
                                    out.writeUTF("/serverclosed");
                                    break;
                                }
                                if (str.startsWith("/w ")) {
                                    String[] tokens = str.split(" ", 3);
                                    server.lichka(ClientHandler.this, tokens[1], tokens[2]);
                                }
                                if (str.startsWith("/blacklist ")) {
                                    String[] tokens = str.split(" ");
                                    blackList.add(tokens[1]);
                                    sendMsg("Вы добавили пользователя с ником " + tokens[1] + " в черный список!");
                                }
                                if (str.startsWith("/unblacklist ")) {      // Выкинуть из черного списка
                                    String[] tokens = str.split(" ");
                                    blackList.remove(tokens[1]);
                                    sendMsg("Вы убрали пользователя с ником " + tokens[1] + " из черного списк!");
                                }
                            } else {
                                server.broadcastMsg(ClientHandler.this, nick + ": " + str);
                            }
                            System.out.println("Client " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        saveBlackList(nick);        // Сохраняем черный список в БЛ
                        server.unsubscribe(ClientHandler.this);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBlackList(String nick) {       // Сохраняем черный список
        StringBuilder str = new StringBuilder();
        if (blackList.isEmpty()) return;
        for (String params : blackList){
            str.append(params + ";");       // Закидываем строку с никами через ';'
        }
        AuthService.saveBlackList(nick, str.toString());    // Запрос в БД на загрузку
    }

    private void loadBlackList(String name) {       // Загрузка черного списка
        String str = AuthService.getBlackList(name);
        String[] tokens = str.split(";");
        blackList.addAll(Arrays.asList(tokens));    // Рабиваем строку по ';' и закидываем в лист
    }

    public String getNick() {   // Гетер на ник. Используем в Server.lichka и getOnlineClients
        return nick;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }
}
