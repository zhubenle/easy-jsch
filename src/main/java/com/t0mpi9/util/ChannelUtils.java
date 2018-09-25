package com.t0mpi9.util;

import com.jcraft.jsch.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * <br/>
 * Created on 2018/9/19 11:10.
 *
 * @author zhubenle
 */
public class ChannelUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChannelUtils.class);

    public static String read(Channel channel) {
        StringBuilder sb;
        try {
            InputStream in = channel.getInputStream();
            sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    sb.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    LOGGER.info("exit-status: {}", channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) {
            return b;
        }
        if (b == -1) {
            return b;
        }

        if (b == 1 || b == 2) {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) {
                // error
            }
            if (b == 2) {
                // fatal error
            }
            LOGGER.info(sb.toString());
        }
        return b;
    }
}
