import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        new MyClient();
    }
}

class MyClient{
    private final String SERVER_ADDR = "localhost"; // Указываем ip
    private final int SERVER_PORT = 8186;           // Указываем порт
    private Socket socket;          // Создаем сокет
    private DataInputStream in;     // Входной
    private DataOutputStream out;   // И выходной потоки

    public MyClient(){
        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);          // Даем ip и порт
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {     // Первый поток на передачу сообщений
                @Override
                public void run() {
                    try {
                        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); // Буферезируем
                        while (true) {
                            String str = keyboard.readLine();   // Ждем ввода в консоль
                            if (str.equals("/end")) break;      // При /end отлетаем от серва
                            out.writeUTF(str);                  // Передаем сообщение
                            out.flush();                        // Очистить поток
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();     // Запуск потока

            new Thread(new Runnable() {     // Второй потокна прием сообщений
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();          // Ждем сообщения
                            System.out.println("Server: " + str);   // Печать сообщения в консоль
                            out.flush();                        // Очистить поток
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();     // Запуск потока
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
