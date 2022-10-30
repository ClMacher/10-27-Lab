package item4;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

public class DecryptMD5Private {
    public static void main(String[] args) {
        try{
            File outFile = new File("archivos/item2/salida.txt");
            File keyStoreFile = new File("archivos/item2/keytool/myKeyStore.jks");
            String password = "storepass";

            // Carga el keystore
            KeyStore myKeyStore = KeyStore.getInstance("JKS");
            FileInputStream inStream = new FileInputStream(keyStoreFile);
            myKeyStore.load(inStream, password.toCharArray());

            // Lee las llaves privada y publica del keystore.
            Certificate cert = myKeyStore.getCertificate("mykey");
            PublicKey publicKey = cert.getPublicKey();

            // Inicializar el Objeto Cipher RSA
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, publicKey);

            FileInputStream encryptedFile = new FileInputStream(outFile);

            byte[] md5 = rsaCipher.doFinal(encryptedFile.readAllBytes());

            // Se escribe byte a byte en hexadecimal
            System.out.println("Hash desencriptado en Hex:");
            for (byte b : md5) {
                System.out.print(Integer.toHexString(0xFF & b));
            }
            System.out.println();

            inStream.close();
            encryptedFile.close();


        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
