package ir.baha2r.m98.crypto.aesfilemanager

import java.io.*
import java.util.*

class FileManager private constructor() {

    @Throws(IOException::class)
    private fun creatFile(directory: File, fileName: String): File? {
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    @Throws(IOException::class)
    private fun creatBinaryFile(directory: File, fileName: String): File? {
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    @Throws(IOException::class)
    private fun readInnerBinaryFile(file: File): String {
        val fis = FileInputStream(file)
        val ois = ObjectInputStream(fis)
        val size = ois.available()
        val bytes = ByteArray(size)
        for (i in 0 until size) {
            bytes[i] = ois.readByte()
        }
        val buff = MakeVisible.show(bytes)
        ois.close()
        fis.close()
        return String(buff)
    }

    @Throws(IOException::class)
    private fun writeBinaryFile(directory: File, fileName: String, text: String): File {
        val file = creatBinaryFile(directory, fileName)!!
        val fos = FileOutputStream(file)
        val os = ObjectOutputStream(fos)
        os.write(text.toByteArray())
        os.flush()
        os.close()
        fos.flush()
        fos.close()
        return file
    }

    @Throws(IOException::class)
    private fun readBinaryFile(file: File): String {
        val fis = FileInputStream(file)
        val ois = ObjectInputStream(fis)
        val size = ois.available()
        val buff = ByteArray(size)
        for (i in 0 until size) {
            buff[i] = ois.readByte()
        }
        ois.close()
        fis.close()
        return String(buff)
    }

    @Throws(IOException::class)
    private fun writeFile(directory: File, fileName: String, text: String): File {
        val file = creatFile(directory, fileName)!!
        val fw = FileWriter(file)
        fw.write(text)
        fw.flush()
        fw.close()
        return file
    }

    @Throws(FileNotFoundException::class)
    private fun readFile(file: File): String {
        val reader = Scanner(file)
        val textBuilder = StringBuilder()
        while (reader.hasNextLine()) {
            textBuilder.append(reader.nextLine())
        }
        return textBuilder.toString()
    }

    private fun isTextFile(file: File): Boolean {
        return file.name.contains(".txt")
    }

    private fun isBinaryFile(file: File): Boolean {
        return file.name.contains(".bin")
    }
}
