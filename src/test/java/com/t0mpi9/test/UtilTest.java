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
}
