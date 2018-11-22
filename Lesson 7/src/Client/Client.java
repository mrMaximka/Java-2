package Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class Client {

    @FXML TextField msgField;
    @FXML TextArea chatArea;
    @FXML ListView<String> onlineList;  // Это должно было быть окно со списком пользователей онлайн
    @FXML BorderPane chatPane;
    @FXML HBox upperPanel;
    @FXML TextField loginField;
    @FXML PasswordField passwordField;
    @FXML TextArea errorMes;    // Окно вывода ошибок при авторизации
    @FXML Button authBtn;
    @FXML Button regBtn;        // Кнопка для регистрации на ок авторизации
    @FXML Button setNick;       // Кнопка подтверждения регистрации
    @FXML TextField nickField;  // Строка ввода ника
    @FXML HBox regPane;         // Панель регстрации
    @FXML TextArea errorMesReg; // Окно вывода ошибок во время регистрации
    @FXML TextField regField;   // Логин в окне регистрации
    @FXML PasswordField passRegField;   // Пароль в окне регистрации

    private boolean isAuthorized;

    private static final int MAX_ATTEMPT = 3;   // Кол-во попыток авторизации

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;


    final String IP_ADRESS = "localhost";
    final int PORT = 8183;

    private int attempt;    // Кол-во оставшихся попыток для авторизации

    Timer timer2 = new Timer(); // Таймер на бездействие

    public Client(){

        newTimer();     // В конструкторе запустили. Т.е. при открытии окна
    }

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);    // Окно авторизации
            upperPanel.setManaged(true);
            chatPane.setVisible(false);     // Окно чата
            chatPane.setManaged(false);
            regPane.setVisible(false);
            regPane.setManaged(false);

        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            chatPane.setVisible(true);
            chatPane.setManaged(true);
            regPane.setVisible(false);
            regPane.setManaged(false);
        }
    }

    public void setRegistration() {     // Выведение окна регистрации
        upperPanel.setVisible(false);
        upperPanel.setManaged(false);
        regPane.setVisible(true);
        regPane.setManaged(true);
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
                                timer2.cancel();        // Вырубили таймер, если клиент вошел
                                timer2.purge();
                                break;
                            }else if (str.startsWith("/regok")){    // Если зарегистрировался
                                setAuthorized(false);       // Открываем авторизацию
                                errorMes.appendText("Вы успешно зарегистрировались");
                                timer2.cancel();            // Вырубаем таймер
                                timer2.purge();
                                //newTimer();
                                break;
                            }
                            else if (str.equals("/authError")){   // При команде, которая прилетает при ошибке авторизации
                                authError();    // Ругаем пользователя
                            }
                            else if (str.equals("/regError")) regError();       // Ошибки при решистрации
                            else if (str.equals("/nickError")) nickError();
                            else {
                                chatArea.appendText(str + "\n");
                            }
                        }

                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/")) {
                                if (str.equals("/serverclosed")) break;
                                if (str.startsWith("/clientlist")) {
                                    String[] tokens = str.split(" ");
                                    Platform.runLater(() -> {
                                        onlineList.getItems().clear();
                                        for (int i = 1; i < tokens.length; i++) {
                                            onlineList.getItems().add(tokens[i]);
                                        }
                                    });
                                }
                            }else {
                                chatArea.appendText(str + "\n");
                            }
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
        connect();
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToReg() {        // Попытка регистрации
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/reg " + regField.getText() + " " + passRegField.getText() + " " + nickField.getText());
            regField.clear();
            passRegField.clear();
            nickField.clear();
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

    public void nickError(){    // Существует ник
        errorMesReg.appendText("Ошибка! Такой ник уже зарегистрирован");
        nickField.clear();
        passRegField.clear();
    }

    public void regError(){     // Существует логин
        errorMesReg.appendText("Ошибка! Такой логин уже зарегистрирован");
        regField.clear();
        passRegField.clear();
    }

    public void newTimer(){     // Создание таймера
        TimerTask task = new TimerTask() {
            public void run()
            {
                errorMes.appendText("Время на авторизацию вышло!");
                loginField.setVisible(false);       // Вырубаем поля авторизации
                passwordField.setVisible(false);
                authBtn.setVisible(false);
                regBtn.setVisible(false);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer2.schedule( task, 120000 );    // На 120 секунд
    }

}