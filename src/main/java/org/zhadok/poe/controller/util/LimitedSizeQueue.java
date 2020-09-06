package org.zhadok.poe.controller.util;

import java.util.ArrayList;

/**
 * https://stackoverflow.com/questions/1963806/is-there-a-fixed-sized-queue-which-removes-excessive-elements
 * @param <K>
 */
public class LimitedSizeQueue<K> extends ArrayList<K> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7089549874757511073L;
	private int maxSize;

    public LimitedSizeQueue(int size){
        this.maxSize = size;
    }

    @Override
    public void add(int index, K k) {
    	super.add(index, k);
    	if (size() > maxSize){
            removeRange(maxSize, size());
        }
    }
    
    public K getYoungest() {
        return get(size() - 1);
    }

    public K getOldest() {
        return get(0);
    }
}
