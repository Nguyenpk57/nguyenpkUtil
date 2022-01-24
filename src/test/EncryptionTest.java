package test;

import com.util.func.encryption.MD5;
import com.util.func.encryption.SHA1;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptionTest {
    public static void main(String[] args) throws Exception {
        String init = "Hello, world";
        //MD5
        System.out.println(MD5.getInstance().getMd5(init)); //bc6e6f16b8a077ef5fbc8d59d0b931b9
        //DigestUtils MD5
        System.out.println(DigestUtils.md5Hex(init)); //bc6e6f16b8a077ef5fbc8d59d0b931b9

        //SHA1
        System.out.println(SHA1.getInstance().sha1(init)); //e02aa1b106d5c7c6a98def2b13005d5b84fd8dc8
        //DigestUtils SHA1
        System.out.println(DigestUtils.shaHex(init)); //e02aa1b106d5c7c6a98def2b13005d5b84fd8dc8

        //Cipher

    }
}
