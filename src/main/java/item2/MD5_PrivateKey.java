package item2;//package com.chuidiang.ejemplos.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import javax.crypto.Cipher;


/*
   GRACIAS A ESTE DOBLE ENCRIPTADO SE LOGRA UNA CAPA EXTRA DE SEGURIDAD
   AL SER UNA CLAVE RSA MUCHO MAS DIFICIL DE DESENCRIPTAR
 */
public class MD5_PrivateKey {
   public static void main(String[] args) throws Exception {

      File outFile = new File("archivos/item2/salida.txt");
      File keyStoreFile = new File("archivos/item2/keytool/myKeyStore.jks");
      String password = "storepass";

      // Carga el keystore
      KeyStore myKeyStore = KeyStore.getInstance("JKS");
      FileInputStream inStream = new FileInputStream(keyStoreFile);
      myKeyStore.load(inStream, password.toCharArray());

      // Lee la llave privada del keystore.
      PrivateKey privatekey = (PrivateKey) myKeyStore.getKey("mykey", "storepass".toCharArray());

      // Especifica un mensaje y genera su MD5
      MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      String texto = "mensaje secreto";
      md.update(texto.getBytes());
      byte[] digest = md.digest();

      // Se escribe byte a byte en hexadecimal
      System.out.println("Hash en Hex:");
      for (byte b : digest) {
         System.out.print(Integer.toHexString(0xFF & b));
      }
      System.out.println();

      // Inicializa el Objeto Cipher RSA
      Cipher rsaCipher = Cipher.getInstance("RSA");
      rsaCipher.init(Cipher.ENCRYPT_MODE, privatekey);

      // Realiza la encriptacion
      byte[] cipherText = rsaCipher.doFinal(digest);

      // Escribir el texto plano encriptado al archivo.
      FileOutputStream outToFile = new FileOutputStream(outFile);
      outToFile.write(cipherText);

      //Cerrando Archivos
      inStream.close();
      outToFile.close();

   }
}
