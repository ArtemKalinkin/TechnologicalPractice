package edu.epidemicsimulation.practice;

public class Utils {

    // Метод для получения правильного склонения слова "день"
    public static String getDayAddition(int num) {
        int preLastDigit = num % 100 / 10;

        if (preLastDigit == 1) {
            return "дней";
        }

        return switch (num % 10) {
            case 1 -> "день";
            case 2, 3, 4 -> "дня";
            default -> "дней";
        };
    }

    public static String whoIs(Person person){
        return switch (person) {
            case Child child -> "Ребенок ";
            case Adult adult -> "Взрослый ";
            case Elderly elderly -> "Пожилой ";
            case null, default -> "Человек ";
        };
    }
}
