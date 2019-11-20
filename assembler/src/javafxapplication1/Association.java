/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.util.ArrayList;

/**
 *
 * @author Giacomo
 */
public class Association {
    private static ArrayList<String[]> instr = new ArrayList<>();
    
    public Association(){
        String[] tmp0 = new String[]{"hlt", "nop"};
        instr.add(tmp0);

        String[] tmp1 = new String[]{"je", "jne", "ja", "jae", "jb", "jbe", "jg", "jge", "jl", "jle", "jz", "jnz", "jc", "jnc", "jo", "jno", "js", "jns", "jmp"};
        instr.add(tmp1);
                
        String[] tmp2 = new String[]{"je", "jne", "ja", "jae", "jb", "jbe", "jg", "jge", "jl", "jle", "jz", "jnz", "jc", "jnc", "jo", "jno", "js", "jns", "inc", "dec", "neg", "not", "jmp"};        
        instr.add(tmp2);
        
        String[] tmp3 = new String[]{"mov", "add", "sub", "cmp", "mul", "imul", "div", "idiv", "and", "or", "shl", "sal", "shr", "sar", "load"};
        instr.add(tmp3);
        
        String[] tmp4 = new String[]{"mov", "add", "sub", "cmp", "mul", "imul", "div", "idiv", "and", "or", "shl", "sal", "shr", "sar", "load", "store", "xchg"};
        instr.add(tmp4);
        
        String[] tmp5 = new String[]{"store"};
        instr.add(tmp5);
        
        String[] tmp6 = new String[]{"je", "jne", "ja", "jae", "jb", "jbe", "jg", "jge", "jl", "jle", "jz", "jnz", "jc", "jnc", "jo", "jno", "js", "jns", "jmp"};
        instr.add(tmp6);
        
        /*
        for(String[] e : instr){
            for(String s : e)
                System.out.println(s);
            System.out.println("------------");
        }
        */
    }
        

    
    private static String[] regDef = {"ax", "bx", "cx", "dx", "si", "di"};
    
    public static int checkOperandType(String s){
        if(s.contains("%")){
            int reg = checkReg(s);
            //System.out.print("\tRegister!  "+ reg + "\tRegID: " + Integer.toBinaryString(0x100 | reg).substring(1));
            return 0;
        }
        else if(s.contains("$")){
            //System.out.print("\t$ type!");
            return 1;
        }
        else if(s.contains("0x")){
            //System.out.print("\t0x type!");
            return 2;
        }
        else
            return -1;
    }
    
    public static int checkReg(String s){
        for(int i=0; i<regDef.length; i++)
            if(regDef[i].equalsIgnoreCase(s))
                return i;
        return -1;
    }
    
    public int findOperationId(String s, int format){
        String[] search = instr.get(format);
        for(int i =0; i<search.length; i++){
            if(search[i].equalsIgnoreCase(s))
                return i;
        }
        return -1;
    }
    
    public static String tellMeInstruction(int id, int format){
        String[] search = instr.get(format);
        return search[id];
    }
    
    public static String returnOperandTranslation(String s, int format){        
        int opType = checkOperandType(s);

        if (opType == 0) {
            s = s.replace("%", "");
            System.out.print("\toperand: " + s);
            Integer regId = new Integer(checkReg(s));
            if(regId == -1){                
                System.err.println("Wrong reg name detected");
                System.exit(1);
            }
            return Integer.toBinaryString(0x100 | regId).substring(1);
            
            //return "00000000" + Integer.toBinaryString(regId);
        }

        if (opType == 1) {
            s = s.replace("$", "");
            if (s.length() == 0) {
                System.err.println("No operand detected");
                System.exit(1);
            }
            int num = 0;
            try {
                num = Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.err.println("Number Format exception");
                System.exit(1);
            }
            if (num >= Math.pow(2, 16)) {
                System.err.println("Too big operand");
                System.exit(1);
            }
                    
            
            System.out.print("\toperand: d" + s);
            return Integer.toBinaryString(0x10000 | Integer.parseInt(s)).substring(1);
            
        } else if (opType == 2) {
            s = s.replace("0x", "");  
            if(s.length() != 4){
                System.err.println("Wrong length operand");
                System.exit(1);
            }
 
            System.out.print("\toperand: h" + s);
            return Integer.toBinaryString(0x10000 | Integer.parseInt(s, 16)).substring(1);
        }

        return "";
    }
}
