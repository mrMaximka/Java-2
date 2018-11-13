public class Calculations {

    public void method_1(){
        final int size = 10000000;
        float[] arr = new float[size];          // Создание массива

        for (int i = 0; i < arr.length; i++) {  // Заполнили
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();    // Записали время

        mathCalc(arr, size);    // Выполняем кучу ненужных операция над каждым элементом

        System.out.println("Method_1: " + (System.currentTimeMillis() - a));    // Вывели время вычислений (разница нового и сторого)

    }

    public void method_2() throws InterruptedException {
        final int size = 10000000;
        final int h = size / 2;
        float[] arr = new float[size];      // Создали массив
        float[] a1 = new float[h];      // + 2 массива для склейки в 2 раза меньше
        float[] a2 = new float[h];

        for (int i = 0; i < arr.length; i++) {      // Заполнили
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();        // Запомнили время

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);     // Разбили массив на 2


        Thread t1 = new Thread(new Runnable() {     // Создали новый поток
            @Override
            public void run() {
                mathCalc(a1, h);        // Выполняем кучу ненужных операция над каждым элементом
            }
        });
        Thread t2 = new Thread(new Runnable() {     // Создали еще поток с таким же телом
            @Override
            public void run() {
                mathCalc(a2, h);        // Выполняем кучу ненужных операция над каждым элементом
            }
        });

        t1.start();     // Запустили первый поток
        t2.start();     // И второй

        t1.join();      // Ждем завершения работы первого потока
        t2.join();      // И второго

        System.arraycopy(a1, 0, arr, 0, h);     // Склеиваем массивы
        System.arraycopy(a2, 0, arr, h, h);

        System.out.println("Method_2: " + (System.currentTimeMillis() - a));    // Вывод времени работы

    }

    private void mathCalc(float[] a, int h) {
        for (int i = 0; i < h; i++) {
            a[i] = (float)(a[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }
}
