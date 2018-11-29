package com.t0mpi9.test;

import org.junit.Test;

import java.io.File;

/**
 * <br/>
 * Created on 2018/9/25 11:49.
 *
 * @author zhubenle
 */
public class UtilTest {

    @Test
    public void testFile() {
        System.out.println(File.pathSeparator);
        System.out.println(File.separator);
    }

    @Test
    public void testSplit() {
        String command = "cd test bbbbb";
        System.out.println(command.split(" ")[0]);
        System.out.println(command.substring(command.indexOf(" ") + 1));
    }

    @Test
    public void getBytes() {
        byte[] bar = "744".getBytes();
        System.out.println(bar[0]);
        System.out.println(Character.toString((char) bar[0]));

        int foo = 0;
        int k;
        for (int j = 0; j < bar.length; j++) {
            k = bar[j];
            if (k < '0' || k > '7') {
                foo = -1;
                break;
            }
            foo <<= 3;
            foo |= (k - '0');
        }
        System.out.println(foo);
    }
}
