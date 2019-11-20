package disassebler;


public class Instruction {
    private final byte  opcode;
    private final short op1;
    private final byte  op2;

    public Instruction(byte[] instructionDump) {
        this.opcode = instructionDump[0];
        
        byte[] help = new byte[2];
        help[1] = instructionDump[1];
        help[0] = instructionDump[2];
        this.op1 = Utils.bytesToShort(help);
        
        this.op2 = instructionDump[3];
    }
    
    @Override
    public String toString() {
        try {
            
            int type = getType();
            
            System.out.println("Conversione istruzione:\nTipo: " + type + "\nIndice: " + (int)(opcode & 0b00011111) + "\nop1: " + op1 + "\nop2: " + op2);
            
            if(type == 0)
                return opcodeToString();
            else if(type == 1)
                return opcodeToString() + " " + Utils.numberToHexString(op1);
            else if(type == 2)
                return opcodeToString() + " %" + registers[op1];
            else if(type == 3)
                return opcodeToString() + " " + Utils.numberToHexString(op1) + ", %" + registers[op2];
            else if(type == 4)
                return opcodeToString() + " %" + registers[op1] + ", %" + registers[op2];
            else if(type == 5)
                return opcodeToString() + " %" + registers[op2] + ", %" + registers[op1];
            else if(type == 6)
                return opcodeToString() + " %" + registers[op2] + ", " + Utils.numberToHexString(op1);
            else
                return "???";
            
        }catch(Exception e){
            e.printStackTrace();
            return "????";
        }
    }
    
    
    
    /*
     * From here you can read the source of the private functions.
    */
    
    private String opcodeToString() {
        int type = getType();
        int index = (int)(opcode & 0b00011111);
        if(type == 6)
            return groups[5][0];
        else if(type < 5)
            return groups[type][index];
        else
            return "UNKOPCODE";
    }
    
    private int getType() {
        /*
         * To do: fix this
         * I don't know how to recognize group 5 :(
        */
        
        //store con "formato" 4??? con reg, ind immediato
        if(opcode == 0b10001111){       //store tipo 4 da rigirare
            return 5;
        }else if(opcode == 0b01101111){ //store tipo 3 da rigirare
            return 6;
        }else{
            return (int)(0b00000000000000000000000000000111 & (opcode >> 5));
        }
    }
    
    
    /*
     * And here we have the static members that's mapping opcodes and register
     * numbers (identifiers) to ascii text.
     * 
     * WARNING: We need to check if those are correct because i copynpasted from
     * an old version of the program Parse
    */
    private static final String[][] groups;
    private static final String[] registers;
    static {
        groups = new String[6][];
        groups[0] = new String[]{"hlt", "nop"};
        groups[1] = new String[]{"je", "jne", "ja", "jae", "jb", "jbe", "jg", "jge", "jl", "jle", "jz", "jnz", "jc", "jnc", "jo", "jno", "js", "jns", "jmp"};
        groups[2] = new String[]{"je", "jne", "ja", "jae", "jb", "jbe", "jg", "jge", "jl", "jle", "jz", "jnz", "jc", "jnc", "jo", "jno", "js", "jns", "inc", "dec", "neg", "not", "jmp"};
        groups[3] = new String[]{"mov", "add", "sub", "cmp", "mul", "imul", "div", "idiv", "and", "or", "shl", "sal", "shr", "sar", "load"};
        groups[4] = new String[]{"mov", "add", "sub", "cmp", "mul", "imul", "div", "idiv", "and", "or", "shl", "sal", "shr", "sar", "load", "store", "xchg"};
        groups[5] = new String[]{"store"};
        
        registers = new String[]{"ax", "bx", "cx", "dx", "si", "di"};
    }
}

