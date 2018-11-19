import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket server = null;         // Создаем скеты
        Socket socket = null;

        try {
            server = new ServerSocket(8186);    // Привязываем к порту
            System.out.println("Сервер запущен");
            socket = server.accept();                 // Ждем подклбчения клиента
            System.out.println("Клиент подключен");
            DataInputStream in = new DataInputStream(socket.getInputStream());      // Создаем входной и выходной потоки сокета
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {     // Первый поток на прием сообщений
                @Override
                public void run() {
                    while (true){
                        String str = null;
                        try {
                            str = in.readUTF();         // Ждем сообщения
                            System.out.println(str);    // Печатаем его в консоль
                            out.flush();                // Очищаем поток
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();     // Запуск потока

            new Thread(new Runnable() {     // Второй поток на передачу сообщений
                @Override
                public void run() {
                    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); // Буферезируем
                    while (true){
                        String str = null;
                        try {
                            String prin = keyboard.readLine();  // Ждем ввода в консоль
                            out.writeUTF(prin);         // Передаем сообщение
                            out.flush();                // Очищаем поток
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();     // Запуск потока

        } catch (IOException e) {
            System.out.println("Ошибка инициализанции сервера");
            e.printStackTrace();
        }finally {
            try {
                server.close();     // Закрыли сервер
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
