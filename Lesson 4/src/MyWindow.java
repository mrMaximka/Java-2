import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {

    private JTextArea chatArea = new JTextArea();           // Создали поле для чата
    private JTextField messageField = new JTextField();     // Поле ввода текста

    public MyWindow() throws HeadlessException {
        setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 150, 400, 400);

        // Параметры переноса слов
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        chatArea.setEditable(false);        // Запрет на редактирование поля

        JScrollPane scrollChatArea = new JScrollPane(chatArea); // Добавили полю scroll-bar

        messageField.setToolTipText("Write message...");        // Подсказака для поля ввода
        messageField.addActionListener(new ActionListener() {   // Слушатель для Enter
            @Override
            public void actionPerformed(ActionEvent e) {
                SendMessage();                                  // Метод передачи сообщения
            }
        });

        JButton sendButton = new JButton("Send");           // Создали кнопку
        sendButton.addActionListener(new ActionListener() {     // Добавили слушатель
            @Override
            public void actionPerformed(ActionEvent e) {
                SendMessage();
            }
        });

        JPanel messagePanel = new JPanel();                     // Панель для строки ввода и кнопки
        messagePanel.setLayout(new BorderLayout());             // BorderLayout чтобы на весь экран было
        messagePanel.add(messageField, BorderLayout.CENTER);    // Добавили строку в панель
        messagePanel.add(sendButton, BorderLayout.EAST);        // Справа кнопку

        add(scrollChatArea, BorderLayout.CENTER);               // Засунули во фрейм поле чата
        add(messagePanel, BorderLayout.SOUTH);                  // И вниз панель с полем ввода и кнопкой
        setVisible(true);
    }

    private void SendMessage() {                                // Метод отправки сообщений
        chatArea.append(messageField.getText() + "\n");         // Добавили сообщение в чат
        messageField.setText("");                               // Обнулили поле ввода
        messageField.grabFocus();                               // Вернули фокус
    }
}
