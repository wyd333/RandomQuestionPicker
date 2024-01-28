import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomQuestionPicker {
    private static final String FILE_PATH = "questions.txt";
    private static final String SEPARATOR = ";";

    public static void main(String[] args) {
        Map<String, Integer> questionCounts = readQuestionCountsFromFile();

        if (questionCounts.isEmpty()) {
            System.out.println("题库为空！");
            return;
        }

        String selectedQuestion = pickRandomQuestion(questionCounts);
        System.out.println("随机抽取的题目是：\n" + selectedQuestion);

        incrementQuestionCount(questionCounts, selectedQuestion);
        writeQuestionCountsToFile(questionCounts);
    }

    private static Map<String, Integer> readQuestionCountsFromFile() {
        Map<String, Integer> questionCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 2) {
                    String question = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    questionCounts.put(question, count);
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件出错：" + e.getMessage());
        }
        return questionCounts;
    }

    private static String pickRandomQuestion(Map<String, Integer> questionCounts) {
        Random random = new Random();
        int randomNumber = random.nextInt(questionCounts.size()) + 1;
        int accumulatedWeight = 0;
        for (Map.Entry<String, Integer> entry : questionCounts.entrySet()) {
            accumulatedWeight += 1;
            if (randomNumber == accumulatedWeight) {
                return entry.getKey();
            }
        }
        return null; // Should never happen if the map is not empty
    }

    private static void incrementQuestionCount(Map<String, Integer> questionCounts, String question) {
        int count = questionCounts.getOrDefault(question, 0);
        questionCounts.put(question, count + 1);
    }

    private static void writeQuestionCountsToFile(Map<String, Integer> questionCounts) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Map.Entry<String, Integer> entry : questionCounts.entrySet()) {
                writer.write(entry.getKey() + SEPARATOR + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("写入文件出错：" + e.getMessage());
        }
    }
}
