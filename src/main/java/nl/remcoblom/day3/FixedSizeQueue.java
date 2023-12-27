package nl.remcoblom.day3;

import java.util.LinkedList;

public class FixedSizeQueue<E> {

    private final LinkedList<E> queue = new LinkedList<E>();
    private final int size;

    public FixedSizeQueue(int size) {
        this.size = size;
        for(int i = 0; i < size; i++) {
            queue.add(null);
        }
    }

    public E addAndRemoveHead(E e) {
        if (queue.size() < size) {
            queue.add(e);
            return null;
        }
        E removedHead = queue.poll();
        queue.add(e);
        return removedHead;
    }

    public E get(int index) {
        try {
            return queue.get(index);
        } catch (Exception e) {
            return null;
        }
    }
}
