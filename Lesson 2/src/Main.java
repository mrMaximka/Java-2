public class Main {
    static final int N = 4;         // Размерность массива

    public static void main(String[] args) throws MyArraySizeException, MyArrayDataException {  // Обработаем исключения
        String arr[][] = {                          // Создаем массив 4x4
                {"123", "345", "567", "567"},
                {"234", "456", "567", "567"},
                {"123", "345", "876", "567"},
                {"543", "765", "987", "567"}
        };

        convertToInt(arr);                          // Передаем массив в метод
    }

    public static void convertToInt(String arr[][]) throws MyArraySizeException, MyArrayDataException {
        if (arr.length != N) throw new MyArraySizeException(N);         // Кол-во строк должно быть N
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].length != N) throw new MyArraySizeException(N);  // В каждой строке должно быть N столбцов, иначе бросаем исключение
        }                                                               // Чтобы с переменной длины не было

        int sum = 0;                    // Считаем сумму
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {   // Обходим массив
                try {
                    sum += Integer.parseInt(arr[i][j]); // Преобразовываем String в int и закидываем в sum
                }catch (NumberFormatException e){       // Ловим NumberFormatException - неверное преобразование String в int
                    throw new MyArrayDataException(arr[i][j], i, j);   // Бросаем наше исключение
                }
            }
        }
        System.out.println("Все элементы успешно переведены в int. Сумма = " + sum);    // Вывод результата
    }
}
