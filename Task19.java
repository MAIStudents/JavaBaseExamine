import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Task19 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        String data = scanner.nextLine();
        String data = "aV_1,,.-fg.D42!r";
        System.out.println("Your string: " + data);
        String sumEven = String.valueOf(Arrays.stream(data.split("")).filter((c) -> Character.isDigit(c.charAt(0))).mapToInt(Integer::parseInt).filter((i) -> i % 2 == 0).sum());
        String lowerChars = data.chars().filter((c) -> c >= 97 && c <= 122).mapToObj((c) -> Character.toString((char) c)).collect(Collectors.joining());
        List<String> otherSymbols = data.chars().filter((c) -> !Character.isLetterOrDigit(c)).mapToObj((c) -> Character.toString((char) c)).collect(Collectors.toList());
        List<String> answer = new ArrayList<>();
        answer.add(sumEven);
        answer.add(lowerChars);
        answer.addAll(otherSymbols);
        System.out.println("Answer is: " + answer);

    }
}
