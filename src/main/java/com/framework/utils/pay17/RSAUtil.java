package com.framework.utils.pay17;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by ldd_person on 2017/3/3.
 */
public class RSAUtil {


    public static String encrypttoStr(String keyUrl, String content)
            throws Exception
    {
        ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream(new File(keyUrl)));
        RSAPublicKey publicKey = (RSAPublicKey)inputStream.readObject();
        RandUtil rand = new RandUtil();
        String endata = rand.parseByte2HexStr(publicEnrypy(publicKey, content));
        return endata;
    }

    public static byte[] publicEnrypy(Key publicKey, String data)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());

        cipher.init(1, publicKey);

        byte[] result = cipher.doFinal(data.getBytes());
        return result;
    }
    }
