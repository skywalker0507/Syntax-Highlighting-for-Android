package com.skywalker.syntaxhighlighter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/21               *
 *******************************/

public class FileUtil {

    public static byte[] readBytes(String filePath) throws IOException {
        return readBytes(new FileInputStream(filePath));
    }

    public static byte[] readBytes(FileInputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }

}
