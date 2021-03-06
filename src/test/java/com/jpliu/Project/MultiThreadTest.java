package com.jpliu.Project;

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }
    @Override
    public  void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("%d : %d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThreadTest {

    public static void testThread() {
        for (int i = 0; i < 10; ++i) {
            new MyThread(i).start();
        }
    }
    public static void main(String[] args) {
        testThread();
    }
}
