public class MyArrayDataException extends NumberFormatException{

    String arr;     // Значение в массиве
    int i, j;       // Идексы массива

    public MyArrayDataException(String arr, int i, int j) {
        this.arr = arr;
        this.i = i;
        this.j = j;
    }

    public String toString() {
        return "Элемент массива arr[" + i + "][" + j + "] содержит символ! (" + arr + ")";  // Сообщение
    }
}
