package xor7studio.boat;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Signal {
    private final Lock lock=new ReentrantLock();
    private final Condition condition=lock.newCondition();
    public void waitForSignal(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void emitSignal(){
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
