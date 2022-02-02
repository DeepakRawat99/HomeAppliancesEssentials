package com.hae.haessentials.utility

import android.util.Base64
import java.io.UnsupportedEncodingException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.lang.Exception
import java.lang.StringBuilder
import java.security.*


class EncryptDecryptData {
    private lateinit var cipherT: Cipher

    private lateinit var endecryptkey: ByteArray
    private lateinit var bitiv:ByteArray

    private enum class EncryptMode{
        ENCRYPT, DECRYPT
    }
    //in kotlin
    init {
        initializevalues()
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class)
    private fun initializevalues() {
        cipherT = Cipher.getInstance("AES/CBC/PKCS5Padding")
        endecryptkey = ByteArray(32) //256 bit key space
        bitiv = ByteArray(16) //128 bit IV
    }

    @Throws(
        UnsupportedEncodingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun encryptDecrypt(
        inputText: String, encryptionKey: String,
        mode: EncryptMode, initVector: String
    ): ByteArray? {
        var len = encryptionKey.toByteArray(charset("UTF-8")).size // length of the key	provided
        if (encryptionKey.toByteArray(charset("UTF-8")).size > endecryptkey.size) len = endecryptkey.size
        var ivlength = initVector.toByteArray(charset("UTF-8")).size
        if (initVector.toByteArray(charset("UTF-8")).size > bitiv.size) ivlength = bitiv.size
        System.arraycopy(encryptionKey.toByteArray(charset("UTF-8")), 0, endecryptkey, 0, len)
        System.arraycopy(initVector.toByteArray(charset("UTF-8")), 0, bitiv, 0, ivlength)
        val keySpec = SecretKeySpec(
            endecryptkey,
            "AES"
        ) // Create a new SecretKeySpec for the specified key data and algorithm name.
        val ivSpec =
            IvParameterSpec(bitiv) // Create a new IvParameterSpec instance with the bytes from the specified buffer iv used as initialization vector.

        // encryption
        return if (mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older. Read for more info.
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            cipherT.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec) // Initialize this cipher instance
            cipherT.doFinal(inputText.toByteArray(charset("UTF-8"))) // Finish multi-part transformation (encryption)
        } else {
            cipherT.init(Cipher.DECRYPT_MODE, keySpec, ivSpec) // Initialize this cipher instance
            val decodedValue: ByteArray = Base64.decode(inputText.toByteArray(), Base64.DEFAULT)
            cipherT.doFinal(decodedValue) // Finish multi-part transformation (decryption)
        }
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun sHAhASH(text: String, length: Int): String? {
        val resultString: String
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        md.update(text.toByteArray(charset("UTF-8")))
        val digest: ByteArray = md.digest()
        val result = StringBuilder()
        for (b in digest) {
            result.append(String.format("%02x", b)) //convert to hex
        }
        resultString = if (length > result.toString().length) {
            result.toString()
        } else {
            result.toString().substring(0, length)
        }
        return resultString
    }


    @Throws(Exception::class)
    fun encryptUserData(plainText: String?, key: String?, iv: String?): String? {
        val bytes = encryptDecrypt(
            plainText!!, key!!, EncryptMode.ENCRYPT,
            iv!!
        )
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decryptUserData(cipherText: String?, key: String?, iv: String?): String? {
        val bytes = encryptDecrypt(
            cipherText!!, key!!, EncryptMode.DECRYPT,
            iv!!
        )
        return String(bytes!!)
    }

    private fun generateIV16(): String {
        val ranGen = SecureRandom()
        val aesKey = ByteArray(16)
        ranGen.nextBytes(aesKey)
        val result = StringBuilder()
        for (b in aesKey) {
            result.append(String.format("%02x", b)) //convert to hex
        }
        return if (16 > result.toString().length) {
            result.toString()
        } else {
            result.toString().substring(0, 16)
        }
    }



}