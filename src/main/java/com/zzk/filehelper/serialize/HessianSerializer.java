package com.zzk.filehelper.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zzk
 * @date 2021/12/10
 * description 使用hessian进行序列化
 */
public class HessianSerializer {
    public byte[] serialize(Object obj) {
        HessianOutput output = null;
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            output = new HessianOutput(arrayOutputStream);
            output.writeObject(obj);
            return arrayOutputStream.toByteArray();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public Object deserialize(byte[] bytes,int length, Class<?> clazz) {
        HessianInput input = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes, 0, length)) {
            input = new HessianInput(inputStream);
            return input.readObject(clazz);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

}
