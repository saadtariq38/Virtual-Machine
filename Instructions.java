public class Instructions {

    //Register-Register Instructions

    static final String MOV = "16";
    static final String ADD = "17";
    static final String SUB = "18";
    static final String MUL = "19";
    static final String DIV = "1A";
    static final String AND = "1B";
    static final String OR = "1C";

    
 
    static final String MOVI = "30";
    static final String ADDI = "31";
    static final String SUBI = "32";
    static final String MULI = "33";
    static final String DIVI = "34";
    static final String ANDI = "35";
    static final String ORI = "36";
    static final String BZ = "37";
    static final String BNZ = "38";
    static final String BC = "39";
    static final String BS = "3A";
    static final String JMP = "3B";
    static final String CALL = "3C";
    static final String ACT = "3D";

    //Memory Instructions

    static final String MOVL = "51";
    static final String MOVS = "52";

    //Singlfinal e Operand Instructions

    static final String SHL = "71";
    static final String SHR = "72";
    static final String RTL = "73";
    static final String RTR = "74";
    static final String INC = "75";
    static final String DEC = "76";
    static final String PUSH = "77";
    static final String POP = "78";

    //No Opfinal erand Instructions
 
    static final String RETURN = "F1";
    static final String NOOP = "F2";
    static final String END = "F3";
 
}
//Integer.parseInt(hex, 16);
