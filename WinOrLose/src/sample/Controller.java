package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button startButton;
    public Button restartButton;
    public Label balance;
    public Label bet;
    public Label betStatus;
    public Label number;
    public Label numberStatus;
    public Label result;
    public TextArea statistic;
    public TextField betField;
    public TextField numberField;
    public static int total = 20000;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        betField.setPromptText("0-"+total);
        numberField.setPromptText("0-5");
        result.setText("-");
    }

    public void handleButtonClick() {
        try {
            if (!isNumeric(betField.getText())) {
                betStatus.setTextFill(Color.ORANGERED);
                betStatus.setText("Make bet 0-" + total);
                betField.setPromptText("0-" + total);
            } else {
                betStatus.setText("");
            }

            if (!isNumeric(numberField.getText()) || Integer.valueOf(numberField.getText()) > 5) {
                numberStatus.setTextFill(Color.ORANGERED);
                numberStatus.setText("Choose number 0-5");
            } else {
                numberStatus.setText("");
            }

            if (isNumeric(betField.getText()) && isNumeric(numberField.getText()) && Integer.valueOf(numberField.getText()) < 6) {
                Random rand = new Random();
                int betFieldInt = Integer.valueOf(betField.getText());
                int numberFieldInt = Integer.valueOf(numberField.getText());
                int winNumber = rand.nextInt(6);

                if (betFieldInt > total) {
                    betFieldInt = total;
                    betField.setText(String.valueOf(betFieldInt));
                }

                total -= betFieldInt;
                result.setText(String.valueOf(winNumber));

                if (winNumber == numberFieldInt) {
                    total += betFieldInt * 6;
                    statistic.setText("You win " + (betFieldInt * 6) + "$");
                } else {
                    statistic.setText("You lose");
                }

                if (total == 0) {
                    statistic.setText("Game over");
                    startButton.setDisable(true);
                    restartButton.setVisible(true);
                }

                if (total > 100000000) {
                    statistic.setText("Congratulate, you win!");
                    startButton.setDisable(true);
                    restartButton.setVisible(true);
                }
                balance.setText(String.valueOf(total));
                bet.setText(betField.getText());
                number.setText(numberField.getText());
            } else {
                result.setText("-");
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex);
            result.setText("-");
            betField.setText("");
            numberField.setText("");
            bet.setText("-");
            number.setText("-");
            statistic.setText("What are you doing?!");
        }
    }

    public void handleRestartButtonClick() {
        result.setText("-");
        startButton.setDisable(false);
        restartButton.setVisible(false);
        total = 20000;
        balance.setText(String.valueOf(total));
        bet.setText("-");
        number.setText("-");
        statistic.setText("");
    }

    private static boolean isNumeric(String str) {
        return str.matches("\\d+?");
    }
}
