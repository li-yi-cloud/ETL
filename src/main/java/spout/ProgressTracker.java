package spout;

import java.io.PrintStream;
import java.util.TreeSet;

public class ProgressTracker {

    TreeSet<FileOffset> offsets = new TreeSet<>();

    public synchronized void recordAckedOffset(FileOffset newOffset) {
        if(newOffset==null) {
            return;
        }
        offsets.add(newOffset);

        FileOffset currHead = offsets.first();

        if( currHead.isNextOffset(newOffset) ) { // check is a minor optimization
            trimHead();
        }
    }

    // remove contiguous elements from the head of the heap
    // e.g.:  1,2,3,4,10,11,12,15  =>  4,10,11,12,15
    private synchronized void trimHead() {
        if(offsets.size()<=1) {
            return;
        }
        FileOffset head = offsets.first();
        FileOffset head2 = offsets.higher(head);
        if( head.isNextOffset(head2) ) {
            offsets.pollFirst();
            trimHead();
        }
        return;
    }

    public synchronized FileOffset getCommitPosition() {
        if(!offsets.isEmpty()) {
            return offsets.first().clone();
        }
        return null;
    }

    public synchronized void dumpState(PrintStream stream) {
        stream.println(offsets);
    }

    public synchronized int size() {
        return offsets.size();
    }
}
