package com.example.nisttestapp;

import com.example.nisttestapp.model.TestBlock;
import com.example.nisttestapp.tests.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.example.nisttestapp.Service.*;

public class Controller {

    @FXML
    private CheckBox frequencyTestCheckBox;

    @FXML
    private CheckBox frequencyTestWithBlockCheckBox;

    @FXML
    private CheckBox runTestCheckBox;

    @FXML
    private CheckBox longestOneBlockTestCheckBox;

    @FXML
    private CheckBox binaryMatrixRankTestCheckBox;

    @FXML
    private CheckBox nonOverlappingTestCheckBox;

    @FXML
    private CheckBox statisticalTestCheckBox;

    @FXML
    private CheckBox serialTestCheckBox;

    @FXML
    private CheckBox approximateEntropyTestCheckBox;

    @FXML
    private CheckBox cumulativeSumsForwardTestCheckBox;

    @FXML
    private CheckBox cumulativeSumsBackwardTestCheckBox;

    @FXML
    private Label frequencyTestPValueLabel;

    @FXML
    private Label frequencyTestWithBlockPValueLabel;

    @FXML
    private Label runTestPValueLabel;

    @FXML
    private Label longestOneBlockTestPValueLabel;

    @FXML
    private Label binaryMatrixRankTestPValueLabel;

    @FXML
    private Label nonOverlappingTestPValueLabel;

    @FXML
    private Label statisticalTestPValueLabel;

    @FXML
    private Label serialTestPValue1Label;

    @FXML
    private Label serialTestPValue2Label;

    @FXML
    private Label approximateEntropyTestPValueLabel;

    @FXML
    private Label cumulativeSumsForwardTestPValueLabel;

    @FXML
    private Label cumulativeSumsBackwardTestPValueLabel;

    @FXML
    private Label frequencyTestResultLabel;

    @FXML
    private Label frequencyTestWithBlockResultLabel;

    @FXML
    private Label runTestResultLabel;

    @FXML
    private Label longestOneBlockTestResultLabel;

    @FXML
    private Label binaryMatrixRankTestResultLabel;

    @FXML
    private Label nonOverlappingTestResultLabel;

    @FXML
    private Label statisticalTestResultLabel;

    @FXML
    private Label serialTestResultLabel;

    @FXML
    private Label approximateEntropyTestResultLabel;

    @FXML
    private Label cumulativeSumsForwardTestResultLabel;

    @FXML
    private Label cumulativeSumsBackwardTestResultLabel;

    @FXML
    private Label welcomeText;

    @FXML
    private Label filenameLabel;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        welcomeText.setText("Welcome to JavaFX Application!");
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/test.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void onTestButtonClick() {
        List<TestBlock> testBlocks = null;
        try {
            testBlocks = initTestBlocks();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(file)) {
            JOptionPane.showMessageDialog(null,
                    new String[] {"Файл таңдалмады"},
                    "Қате", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean testsIsCheck = false;

        for (TestBlock block: testBlocks) {
            if (block.getCheckBox().isSelected()) {
                testsIsCheck = true;
                Map<String, Object> map = null;
                try {
                    map = block.getTest().test(Service.getBits(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                TODO пока что кейін exception құрып өзгертіп шығу қажет
                if (!Objects.isNull(map)) {
                    double pValue = (double) map.get("pValue");

                    block.getpValueLabel().setText("" + pValue);

                    if (pValue >= 0.01) {
                        block.getResultLabel().setText("" + true);
                    } else {
                        block.getResultLabel().setText("" + false);
                    }

                    if (map.containsKey("pValue2")) {
                        double pValue2 = (double)(map.get("pValue2"));
                        serialTestPValue2Label.setText(String.valueOf(pValue2));

                        if (pValue2 >= 0.01) {
                            block.getResultLabel().setText("" + true);
                        }
                    }
                }
            }
        }

        if (!testsIsCheck) {
            JOptionPane.showMessageDialog(null,
                    new String[] {"Тест таңдалмады"},
                    "Қате", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Тест аяқталды", "Тест",
                    JOptionPane.INFORMATION_MESSAGE, null);
        }
    }

    private List<TestBlock> initTestBlocks() throws NoSuchMethodException {
        return List.of(
                new TestBlock("1. Частотный побитовый тест", frequencyTestCheckBox, frequencyTestPValueLabel, frequencyTestResultLabel, new FrequencyTest()),
                new TestBlock("2. Частотный блочный тест", frequencyTestWithBlockCheckBox, frequencyTestWithBlockPValueLabel, frequencyTestWithBlockResultLabel, new FrequencyTestWithinBlock()),
                new TestBlock("3. Тест на последовательность одинаковых битов", runTestCheckBox, runTestPValueLabel, runTestResultLabel, new RunTest()),
                new TestBlock("4. Тест на самую длинную последовательность единиц в блоке", longestOneBlockTestCheckBox, longestOneBlockTestPValueLabel, longestOneBlockTestResultLabel, new LongestOneBlockTest()),
                new TestBlock("5. Тест рангов бинарных матриц", binaryMatrixRankTestCheckBox, binaryMatrixRankTestPValueLabel, binaryMatrixRankTestResultLabel, new BinaryMatrixRankTest()),
                new TestBlock("6. Тест на совпадение неперекрывающихся шаблонов", nonOverlappingTestCheckBox, nonOverlappingTestPValueLabel, nonOverlappingTestResultLabel, new NonOverlappingTest()),
                new TestBlock("7. Универсальный статистический тест Маурера", statisticalTestCheckBox, statisticalTestPValueLabel, statisticalTestResultLabel, new StatisticalTest()),
                new TestBlock("8. Тест на периодичность", serialTestCheckBox, serialTestPValue1Label, serialTestResultLabel, new SerialTest()),
                new TestBlock("9. Тест приблизительной энтропии", approximateEntropyTestCheckBox, approximateEntropyTestPValueLabel, approximateEntropyTestResultLabel, new ApproximateEntropyTest()),
                new TestBlock("10. Тест кумулятивных сумм (Forward)", cumulativeSumsForwardTestCheckBox, cumulativeSumsForwardTestPValueLabel, cumulativeSumsForwardTestResultLabel, new CumulativeSumsTest(0)),
                new TestBlock("11. Тест кумулятивных сумм (Backward)", cumulativeSumsBackwardTestCheckBox, cumulativeSumsBackwardTestPValueLabel, cumulativeSumsBackwardTestResultLabel, new CumulativeSumsTest(1))
        );
    }

    @FXML
    protected void onChooseFileClick() {
        String filename = chooseFile();
        filenameLabel.setText(filename);
    }

    @FXML
    protected void selectAllTestsButtonClick() throws NoSuchMethodException {
        List<TestBlock> blocks = initTestBlocks();
        for (TestBlock block: blocks) {
            block.getCheckBox().setSelected(true);
        }
    }

    @FXML
    protected void deSelectAllTestsButtonClick() throws NoSuchMethodException {
        List<TestBlock> blocks = initTestBlocks();
        for (TestBlock block: blocks) {
            block.getCheckBox().setSelected(false);
            block.getpValueLabel().setText("");
            block.getResultLabel().setText("");
        }
        file = null;
        setBits("");
        filenameLabel.setText("Қажетті файл таңдаңыз");
        serialTestPValue2Label.setText("");
    }

    @FXML
    protected void aboutProgram() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Бағдарлама туралы");
        alert.setHeaderText("Бағдарлама туралы ақпарат");
        alert.setContentText("""
                Әл-Фараби атындағы Қазақ ұлттық университеті\n
                Криптографиялық генераторлардың тиімділігін зерттеу және бағалауға арналған бағдарлама\n 
                Арнайы дипломдық жоба үшін әзірленген бағдарламалық қосымша\n 
                Автор: Бекбатыр Алихан \n
                2023 жыл \n
                """);
        alert.showAndWait();
    }

    @FXML
    protected void saveResultToFile() throws NoSuchMethodException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Файл сақтайтын орынды таңдаңыз");

        File file = chooser.showDialog(null);

        if (Objects.isNull(file)){
            JOptionPane.showMessageDialog(null,
                    new String[] {"Нәтижелерді сақтайтын орын таңдалынбады"},
                    "Қате", JOptionPane.ERROR_MESSAGE);
        } else {
            String filename = JOptionPane.showInputDialog(null, "<html><h2>Файл атауын енгізіңіз");
            List<TestBlock> testBlocks = initTestBlocks();
            try (FileWriter fileWriter = new FileWriter(file + "/" + filename + ".txt", true)) {
                boolean isSelected = false;
                for (TestBlock block:testBlocks) {
                    if (block.getCheckBox().isSelected()) {
                        fileWriter.write(block.getName());
                        fileWriter.write(" : ");
                        fileWriter.write(block.getResultLabel().getText());
                        fileWriter.write("  P-value: ");
                        fileWriter.write(block.getpValueLabel().getText());
                        if (block.getName().equals("8. Тест на периодичность")) {
                            fileWriter.write("  P-value2: ");
                            fileWriter.write(serialTestPValue2Label.getText());
                        }
                        fileWriter.append("\n");
                        isSelected = true;
                    }
                }
                if (isSelected) {
                    fileWriter.append("\n");
                    fileWriter.append("Тестіленген деректер: ");
                    fileWriter.write(getBits());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,
                    "<html><h2>Файл сәтті сақталды</h2><i>" + file + "/" + filename + ".txt</i>");
        }
    }

    @FXML
    protected void testingFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Тест файлдары орналасқан директорияны таңдаңыз");

        File dir = chooser.showDialog(null);

        if (dir != null) {
            testFiles(dir);
            JOptionPane.showMessageDialog(null, "<html><h2>Тест аяқталды</h2>");
        }

    }

    @FXML
    protected void showLoginPage() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
                System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
    }

    @FXML
    protected void aboutTest1() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/frequencyTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest2() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/frequencyTestWithBlock.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest3() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/runTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest4() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/longestOneBlock.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest5() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/binaryMatrixRankTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest6() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/nonOverlappingTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest7() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/statisticalTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest8() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/serialTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest9() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/approximateEntropyTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }

    @FXML
    protected void aboutTest10() throws IOException {
        File htmlFile = new File("src/main/resources/com/example/nisttestapp/templates/cumulativeSumsTest.html");
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}
