package ir.baha2r.m98.crypto.aes

//TODO: add comments
internal interface Holder {
    companion object {
        internal val IV: ByteArray = byteArrayOf(19, -53, -47, -48, -75, 18, 13, 19, 19, 15, 19, -55, -56, 10, 19, 9)
        internal val KEY: ByteArray =
            byteArrayOf(-55, 19, -51, -66, -63, -5, -2, 19, -47, -53, 19, -51, -53, -1, -75, -10)
    }
}