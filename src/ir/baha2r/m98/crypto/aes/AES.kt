package ir.baha2r.m98.crypto.aes

import ir.baha2r.m98.crypto.aesfilemanager.FileManager
import ir.baha2r.m98.crypto.aesfilemanager.MakeVisible
import java.io.File
import java.io.IOException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AES
/**
 * This constructor is used for prepared Key and ivVector thad are already exist
 */ @Throws(IOException::class) constructor() : FileManager() {
    private val ALGORITHM = "AES/CBC/PKCS5PADDING"
    private lateinit var KEY: SecretKeySpec
    private lateinit var IV: IvParameterSpec

    init {
        IV = IvParameterSpec(ivReader())
        KEY = SecretKeySpec(keyReader(), "AES")
    }

    @Throws(IOException::class)
    private fun ivReader(): ByteArray {
        val bytes: ByteArray = Holder.IV
        return MakeVisible.show(bytes)
    }

    @Throws(IOException::class)
    private fun keyReader(): ByteArray {
        val bytes: ByteArray = Holder.KEY
        return MakeVisible.show(bytes)
    }

    private fun encryption(plainText: String): String? {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, KEY, IV)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            return Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun decryption(cipherText: String): String? {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, KEY, IV)
            val original = cipher.doFinal(Base64.getDecoder().decode(cipherText))
            return String(original)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(IOException::class)
    fun fileEncryption(directory: File, fileName: String): File? {
        var fileName = fileName
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
        if (file == null) {
            System.err.println("There is no file with this information in this directory!")
            return null
        }
        val text: String
        if (isTextFile(file)) {
            text = readFile(file)
            if (text == "") {
                System.err.println("file is empty!")
                return null
            }
            fileName = "Encrypted_$fileName"
            println("Your file is encrypted now!\n")
            val EF = writeFile(directory, fileName, encryption(text)!!)
            EF.setReadOnly()
            return EF
        }
        if (file.name.contains(".bin")) {
            text = readBinaryFile(file)
            if (text == "") {
                System.err.println("file is empty!")
                return null
            }
            fileName = "Encrypted_$fileName"
            println("Your file is encrypted now!\n")
            val EF = writeBinaryFile(directory, fileName, encryption(text)!!)
            EF.setReadOnly()
            return EF
        }
        return null
    }

    @Throws(IOException::class)
    fun fileDecryption(directory: File, fileName: String): File? {
        var fileName = fileName
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
        if (encryptedFile == null) {
            System.err.println("There is no file with this information in this directory!")
            return null
        }
        val encrypted: String
        if (isTextFile(encryptedFile)) {
            encrypted = readFile(encryptedFile)
            if (encrypted == "") {
                System.err.println("file is empty!")
                return null
            }
            val finalName = "Decrypted_$tempName"
            println("Your file is decrypted now!\n")
            return writeFile(directory, finalName, decryption(encrypted)!!)
        }
        if (isBinaryFile(encryptedFile)) {
            encrypted = readBinaryFile(encryptedFile)
            if (encrypted == "") {
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