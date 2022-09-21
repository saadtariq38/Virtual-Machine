
public class Test{

    

    public static void main(String[] args) {
      /*  Memory memory = new Memory();    //init the memory for all processes
       RegisterStorage  regStorage = new RegisterStorage(); //init the registers for all processes

       regStorage.setSpecialRegister(0, (short) 0); //setting first register to 0
       regStorage.setSpecialRegister(4, (short) -1); //setting stack pointer aka stack base to -1
       
             */
       

        VM vm = new VM("p0.txt", (short) 0);
        vm.cpu();
        
    }
}
