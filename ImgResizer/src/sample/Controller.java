package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    public Button browseButton;
    public Button saveButton;
    public Label resultMessage;
    public TextField newWidth;
    public TextField newHeight;
    public ImageView imageView;

    private File file;
    private Graphics2D g2d;
    private Image image;
    private String formatName;
    private String chooserPath;
    private String savePath;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultMessage.setText("Choose image from your gallery.");
    }

    public void handleBrowseButton() {
        try {
            FileChooser fileChooser = new FileChooser();
            chooserPath = fileChooser.showOpenDialog(null).getAbsolutePath();
            formatName = chooserPath.substring(chooserPath.lastIndexOf(".") + 1);

            if ( validateFormat(formatName) ) {
                file = new File(chooserPath);
                image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.visibleProperty().setValue(true);
                saveButton.setDisable(false);
                resultMessage.setText("Enter width and height.");
                resultMessage.setTextFill(Color.BLACK);
            } else {
                resultMessage.setText("Wrong format.");
                resultMessage.setTextFill(Color.RED);
                saveButton.setDisable(true);
            }
        } catch (Exception e) {
            System.out.println("Can't load file.");
        }
    }

    public void handleSaveButton() throws IOException {
        if (isNumeric(newWidth.getText()) && isNumeric(newHeight.getText()) && (Integer.valueOf(newWidth.getText()) > 0 && Integer.valueOf(newWidth.getText()) > 0) ) {
            try {
                int scaledWidth = Integer.valueOf(newWidth.getText());
                int scaledHeight = Integer.valueOf(newHeight.getText());
                BufferedImage inputImage = ImageIO.read(file);
                BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

                g2d = outputImage.createGraphics();
                g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
                g2d.dispose();

                String[] splitPath = chooserPath.split("/");
                savePath = chooserPath.replace(splitPath[splitPath.length-1], newWidth.getText()+"x"+newHeight.getText()+"."+formatName);

                ImageIO.write(outputImage, formatName, new File(savePath));

                resultMessage.setText("Saved: "+savePath);
                resultMessage.setTextFill(Color.GREEN);
            } catch (IOException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        } else {
            resultMessage.setText("Enter correct width and height.");
            resultMessage.setTextFill(Color.RED);
        }
    }

    private boolean validateFormat(String str) {
        str.toLowerCase();
        if ( str.equals("jpg") || str.equals("jpeg") || str.equals("bmp") || str.equals("png") || str.equals("tiff") || str.equals("gif") ) {
            return true;
        }
        return false;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }
}
