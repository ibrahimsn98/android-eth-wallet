package me.ibrahimsn.wallet.util

import android.content.Context
import java.io.*

object FileUtil {

    @Synchronized
    fun getFilePath(context: Context, fileName: String): String {
        return File(context.filesDir, fileName).absolutePath
    }

    fun writeBytesToFile(path: String, data: ByteArray): Boolean {
        var fos: FileOutputStream? = null

        try {
            val file = File(path)
            fos = FileOutputStream(file)
            fos.write(data)
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return false
    }

    fun readBytesFromFile(path: String): ByteArray? {
        var bytes: ByteArray? = null
        val fin: FileInputStream

        try {
            val file = File(path)
            fin = FileInputStream(file)
            bytes = readBytesFromStream(fin)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bytes
    }

    fun readBytesFromStream(input: InputStream): ByteArray {

        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        try {
            var len = input.read(buffer)

            while (len != -1) {
                byteBuffer.write(buffer, 0, len)
                len = input.read(buffer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                byteBuffer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return byteBuffer.toByteArray()
    }
}