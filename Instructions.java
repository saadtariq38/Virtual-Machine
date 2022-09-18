public class Instructions {

    //Register-Register Instructions

    static String MOV = "16";
    static String ADD = "17";
    static String SUB = "18";
    static String MUL = "19";
    static String DIV = "1A";
    static String AND = "1B";
    static String OR = "1C";

    //Register-Immediate Instructions

    static String MOVI = "30";
    static String ADDI = "31";
    static String SUBI = "32";
    static String MULI = "33";
    static String DIVI = "34";
    static String ANDI = "35";
    static String ORI = "36";
    static String BZ = "37";
    static String BNZ = "38";
    static String BC = "39";
    static String BS = "3A";
    static String JMP = "3B";
    static String CALL = "3C";
    static String ACT = "3D";

    //Memory Instructions

    static String MOVL = "51";
    static String MOVS = "52";

    //Single Operand Instructions\

    static String SHL = "71";
    static String SHR = "72";
    static String RTL = "73";
    static String RTR = "74";
    static String INC = "75";
    static String DEC = "76";
    static String PUSH = "77";
    static String POP = "78";

    //No Operand Instructions

    static String RETURN = "F1";
    static String NOOP = "F2";
    static String END = "F3";
 
}
//Integer.parseInt(hex, 16);
