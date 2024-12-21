package exam2;
import java.util.*;

/* 
Реализуйте программу на Java, в которой необходимо сформировать коллекцию с помощью Stream API на основе входных данных. На вход подается строка с любым набором символов. Необходимо с помощью Stream API получить из этой строки коллекцию с типом String, элементами которой являются сумма всех четных чисел из этой строки(в виде строки), конкатенация всех символов нижнего регистра, и отдельно стоящие символы не буквы и не цифры. Пример: На вход подается строка "aV_1,,.-fg.D42!r" на выходе коллекция ["6","afgr","_", ",",",",".","-",".","!"].
*/

public class ExamTask {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Введите строку: ");
        // String input = "aV_1,,.-fg.D42!r";
        String input = scanner.nextLine();
        
        List<String> answ = new ArrayList<>();
        answ.add("0");
        answ.add("");

        input.chars()
            .mapToObj(c -> (char) c)
            .forEach(c -> {
                if (Character.isDigit(c)) {
                    int a = Character.getNumericValue(c);
                    if (a % 2 == 0) {
                        int b = Integer.parseInt(answ.getFirst());
                        answ.set(0, String.valueOf(a + b));
                    }
                } else if (Character.isLowerCase(c)) {
                    answ.set(1, answ.get(1) + c);
                } else if (!Character.isUpperCase(c)) {
                    answ.add(String.valueOf(c));
                }
            });
        
        System.out.println(answ);
        scanner.close();
    }
}
