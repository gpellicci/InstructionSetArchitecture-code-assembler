/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author Giacomo
 */
public class Parse {

    private static final Association a = new Association();
    private static final String[] mnemonicFormat = {"OPCODE no operands\t", "OPCODE address\t\t", "OPCODE %reg\t\t", "OPCODE imm, %reg\t", "OPCODE %reg, %reg", "store imm, %reg\t\t"};
    private static ArrayList<Label> labels = new ArrayList<>();

    public static void parseFile(String path, String filename) throws IOException {
        int i = 0;
        File in = new File(path + filename + ".s");
        
        PrintWriter copy_wr = new PrintWriter(path + "copia_" + filename + ".tmp", "UTF-8");
        File copy = new File(path + "copia_" + filename + ".tmp");
        File object_file = new File(path + filename + ".o");
        PrintWriter wr = new PrintWriter(path + filename + ".o", "UTF-8");
        File bin_file = new File(path + filename + ".bin");
        FileOutputStream out = new FileOutputStream(bin_file);

        //1°giro per creare la struttura dati labels e rimuovere le linee bianche
        try (BufferedReader br = new BufferedReader(new FileReader(in))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                if (Pattern.matches("^\\s*$", line)) {
                    continue;
                }

                if (line.contains(":")) {
                    Pattern patt = Pattern.compile("\\:");
                    String[] str = patt.split(line);
                        for(String s: str)
                            System.out.println(s);
                    for (Label l : labels) {
                        if (str[0].equals(l.getName())) {
                            System.err.println("Duplicate label");
                            wr.close();
                            br.close();
                            out.close();
                            copy_wr.close();
                            Files.delete(copy.toPath());
                            Files.delete(object_file.toPath());
                            Files.delete(bin_file.toPath());
                            System.exit(1);
                        }
                    }
                    
                    labels.add(new Label(str[0], row));
                    line = line.replace(str[0] + ":", "");
                    System.out.println("Etichetta trovata: " + str[0] + "\t Riga: " + row);
                }
                ++row;
                copy_wr.println(line);
                copy_wr.flush();
            }
            copy_wr.close();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(copy))) {
            String line;
            int format;
            while ((line = br.readLine()) != null) {
                format = FormatRecognizer.findFormat(line);
                if (format == 6) {
                    format = 1;
                    Pattern p = Pattern.compile("\\s+");
                    String[] tmp = p.split(line);

                    int label_index = tmp.length - 1;
                    tmp[label_index] = tmp[label_index].replace("\\s+", "");
                    Association aa = new Association();
                    int ret = aa.findOperationId(tmp[label_index-1], 6);
                    
                    if(ret == -1){
                        System.err.println("Syntax error.");
                        wr.close();
                        br.close();
                        out.close();
                        Files.delete(copy.toPath());
                        Files.delete(object_file.toPath());
                        Files.delete(bin_file.toPath());
                        System.exit(1);
                    }
                    int label_line = -1;
                    for (Label l : labels) {
                        if (l.getName().equals(tmp[label_index])) {
                            label_line = l.getLine();
                            break;
                        }
                    }
                    if (label_line == -1) {
                        System.err.println("Wrong label");
                        wr.close();
                        br.close();
                        out.close();
                        Files.delete(copy.toPath());
                        Files.delete(object_file.toPath());
                        Files.delete(bin_file.toPath());
                        System.exit(1);
                    }
                    tmp[label_index] = "0x" + String.format("%1$04X", (label_line * 2));
                    line = tmp[label_index - 1] + " " + tmp[label_index];
                }
                if (format < 0) {
                    br.close();
                    wr.close();
                    out.close();
                    Files.delete(copy.toPath());
                    Files.delete(object_file.toPath());
                    Files.delete(bin_file.toPath());
                    System.err.println("COMPILATION ERROR AT LINE " + (i + 1));
                    System.err.println("\tThe instruction format used is wrong");
                    return;
                }

                System.out.print("Format: " + format + "\t" + mnemonicFormat[format] +/*"\tTRANSLATED:\t"+bin+*/ " | ");
                System.out.println("\tline #" + ++i + "\t" + line);
                String[] bin = InstructionSplitter(line, format);
                if (bin == null) {
                    wr.close();
                    br.close();
                    out.close();
                    Files.delete(copy.toPath());
                    Files.delete(object_file.toPath());
                    Files.delete(bin_file.toPath());
                    System.exit(1);
                }
                String binaryInstr = padding(bin, format);
                wr.println(binaryInstr);

                System.out.println("Instr: " + binaryInstr);

                byte[] bval = new BigInteger(binaryInstr, 2).toByteArray();
                if(bval.length == 1) {
                    byte[] bval_hlt = new byte[4];
                    for(int w=0; w<4; ++w)
                        bval_hlt[w] = 0;
                    out.write(bval_hlt);
                }
                else if(bval.length == 5) {
                    byte[] bval1 = new byte[4];
                    for(int k=1; k<5; ++k)
                        bval1[k-1] = bval[k];
                    out.write(bval1);
                }
                else {
                    out.write(bval);
                }
                
                System.out.println("");
            }

            wr.close();
            br.close();
            Files.delete(copy.toPath());
            System.out.println("Source code assembled. #lines " + i);
            System.out.println("\tBinary and text file produced.");

        }
    }

    public static String[] InstructionSplitter(String s, int format) {
        s = s.replaceAll("^\\s*", "");
        Pattern pattern = Pattern.compile("(\\s+|,\\s*)");
        String[] instruction = pattern.split(s);
        String[] binaryInstruction = new String[instruction.length];
        //System.out.println("Instruction length: "+instruction.length);

        if (instruction.length > 2) {
            instruction[1] = instruction[1].replace(",", "");
        }
        for (int i = 0; i < instruction.length; i++) {
            String ret;
            System.out.print("\t#" + i + "\t" + instruction[i]);
            if (i > 0) {

                //Association.checkOperandType(instruction[i]);
                ret = Association.returnOperandTranslation(instruction[i], format);
                if (ret.equals("")) {
                    System.err.println("Assembling error !!!");
                    return null;
                } else {
                    //System.out.print("\t\tId: "+ ret);                    
                }
            } else {
                int id = a.findOperationId(instruction[i], format);
                if (id == -1) {
                    System.err.println("Syntax error.\n");
                    return null;
                }

                //special case for store imm, reg
                if (format == 5) {
                    format = 3;
                    id = 15;
                }

                ret = Integer.toBinaryString(0x10 | format).substring(2) + Integer.toBinaryString(0x100 | id).substring(4);

                //special case for store imm, reg
                if (format == 3) {
                    format = 5;
                    id = 0;
                }

                System.out.print("\tInstruction\t\tId: " + ret + "\t" + Association.tellMeInstruction(id, format));

            }
            System.out.println("");
            binaryInstruction[i] = ret;
        }
        return binaryInstruction;
    }

    public static String padding(String[] bin, int format) {
        String instr = "";
        for (String s : bin) {
            instr += s;
        }
        if (format == 0) {
            instr = padZero(instr, 24);
        } else if (format == 1 ) {
            instr = padZero(instr, 8);
        } else if (format == 2) {
            instr = bin[0] + "00000000" + bin[1] + "00000000";
        } else if (format == 4){
            instr = bin[0] + "00000000" + bin[1] + bin[2];
        } else if (format == 5) {
            System.out.println("Store operand swap");
            instr = bin[0] + bin[2] + bin[1];
        }
        return instr;
    }

    private static String padZero(String s, int n) {
        for (int i = 0; i < n; i++) {
            s += "0";
        }
        return s;
    }

}
