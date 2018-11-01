public class MyArraySizeException extends Exception {
    int n;      // Необходимая размерность массива

    public MyArraySizeException(int n) {
        this.n = n;
    }

    public String toString() {
        return "Массив должен иметь размерность " + n + "x" + n + "!";  // Сообщение
    }
}
