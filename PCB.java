public class PCB extends OS{
    Long processID;
    Long processPriority;
    int processSize;
    int dataSize;
    String processFileName;
    String processState;
    RegisterStorage reg;    //access to separate register storage for each PCB

    double execTime;
    double waitTime;
    int codeCounter;

   // int[] codePageTable = new int[512]; //512 size bcs 65536 total memory size divided by 128 which is the size of one frame i.e total frames are 512
    //int[] dataPageTable = new int[512];
    int[] pageTable = new int[512]; //common page table for data and code


    public PCB(Long priority,Long id,int processsize,int datasize,String name) {
        this.processPriority = priority;
    	this.processID = id;
    	this.processFileName = name;
    	this.processSize = processsize;
    	this.dataSize = datasize;
    	codeCounter= 0;
    	 
    	processState = "Ready";
    	reg = new RegisterStorage();   //init registerStorage class to access for GPR and SPR
    }

    public Long getProcessID() {
        return processID;
    }
    public Long getProcessPriority() {
        return processPriority;
    }
    public int getProcessSize() {
        return processSize;
    }
    public int getDataSize() {
        return dataSize;
    }
    public int getCodeCounter() {
        return codeCounter;
    }
    public String getProcessState() {
        return processState;
    }

    public void setProcessID(long processID) {
        this.processID = processID;
    }
    public void setProcessPriority(long processPriority) {
        this.processPriority = processPriority;
    }
    public void setProcessSize(int processSize) {
        this.processSize = processSize;
    }
    public void setDataSzie(int dataSize) {
        this.dataSize = dataSize;
    }
    public void setCodeCounter(int codeCounter) {
        this.codeCounter = codeCounter;
    }
    public void setProcessState(String processState) {
        this.processState = processState;
    }
}
