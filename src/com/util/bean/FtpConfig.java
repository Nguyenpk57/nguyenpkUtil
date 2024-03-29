package com.util.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenpk
 * @since 2022-03-07
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FtpConfig {
    private String user;
    private String ip;
    private Integer port;
    private String pass;
    private String remoteFolder;
    private String filePartent;
}
