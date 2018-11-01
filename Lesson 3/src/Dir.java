import java.util.*;

public class Dir {
    HashMap<String, String> hm = new HashMap<>();       // Создаем хэш - таблицу

    public Dir() {                          // Закинем в конструкторе несколько контактов
        hm.put("Ivanov", "+79456845792");
        hm.put("Morgunov", "+79454758376");
        hm.put("Sidorov", "+79454981264");
        hm.put("Petrov", "+79458673489");
    }

    public void add(String name, String number){        // Метод добавления контакта
        hm.merge(name, number, (a, b) -> a + "; " + b); // Если нет такого контакта, то созлаем нового
        System.out.println("Контакт успешно добавлен"); // Если есть, то добавим еще 1 номер через ";"
    }

    public void get(String name){
        System.out.println(name + ": " + hm.get(name)); // Метод получения номера по фамилии
    }

    public void dirInfo(){                              // Вывод инфы о всех контактах
        System.out.println("Информация о контактах:");
        for (Map.Entry<String, String> o : hm.entrySet()) {
            System.out.print(o.getKey() + ": ");
            System.out.println(o.getValue());
        }
    }
}
