public class Main {
    public static void main(String[] args) throws InterruptedException {

        Calculations calc = new Calculations();     // Создали объект

        calc.method_1();        // Вызов метода 1
        calc.method_2();        // Вызов метода 2 (2 потока)

    }
}
