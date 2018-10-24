public class Main {
    public static void main(String[] args) {
        Course course = new Course(3000, 150, 15);  // Создали препятствия с нужной дистанцией\высотой
        Team team = new Team("Gnom");                           // Создали команду
        team.info();                               // Информация об участниках
        course.doIt(team);                         // Отправили команду препятствий
        team.showResults();                        // Результаты
    }
}
