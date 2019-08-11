package ir.baha2r.m98.crypto.aes

import ir.baha2r.m98.crypto.aes.Holder.Companion.IV
import ir.baha2r.m98.crypto.aes.Holder.Companion.KEY
import ir.baha2r.m98.crypto.aesfilemanager.AESFileManager
import ir.baha2r.m98.crypto.aesfilemanager.MakeVisible
import java.io.File
import java.io.IOException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Baha2r
 * **/
//TODO: add comments
class AES
@Throws(IOException::class) constructor() : AESFileManager(), Holder {
    private val algorithm = "AES/CBC/PKCS5PADDING"
    private var key: SecretKeySpec
    private var iv: IvParameterSpec

    init {
        iv = IvParameterSpec(ivReader())
        key = SecretKeySpec(keyReader(), "AES")
    }

    @Throws(IOException::class)
    private fun ivReader(): ByteArray {
        val bytes: ByteArray = IV
        return MakeVisible.show(bytes)
    }

    @Throws(IOException::class)
    private fun keyReader(): ByteArray {
        val bytes: ByteArray = KEY
        return MakeVisible.show(bytes)
    }

    private fun encryption(plainText: String): String? {
        try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, key, iv)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            return Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun decryption(cipherText: String): String? {
        try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
            val original = cipher.doFinal(Base64.getDecoder().decode(cipherText))
            return String(original)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(IOException::class)
    fun fileEncryption(directory: File, originalFileName: String): File? {
        var fileName = originalFileName
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        var file: File? = null
        val files = directory.listFiles()!!
        for (value in files) {
            if (value.name == fileName) {
                file = value
                break
            }
        }
        if (file === null) {
            System.err.println("There is no file with this information in this directory!")
            return null
        }
        val text: String
        if (isTextFile(file)) {
            text = readFile(file)
            if (text === "") {
                System.err.println("file is empty!")
                return null
            }
            fileName = "Encrypted_$fileName"
            println("Your file is encrypted now!\n")
            val ef = writeFile(directory, fileName, encryption(text)!!)
            ef.setReadOnly()
            return ef
        }
        if (file.name.contains(".bin")) {
            text = readBinaryFile(file)
            if (text === "") {
                System.err.println("file is empty!")
                return null
            }
            fileName = "Encrypted_$fileName"
            println("Your file is encrypted now!\n")
            val ef = writeBinaryFile(directory, fileName, encryption(text)!!)
            ef.setReadOnly()
            return ef
        }
        return null
    }

    @Throws(IOException::class)
    fun fileDecryption(directory: File, originalFileName: String): File? {
        var fileName = originalFileName
        val tempName = fileName
        fileName = "Encrypted_$fileName"
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        var encryptedFile: File? = null
        val files = directory.listFiles()!!
        for (value in files) {
            if (value.name == fileName) {
                encryptedFile = value
                break
            }
        }
        if (encryptedFile === null) {
            System.err.println("There is no file with this information in this directory!")
            return null
        }
        val encrypted: String
        if (isTextFile(encryptedFile)) {
            encrypted = readFile(encryptedFile)
            if (encrypted === "") {
                System.err.println("file is empty!")
                return null
            }
            val finalName = "Decrypted_$tempName"
            println("Your file is decrypted now!\n")
            return writeFile(directory, finalName, decryption(encrypted)!!)
        }
        if (isBinaryFile(encryptedFile)) {
            encrypted = readBinaryFile(encryptedFile)
            if (encrypted === "") {
                System.err.println("file is empty!")
                return null
            }
            val finalName = "Decrypted_$tempName"
            println("Your file is decrypted now!\n")
            return writeFile(directory, finalName, decryption(encrypted)!!)
        }
        return null
    }
}