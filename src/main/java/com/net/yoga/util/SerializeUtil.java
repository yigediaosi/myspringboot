package com.net.yoga.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 序列化实现工具类，提供两种类型的序列化和反序列化实现java自身及hessian
 *
 * @author gaort
 */
@Slf4j
public class SerializeUtil {
    //    private static final Logger log = LogConstant.commonLog;

    /**
     * 基于Java实现序列化和反序列化
     *
     * @param objContent
     * @return
     * @throws IOException
     */
    public static byte[] javaSerialize(final Object objContent) throws IOException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream output = null;
        try {
            baos = new ByteArrayOutputStream(1024);
            output = new ObjectOutputStream(baos);
            output.writeObject(objContent);
        } catch (final IOException ex) {
            log.warn("IOException while JavaSerialize:", ex);
            throw ex;
        } finally {
            if (output != null) {
                try {
                    output.close();
                    if (baos != null) {
                        baos.close();
                    }
                } catch (final IOException ex) {
                    log.warn("IOException while JavaSerialize final:", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    public static Object javaDeserialize(final byte[] objContent) throws IOException {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(objContent);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (final IOException ex) {
            log.warn("IOException while JavaDeserialize:", ex);
            throw ex;
        } catch (final ClassNotFoundException ex) {
            log.warn("ClassNotFoundException while JavaDeserialize:", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                    bais.close();
                } catch (final IOException ex) {
                    log.warn("IOException while JavaDeserialize final:", ex);
                }
            }
        }
        return obj;
    }

    /**
     * Kryo serialize
     *
     * @param source
     * @return
     * @throws IOException
     */
    public static byte[] KryoSerialize(Object source) throws IOException {

        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            Kryo kryo = new Kryo();
            Output output = new Output(bos);
            kryo.writeObject(output, source);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);
            //byte[] outByteArray = output.toBytes();
            output.flush();
            output.close();
            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                log.warn("IOException while kryoWriteObject final:", e);
            }
        }
    }

    /**
     * Kryo deserialize
     *
     * @param bytes,Class
     * @return
     * @throws IOException
     */
    public static <T> T KryoDeserialize(byte[] bytes, Class<T> type) throws IOException {
        try {
            if (bytes == null) {
                return null;
            }
            Kryo kryo = new Kryo();
            Input input = new Input(bytes);
            T obj = kryo.readObject(input, type);
            SynchronizedCollectionsSerializer.registerSerializers(kryo);
            input.close();
            return obj;
        } catch (Exception e) {
            log.warn("Exception while kryoReadObject final:", e);
            return null;
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println("start");
        byte[] ser = KryoSerialize(1L);
        System.out.println("KryoSerialize=====" + new String(ser));
        System.out.println("KryoDeserialize=====" + KryoDeserialize(ser, Long.class));
    }
}
