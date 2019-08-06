package ir.baha2r.m98.file

import java.io.*
import java.util.*

/**
 * @author Baha2r
 */

//TODO : comment for methods - library as jar file - think about some new features :)
class FileManager : File(File::class.java.toString()) {

    private val BinarySuffix = ".bin"
    private val TXTSuffix = ".txt"

    @Throws(IOException::class)
    private fun creatFile(directory: File, fileName: String): File? {
        var fileName = fileName
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists())
            directory.mkdirs()
        fileName += TXTSuffix
        val file = File(directory, fileName)
        if (!file.exists())
            file.createNewFile()
        return file
    }

    @Throws(IOException::class)
    private fun creatBinaryFile(directory: File, fileName: String): File? {
        var fileName = fileName
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists())
            directory.mkdirs()
        fileName += BinarySuffix
        val file = File(directory, fileName)
        if (!file.exists())
            file.createNewFile()
        return file
    }

    @Throws(IOException::class)
    private fun pushFile(file: File, text: String) {
        val fw = FileWriter(file, true)
        fw.write(text)
        fw.flush()
        fw.close()
    }

    @Throws(IOException::class)
    private fun pushBinaryFile(file: File, text: String) {
        val fos = FileOutputStream(file, true)
        val os = ObjectOutputStream(fos)
        os.write(text.toByteArray())
        os.flush()
        os.close()
        fos.flush()
        fos.close()
    }

    @Throws(IOException::class)
    fun readBinaryFile(file: File): String {
        val fis = FileInputStream(file)
        val ois = ObjectInputStream(fis)
        val size = ois.available()
        val bytes = ByteArray(size)
        for (i in 0 until size) {
            bytes[i] = ois.readByte()
        }
        ois.close()
        fis.close()
        return String(bytes)
    }

    @Throws(FileNotFoundException::class)
    fun readFile(file: File): String {
        val reader = Scanner(file)
        val textBuilder = StringBuilder()
        while (reader.hasNextLine()) {
            textBuilder.append(reader.nextLine())
        }
        return textBuilder.toString()
    }

    @Throws(IOException::class)
    fun writeDataToBinaryFile(directory: File, fileName: String, text: String) {
        val file = creatBinaryFile(directory, fileName)!!
        val fos = FileOutputStream(file)
        val os = ObjectOutputStream(fos)
        os.write(text.toByteArray())
        os.flush()
        os.close()
        fos.flush()
        fos.close()
    }

    @Throws(IOException::class)
    fun writeDataToFile(directory: File, fileName: String, text: String) {
        val file = creatFile(directory, fileName)!!
        val fw = FileWriter(file)
        fw.write(text)
        fw.flush()
        fw.close()
    }

    fun isTextFile(file: File): Boolean {
        return file.name.contains(TXTSuffix)
    }

    fun isBinaryFile(file: File): Boolean {
        return file.name.contains(BinarySuffix)
    }

    @Throws(IOException::class)
    fun isFileEmpty(file: File): Boolean {
        if (isTextFile(file)) {
            val text = readFile(file)
            return text == ""
        }
        if (isBinaryFile(file)) {
            val text = readBinaryFile(file)
            return text == ""
        }
        return false
    }

    @Throws(IOException::class)
    fun copy(source: File, destination: File) {
        val text: String
        if (isTextFile(source) && isTextFile(destination)) {
            text = readFile(source)
            pushFile(destination, text)
            return
        }
        if (isBinaryFile(source) && isBinaryFile(destination)) {
            text = readBinaryFile(source)
            pushBinaryFile(destination, text)
            return
        }
        if (isTextFile(source) && isBinaryFile(destination)) {
            text = readFile(source)
            pushBinaryFile(destination, text)
            return
        }
        if (isBinaryFile(source) && isTextFile(destination)) {
            text = readBinaryFile(source)
            pushFile(destination, text)
        }
    }
}