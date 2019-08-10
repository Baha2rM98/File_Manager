package ir.baha2r.m98.crypto.aesfilemanager

internal object MakeVisible {
    private val MUL: Byte = -1
    private val PLUS: Byte = 51

    fun show(bytes: ByteArray): ByteArray {
        val size = bytes.size
        val buf = ByteArray(size)
        for (i in 0 until size) {
            buf[i] = (bytes[i] * MUL + PLUS).toByte()
        }
        return buf
    }
}
