public class Task_2 {
    public static void main(String[] args) {
        Dir dir = new Dir();        // Создаем телефонную книгу

        dir.get("Ivanov");          // Просим телефон абонента Ivanov
        dir.add("Ivanov", "+79458437643");  // Кидаем еще  номер на Ivanov
        dir.add("Rogov", "+7945876475");    // Создаем нового абонента Rogov
        dir.dirInfo();      // Вывод информации о контактах (Ivanov с 2мя номерами)
    }
}
