package ir.baha2r.m98.crypto.aesfilemanager

//TODO: add comments
internal object MakeVisible {
    private const val mul: Byte = -1
    private const val plus: Byte = 51

    internal fun show(bytes: ByteArray): ByteArray {
        val size = bytes.size
        val buf = ByteArray(size)
        for (i in 0 until size) {
            buf[i] = (bytes[i] * mul + plus).toByte()
        }
        return buf
    }
}
