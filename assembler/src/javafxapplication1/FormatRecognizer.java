/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.regex.Pattern;

/**
 *
 * @author Giacomo
 */
public class FormatRecognizer {
    
    public static int findFormat(String s){
        
        //OPCODE 
        if(Pattern.matches("^\\s*\\S+\\s*$", s)){
            return 0;
        }
        
        //OPCODE address
        else if(Pattern.matches("^\\s*\\S+\\s+[0x]\\S+\\s*$", s)){
            return 1;
        }
        
        //OPCODE %reg
        else if(Pattern.matches("^\\s*\\S+\\s+[%]\\S+\\s*$", s)){
            return 2;
        }
        
        //OPCODE $immediate, %reg
        else if(Pattern.matches("^\\s*\\S+\\s+([$]|0x)\\S+[,]\\s*[%]\\S+\\s*$", s)){
            return 3;
        }
            
        //OPCODE %reg, %reg
        else if(Pattern.matches("^\\s*\\S+\\s+[%]\\S+[,]\\s*[%]\\S+\\s*$", s)){
            return 4;
        }
        
        //special return for STORE %REG, $IMM instruction (store %reg, %reg already covered by format 4)
        else if(Pattern.matches("^\\s*(store|STORE)\\s+[%]\\S+\\s*[,]\\s*([$]|0x)\\S+\\s*$", s)){
            return 5;
        }
        
         //OPCODE label for JMP label
        else if(Pattern.matches("^\\s*\\S+\\s+\\S+\\s*$", s)){
            return 6;
        }
        
        return -1;
    }
}
