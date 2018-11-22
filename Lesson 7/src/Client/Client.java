package Client;

import Server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Client {

    @FXML TextField msgField;
    @FXML TextArea chatArea;
    @FXML TextArea onlineList;  // Это должно было быть окно со списком пользователей онлайн
    @FXML BorderPane chatPane;
    @FXML HBox upperPanel;
    @FXML TextField loginField;
    @FXML PasswordField passwordField;
    @FXML TextArea errorMes;    // Окно вывода ошибок при авторизации

    private boolean isAuthorized;

    private static final int MAX_ATTEMPT = 3;   // Кол-во попыток авторизации

    Server serv;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;


    final String IP_ADRESS = "localhost";
    final int PORT = 8183;

    private int attempt;    // Кол-во оставшихся попыток для авторизации

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);    // Окно авторизации
            upperPanel.setManaged(true);
            chatPane.setVisible(false);     // Окно чата
            chatPane.setManaged(false);

        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            chatPane.setVisible(true);
            chatPane.setManaged(true);
        }
    }

    void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            attempt = MAX_ATTEMPT;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            } else if (str.equals("/authError")){   // При команде, которая прилетает при ошибке авторизации
                                authError();    // Ругаем пользователя
                            }else {
                                chatArea.appendText(str + "\n");
                            }
                        }

                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/serverclosed")) {
                                break;
                            }
                            chatArea.appendText(str + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            out.writeUTF(msgField.getText());
            msgField.clear();
            msgField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth() {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authError(){        // Ругаемся
        attempt--;                  // Минус попытка, котороую он только что сделал
        if (attempt < 1){           // Если попытки закончились, отбираем что было
            errorMes.appendText("Превышен лимит попыток авторизации!");
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            return;
        }   // Если еще есть, то угрожаем
        errorMes.appendText("Ошибка! Неверный логин/пароль\nОсталось попыток: " + attempt + "\n");
    }

    public void reOnline(){         // Злополучный список онлайн, который мы вызывали с сервера каждому клиенту
        ArrayList<String> list;
        list = serv.getOnlineClients();     // Берем лист с никами из сервера
        onlineList.setText("В сети:\n");    // Обнуляем текст поля, устанавливая "заголовок"
        for (String name : list){
            onlineList.appendText(name + "\n"); //Запихиваем ники
        }
    }       // Все просто, но эта шайтан-машина выбрасывает исключение EOFException

}