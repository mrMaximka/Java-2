public class Team implements Competitor {

    public static final int PLACES = 4;         // Кол-во участников в команде

    String name;                                // Название команды

    int[] maxRunDistance = new int[PLACES];     // Дистанция которую может пробежать каждый участник команды
    int[] maxJumpHeight = new int[PLACES];      // Высота прыжка
    int[] maxSwimDistance = new int[PLACES];    // И дистанция для правания

    boolean[] active = new boolean[PLACES];     // Прошел ли участник всю полосу

    public Team(String name) {
        this.name = name;                       // Задали название тимы
        for (int i = 0; i < PLACES; i++) {
            this.maxRunDistance[i] = 1000 + (int)(Math.random() * 3000);    // Задали дистанцию от 1000 до 4000 для бега
        }                                                                 // Через foreach что-то не зашло
        for (int i = 0; i < PLACES; i++) {
            this.maxJumpHeight[i] = 10 + (int)(Math.random() * 20);         // Так же для прыжка
        }
        for (int i = 0; i < PLACES; i++) {
            this.maxSwimDistance[i] = 100 + (int)(Math.random() * 200);     // И плавания
        }
        for (int i = 0; i < PLACES; i++) {                                  // "обнулили"
            this.active[i] = true;
        }
    }


    @Override
    public void run(int dist) {
        System.out.println("Забег на дистанцию: " + dist + ". Команда " + name);
        for (int i = 0; i < PLACES; i++) {
            if (dist <= maxRunDistance[i]){                                         // Смог пройти
                System.out.println("Участник " + (i + 1) + " успешно прошел бег");
            }
            else {                                                                  // Не смог
                System.err.println("Участник " + (i + 1) + " провалил бег");
                active[i] = false;                                              // Соответсвенно проиграл
            }
        }
    }


    @Override
    public void swim(int dist) {        // Все аналогично
        System.out.println("Заплыв на дистанцию: " + dist + ". Команда " + name);
        for (int i = 0; i < PLACES; i++) {
            if (dist <= maxSwimDistance[i]){
                System.out.println("Участник " + (i + 1) + " успешно проплыл");
            }
            else {
                System.err.println("Участник " + (i + 1) + " провалил плавание");
                active[i] = false;
            }
        }
    }

    @Override
    public void jump(int height) {          // И на прыжки
        System.out.println("Прыжки на высоту: " + height + ". Команда " + name);
        for (int i = 0; i < PLACES; i++) {
            if (height <= maxJumpHeight[i]){
                System.out.println("Участник " + (i + 1) + " успешно прыгнул");
            }
            else {
                System.err.println("Участник " + (i + 1) + " провалил прыжки");
                active[i] = false;
            }
        }
    }

    @Override
    public void showResults(){          // Вывод результатов команды
        System.out.println("\nРезультаты соревнований команды " + name + ":");
        for (int i = 0; i < PLACES; i++) {
            System.out.print("Участник " + (i + 1));
            if (active[i] == true) System.out.println(" прошел");
            else System.err.println(" провалил");
        }
    }

    @Override
    public void info() {                   // И инфа о команде
        System.out.println("Информация о команде " + this.name);
        for (int i = 0; i < PLACES; i++) {
            System.out.println("Участник " + (i+1));
            System.out.println("\tМожет пробежать: " + this.maxRunDistance[i]);
            System.out.println("\tМожет прыгнуть: " + this.maxJumpHeight[i]);
            System.out.println("\tМожет проплыть: " + this.maxSwimDistance[i]);
        }
    }
}
