/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disassebler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Alberto-Surface
 */
public class Disassembler extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Binary file", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            System.exit(1);
        }
        
        
        /*
         * 1. args[0] is the input filepath. Temporarely we're using a fixed path
         * 2. The output filepath is calculated from the input one, but for now
         *    we're using a fixed path
        */
        String pathBin = selectedFile.getAbsolutePath();
        String filename = selectedFile.getName();
        String pathDecompiled = pathBin.replace(filename, "");
        String inFilePath =  pathBin;
        String outFilePath = pathDecompiled.concat("\\prova.deSource");
        
        //Create the input stream
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(inFilePath);
        }
        catch(FileNotFoundException fnfe) {
            System.err.println("Error: file not found. Operation aborted.");
            return;
        }
        
        //Create the output stream
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(outFilePath));
        }
        catch(IOException ioe) {
            System.err.println("Error: IOException try opening the ouput file. Operation interrupted.");
            return;
        }
        
        //Let's read 32bit (4bytes) a time
        //Every quadruplet become a magic wonderful instruction
        byte[] instructionDump = new byte[4];
        int readedBytes;
        do{
            try {
                readedBytes = inputStream.read(instructionDump, 0, 4);
                if(readedBytes !=4)
                    break;
                Instruction inst = new Instruction(instructionDump);
                writer.write(inst.toString() + "\n"); //The magic is here
                printTheForBytes(instructionDump);
            }
            catch(IOException ioe){
                System.err.println("Error: IOException reading the input file. Operation interrupted.");
                return;
            }
        }while(readedBytes == 4);
        
        //Close streams
        try {
            writer.close();
            inputStream.close();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }       
        System.exit(0);
    }

    private static void printTheForBytes(byte[] buf) {
        System.out.println(
            String.format("%02x", buf[0]) + " " + String.format("%02x", buf[1]) + " " + String.format("%02x", buf[2]) + " " + String.format("%02x", buf[3]) + "\n"
        );
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
