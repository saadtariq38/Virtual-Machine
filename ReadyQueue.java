import java.util.*;
public class ReadyQueue {
    PriorityQueue<PCB> pQueue = new PriorityQueue<PCB>(16, new Comparator<PCB>() {
        @Override
        public int compare(PCB p1, PCB p2) {
            if (p1.processPriority > p2.processPriority)
                return 1;
            else
                return -1;
        }
    });

    Queue<PCB> FCFSQueue = new LinkedList<PCB>();

    public PriorityQueue<PCB> getPriorityQueue() {
        return pQueue;
    }

    public Queue<PCB> getFCFSQueue() {
        return FCFSQueue;
    }
}
