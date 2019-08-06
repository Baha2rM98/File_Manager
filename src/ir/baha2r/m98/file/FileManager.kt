
/**
 * A class to complete java.io.File @suppress java.io.File
 **/

package ir.baha2r.m98.file

import java.io.*
import java.util.*

/**
 * @author Baha2r
 * @constructor prepare this class through inherited class
 **/

class FileManager : File(File::class.java.toString()) {

    /**
     * string for files's suffixes
     **/

    companion object {
        private const val binarySuffix = ".bin"
        private const val txtSuffix = ".txt"
    }


    /**
     * this method creates a empty text file for next operations
     * @param directory directory for main file
     * @param fileName name of file it gonna create (suffixes will handle automatically)
     * @return File
     **/

    @Throws(IOException::class)
    private fun creatFile(directory: File, fileName: String): File? {
        var name = fileName
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists())
            directory.mkdirs()
        name += txtSuffix
        val file = File(directory, name)
        if (!file.exists())
            file.createNewFile()
        return file
    }

    /**
     * this method creates a empty binary file for next operations
     * @param directory directory for main file
     * @param fileName name of file it gonna create (suffixes will handle automatically)
     * @return File
     **/

    @Throws(IOException::class)
    private fun creatBinaryFile(directory: File, fileName: String): File? {
        var name = fileName
        if (!directory.isDirectory) {
            System.err.println("This is not a directory!")
            return null
        }
        if (!directory.exists())
            directory.mkdirs()
        name += binarySuffix
        val file = File(directory, name)
        if (!file.exists())
            file.createNewFile()
        return file
    }

    /**
     * this method writes data's from another file to new file
     * @param file destination file
     * @param text text
     **/

    @Throws(IOException::class)
    private fun pushFile(file: File, text: String) {
        val fw = FileWriter(file, true)
        fw.write(text)
        fw.flush()
        fw.close()
    }

    /**
     * this method writes data's from another file to new file
     * @param file destination file
     * @param text text
     **/

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

    /**
     * this method reads data's from binary file
     * @param file source file will be read
     * @return read file in a string
     **/

    @Throws(IOException::class)
    fun readBinaryFile(file: File): String {
        if (isTextFile(file)!!) {
            return "NOTICE: this is not a binary file!"
        }
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

    /**
     * this method reads data's from text file
     * @param file source file will be read
     * @return read file in a string
     **/

    @Throws(FileNotFoundException::class)
    fun readFile(file: File): String {
        if (isBinaryFile(file)!!) {
            return "NOTICE: this is not a text file!"
        }
        val reader = Scanner(file)
        val textBuilder = StringBuilder()
        while (reader.hasNextLine()) {
            textBuilder.append(reader.nextLine())
        }
        return textBuilder.toString()
    }

    /**
     * this method writes data's to binary file
     * @param directory directory of source file
     * @param fileName {@String} file name
     * @param text string data
     **/

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

    /**
     * this method writes data's to text file
     * @param directory directory of source file
     * @param fileName {@String} file name
     * @param text string data
     **/

    @Throws(IOException::class)
    fun writeDataToFile(directory: File, fileName: String, text: String) {
        val file = creatFile(directory, fileName)!!
        val fw = FileWriter(file)
        fw.write(text)
        fw.flush()
        fw.close()
    }

    /**
     * this method checks if file is txt or not
     * @param file original file
     * @return boolean result
     **/

    @Throws(FileNotFoundException::class)
    fun isTextFile(file: File): Boolean? {
        if (file is Nothing) {
            System.err.println("file not found!")
            return null
        }
        return file.name.contains(txtSuffix)
    }

    /**
     * this method checks if file is binary or not
     * @param file original file
     * @return boolean result
     **/

    @Throws(FileNotFoundException::class)
    fun isBinaryFile(file: File): Boolean? {
        if (file is Nothing) {
            System.err.println("file not found!")
            return null
        }
        return file.name.contains(binarySuffix)
    }

    /**
     * this method checks if file is empty or not
     * @param file original file
     * @return boolean result
     **/

    @Throws(IOException::class)
    fun isFileEmpty(file: File): Boolean {
        if (isTextFile(file)!!) {
            val text = readFile(file)
            return text == ""
        }
        if (isBinaryFile(file)!!) {
            val text = readBinaryFile(file)
            return text == ""
        }
        return false
    }

    /**
     * this method copies data's from source file to destination file
     * @param source source file
     * @param destination des file
     **/

    @Throws(IOException::class)
    fun copy(source: File, destination: File) {
        val text: String
        if (isTextFile(source)!! && isTextFile(destination)!!) {
            text = readFile(source)
            pushFile(destination, text)
            return
        }
        if (isBinaryFile(source)!! && isBinaryFile(destination)!!) {
            text = readBinaryFile(source)
            pushBinaryFile(destination, text)
            return
        }
        if (isTextFile(source)!! && isBinaryFile(destination)!!) {
            text = readFile(source)
            pushBinaryFile(destination, text)
            return
        }
        if (isBinaryFile(source)!! && isTextFile(destination)!!) {
            text = readBinaryFile(source)
            pushFile(destination, text)
        }
    }
}