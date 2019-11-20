/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Giacomo Pellicci
 */
public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        StackPane root = new StackPane();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Assembler");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("assembly Files", "*.s"));
        File f = fileChooser.showOpenDialog(primaryStage);
        if (f == null) {
            System.err.println("bad file");
            System.exit(1);
        }
        String path = f.getAbsolutePath();
        String filename = f.getName();
        path = path.replace(filename, "");
        filename = filename.replace(".s", "");
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Assembler!");
        primaryStage.setScene(scene);
        //primaryStage.show();
        Parse.parseFile(path, filename);
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
