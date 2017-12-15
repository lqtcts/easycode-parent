package com.concurrent;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;
/**
 *    1. Exchanger用于在2个线程中交换对象。
    2. return_object = exchanger.exchange(exch_object)
    3. 例子中Producer向ArrayList中缓慢填充随机整数，Consumer从另一个ArrayList中缓慢取出整数并输出。
    4. 当Producer的ArrayList填满，并且Consumer的ArrayList为空时，2个线程才交换ArrayList。
 * @author Administrator
 */
public class ExchangerTest {

    private static Exchanger<ArrayList<Integer>> exchanger = null;
    private static ArrayList<Integer> buffer1 = null;
    private static ArrayList<Integer> buffer2 = null;

    public static void main(String[] args) throws Exception {
        exchanger = new Exchanger<ArrayList<Integer>>();
        buffer1 = new ArrayList<Integer>(10);
        buffer2 = new ArrayList<Integer>(10);

        Thread pth = new ProducerThread();
        Thread cth = new ConsumerThread();

        pth.start();
        cth.start();

        Thread.sleep(60 * 1000);
        System.out.println("main: interrupting threads.");
        pth.interrupt();
        cth.interrupt();

        pth.join();
        cth.join();

        System.out.println("main: end.");
    }

    private static class ProducerThread extends Thread {
        @Override
        public void run() {
            ArrayList<Integer> buff = buffer1;
            try {
                while (true) {
                    if (buff.size() >= 10) {
                        // 与consumer交换buffer.
                        System.out.println("producer: exchanging.");
                        buff = exchanger.exchange(buff);
                        buff.clear();
                    }

                    // 随机产生一个0-100的整数。
                    int x = (int) (Math.random() * 100);
                    buff.add(x);
                    System.out.println("producer: " + x);

                    // 随机等待0-3秒 。
                    int t = (int) (Math.random() * 3);
                    Thread.sleep(t * 1000);
                }
            } catch (InterruptedException e) {
                System.out.println("producer: interrupted.");
            }
        }
    }

    private static class ConsumerThread extends Thread {
        @Override
        public void run() {
            ArrayList<Integer> buff = buffer2;
            try {
                while (true) {
                    for (Integer x : buff) {
                        System.out.println("consumer: " + x);

                        // 随机等待0-3秒 。
                        int t = (int) (Math.random() * 3);
                        Thread.sleep(t * 1000);
                    }

                    // 与producer交换buffer。
                    System.out.println("consumer: exchanging.");
                    buff = exchanger.exchange(buff);
                }
            } catch (InterruptedException e) {
                System.out.println("consumer: interrupted.");
            }
        }
    }
}