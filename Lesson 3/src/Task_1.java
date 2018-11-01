import java.util.*;

public class Task_1 {
    public static void main(String[] args) {
        String arr[] = {"world", "street", "door", "number", "pen", "way", "door", "table",
                "car", "street", "number", "chair", "way", "house", "door", "number",
                "world", "sun", "pen", "car", "house", "tree", "door", "pen",};         // Массив слов

        convertToList(arr);     // Передаем массив в метод
    }

    private static void convertToList(String[] arr) {
        HashMap<String, Integer> hm = new HashMap<>();      // Создаем хэш - таблицу
                                                            // Key - слово, value - кол-во повторений
        for (int i = 0; i < arr.length; i++) {              // Идем по массиву слов и заполняем HashMap
            hm.merge(arr[i], 1, (a, b) -> a += b);      // Если значения нет, то создаем и кол-во повторений 1
        }                                                     // Если есть, то прибавляем еще 1 повторение
        for (Map.Entry<String, Integer> o : hm.entrySet()) {    // Выводим список уникальных слов с количеством повторений
            System.out.print(o.getKey() + " [");
            System.out.println(o.getValue() + "]");
        }
    }
}
