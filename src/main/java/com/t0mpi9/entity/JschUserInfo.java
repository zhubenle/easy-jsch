package com.t0mpi9.entity;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * <br/>
 * Created on 2018/9/18 17:20.
 *
 * @author zhubenle
 */
public class JschUserInfo implements UserInfo, UIKeyboardInteractive {

    private String password;
    private String passphrase;
    private Boolean promptPassword = true;
    private Boolean promptPassphrase= true;
    private Boolean promptYesNo = true;

    public JschUserInfo(String password) {
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return passphrase;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String message) {
        return promptPassword;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return promptPassphrase;
    }

    @Override
    public boolean promptYesNo(String message) {
        return promptYesNo;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        return new String[0];
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public void setPromptPassword(Boolean promptPassword) {
        this.promptPassword = promptPassword;
    }

    public void setPromptPassphrase(Boolean promptPassphrase) {
        this.promptPassphrase = promptPassphrase;
    }

    public void setPromptYesNo(Boolean promptYesNo) {
        this.promptYesNo = promptYesNo;
    }
}
