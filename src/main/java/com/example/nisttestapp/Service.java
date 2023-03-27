package com.example.nisttestapp;

import com.example.nisttestapp.model.Test;
import com.example.nisttestapp.model.TestBlock;
import com.example.nisttestapp.tests.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Service {

    public static File file;
//    private static char[] buffer;
    private static String bits;

    public static String chooseFile() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);

        if (!Objects.isNull(file)) {
            try {
                FileReader reader = new FileReader(file);
                StringBuilder contentBuilder = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    contentBuilder.append((char) character);
                }
                reader.close();
                bits = contentBuilder.toString();
//                buffer = fileContent.toCharArray();
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
            return file.getName();
        }
        return "Файл таңдалмады";
    }

    public static String getBits() {
        return bits;
    }

    public static void setBits(String str) {
        bits = str;
    }

    public static void testFiles(File dir){
        File[] files = dir.listFiles();
        if (files != null) {
            File catalog = new File(dir + "/results");
            catalog.mkdir();
            Arrays.stream(files)
                    .filter(File::isFile)
                    .forEach(file -> testFile(file, catalog));
        }
    }

    private static void testFile(File file, File dir) {
        try(FileReader reader = new FileReader(file);
            FileWriter writer = new FileWriter( dir + "/" + file.getName(), true)) {

            char[] bits = new char[(int) file.length()];
            int result = reader.read(bits);
            String fileContent = new String(bits);

            List<Test> tests = initTests();
            if (tests != null) {
                tests.forEach(test -> {
                    Map<String, Object> map = test.test(fileContent, false);
                    double pValue = (double) map.get("pValue");
                    boolean resultTest = pValue >= 0.01;

                    try {
                        writer.write(String.valueOf(map.get("testName")));
                        writer.write(" : ");
                        writer.write(String.valueOf(resultTest));
                        writer.write("  P-value: ");
                        writer.write(String.valueOf(pValue));
                        if (map.get("testName").equals("8. Тест на периодичность")) {
                            writer.write("  P-value2: ");
                            writer.write(String.valueOf(map.get("pValue2")));
                        }
                        writer.append("\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                writer.append("\n");
                writer.append("Тестіленген деректер: ");
                writer.write(fileContent);
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static List<Test> initTests() {
        return List.of(
                new FrequencyTest(),
                new FrequencyTestWithinBlock(),
                new RunTest(),
                new LongestOneBlockTest(),
                new BinaryMatrixRankTest(),
                new NonOverlappingTest(),
                new StatisticalTest(),
                new SerialTest(),
                new ApproximateEntropyTest(),
                new CumulativeSumsTest(0),
                new CumulativeSumsTest(1)
        );
    }

}
