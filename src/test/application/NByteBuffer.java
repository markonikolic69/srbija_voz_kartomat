package test.application;

import java.util.Arrays;

public class NByteBuffer
{
    private static int DEFAULTSIZE;
    private byte[] buffer;
    private int position;
    private int maxsize;
    
    static {
        NByteBuffer.DEFAULTSIZE = 1024;
    }
    
    public NByteBuffer() {
        this(NByteBuffer.DEFAULTSIZE);
    }
    
    public NByteBuffer(final int size) {
        this.position = 0;
        this.maxsize = size;
        this.buffer = new byte[size];
    }
    
    public byte[] getBuffer() {
        if (this.position <= 0) {
            return null;
        }
        final byte[] tmpBuf = new byte[this.position];
        System.arraycopy(this.buffer, 0, tmpBuf, 0, this.position);
        return tmpBuf;
    }
    
    public byte[] getAllBuffer() {
        return this.buffer;
    }
    
    public boolean setBuffer(final byte[] newBuf) {
        if (newBuf == null) {
            return false;
        }
        System.arraycopy(newBuf, 0, this.buffer = new byte[newBuf.length], 0, newBuf.length);
        this.position = newBuf.length;
        return true;
    }
    
    public void clear() {
        Arrays.fill(this.buffer, (byte)0);
        this.position = 0;
    }
    
    public boolean addBuffer(final byte addNum) {
        final byte[] addBuf = { addNum };
        return this.addBuffer(addBuf);
    }
    
    public boolean addBuffer(final byte[] addBuf) {
        return this.addBuffer(addBuf, addBuf.length);
    }
    
    public boolean addBuffer(final byte[] addBuf, int addSize) {
        if (addBuf == null) {
            return false;
        }
        byte[] tmpBuf = null;
        final int nRemain = this.maxsize - this.position;
        if (addSize > addBuf.length) {
            addSize = addBuf.length;
        }
        if (nRemain < addSize) {
            tmpBuf = new byte[this.maxsize];
            System.arraycopy(this.buffer, 0, tmpBuf, 0, this.buffer.length);
            if (addSize < NByteBuffer.DEFAULTSIZE) {
                this.maxsize += NByteBuffer.DEFAULTSIZE;
            }
            else {
                this.maxsize += addSize - nRemain;
            }
            System.arraycopy(tmpBuf, 0, this.buffer = new byte[this.maxsize], 0, tmpBuf.length);
            tmpBuf = null;
        }
        System.arraycopy(addBuf, 0, this.buffer, this.position, addSize);
        this.position += addSize;
        return true;
    }
}

