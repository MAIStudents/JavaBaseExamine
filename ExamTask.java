package exam2;
import java.util.*;
import java.util.stream.*;

/* 
Реализуйте программу на Java, в которой необходимо сформировать коллекцию с помощью Stream API на основе входных данных. На вход подается строка с любым набором символов. Необходимо с помощью Stream API получить из этой строки коллекцию с типом String, элементами которой являются сумма всех четных чисел из этой строки(в виде строки), конкатенация всех символов нижнего регистра, и отдельно стоящие символы не буквы и не цифры. Пример: На вход подается строка "aV_1,,.-fg.D42!r" на выходе коллекция ["6","afgr","_", ",",",",".","-",".","!"].
*/

public class ExamTask {
/*     public static void main(String[] args) {
        List<List<Integer>> listOfLists = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5),
            Arrays.asList(6, 7, 8)
        );
        
        // Используем flatMap для получения одного потока чисел
        List<Integer> flattenedList = listOfLists.stream()
            .flatMap(Collection::stream)  // Преобразуем каждый список в поток
            .collect(Collectors.toList());  // Собираем в новый список
        
        System.out.println(flattenedList); // [1, 2, 3, 4, 5, 6, 7, 8]
    } */
    public static void main(String[] args) {
        String input = "aV_1,,.-fg.D42!r";
        
        // Используем flatMap, чтобы превратить список строк в поток символов
        // List<Character> characters = words.stream()
        //     .flatMap(word -> word.chars()  // Преобразуем каждое слово в IntStream символов
        //         .mapToObj(c -> (char) c))  // Преобразуем каждый символ в Character
        //     .collect(Collectors.toList());
        // List<String> answ = 
            input.chars()
            .forEach(c -> System.out.print((char) c));
        
        // System.out.println(characters); // [a, p, p, l, e, b, a, n, a, n, a, c, h, e, r, r, y]
    }
}