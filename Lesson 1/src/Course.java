public class Course {

    int cross;      // Дистанция, которую нужно пробежать
    int water;      // Проплыть
    int jump;       // И прыгнуть

    public Course(int cross, int water, int jump) {     // Получили эти значения
        this.cross = cross;
        this.water = water;
        this.jump = jump;
    }

    public void doIt(Team team){        // Отправили команду на полосу
        team.run(cross);        // На бег
        team.jump(jump);        // Прыжки
        team.swim(water);       // И добили плаванием
    }
}
