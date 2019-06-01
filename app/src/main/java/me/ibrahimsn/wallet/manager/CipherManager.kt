package me.ibrahimsn.wallet.manager

import android.annotation.TargetApi
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.UserNotAuthenticatedException
import me.ibrahimsn.wallet.util.FileUtil
import me.ibrahimsn.wallet.util.FileUtil.getFilePath
import me.ibrahimsn.wallet.util.ServiceErrorException
import me.ibrahimsn.wallet.util.ServiceErrorException.Companion.INVALID_KEY
import me.ibrahimsn.wallet.util.ServiceErrorException.Companion.IV_OR_ALIAS_NO_ON_DISK
import me.ibrahimsn.wallet.util.ServiceErrorException.Companion.KEY_IS_GONE
import me.ibrahimsn.wallet.util.ServiceErrorException.Companion.KEY_STORE_ERROR
import me.ibrahimsn.wallet.util.ServiceErrorException.Companion.USER_NOT_AUTHENTICATED
import java.io.*
import java.lang.Exception
import java.security.InvalidKeyException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

@TargetApi(23)
class CipherManager(private val context: Context) {

    private val ANDROID_KEY_STORE = "AndroidKeyStore"
    private val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private val CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    fun put(address: String, password: String) {
        setData(password.toByteArray(), address, address, address + "iv")
    }

    fun get(address: String): ByteArray {
        return getData(address, address, address + "iv")
    }

    @Throws(ServiceErrorException::class)
    private fun setData(data: ByteArray?, alias: String, aliasFile: String, aliasIV: String): Boolean {

        if (data == null)
            throw ServiceErrorException(ServiceErrorException.INVALID_DATA, "Keystore insert data is null!")

        val keyStore: KeyStore

        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)

            if (!keyStore.containsAlias(alias)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
                keyGenerator.init(KeyGenParameterSpec.Builder(
                        alias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(BLOCK_MODE)
                        .setKeySize(256)
                        .setUserAuthenticationRequired(false)
                        .setRandomizedEncryptionRequired(true)
                        .setEncryptionPaddings(PADDING)
                        .build())
                keyGenerator.generateKey()
            }

            val encryptedDataFilePath = getFilePath(context, aliasFile)
            val secretKey = keyStore.getKey(alias, null) ?:
                throw ServiceErrorException(ServiceErrorException.KEY_STORE_SECRET, "Secret is null on setData: $alias")

            val inCipher = Cipher.getInstance(CIPHER_ALGORITHM)
            inCipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = inCipher.iv
            val path = getFilePath(context, aliasIV)

            if (!FileUtil.writeBytesToFile(path, iv)) {
                keyStore.deleteEntry(alias)
                throw ServiceErrorException(ServiceErrorException.FAIL_TO_SAVE_IV_FILE, "Failed to save the iv file for: $alias")
            }

            var cipherOutputStream: CipherOutputStream? = null
            try {
                cipherOutputStream = CipherOutputStream(FileOutputStream(encryptedDataFilePath), inCipher)
                cipherOutputStream.write(data)
            } catch (e: Exception) {
                throw ServiceErrorException(KEY_STORE_ERROR, "Failed to save the file for: $alias")
            } finally {
                cipherOutputStream?.close()
            }

            return true
        } catch (e: UserNotAuthenticatedException) {
            throw ServiceErrorException(USER_NOT_AUTHENTICATED)
        } catch (e: Exception) {
            e.printStackTrace()
            throw ServiceErrorException(KEY_STORE_ERROR)
        }
    }

    @Throws(ServiceErrorException::class)
    private fun getData(alias: String, aliasFile: String, aliasIV: String): ByteArray {

        val keyStore: KeyStore
        val encryptedDataFilePath = getFilePath(context, aliasFile)

        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)

            val secretKey = keyStore.getKey(alias, null) ?:
                throw ServiceErrorException(KEY_IS_GONE, "Secret is gone!")

            val ivExists = File(getFilePath(context, aliasIV)).exists()
            val aliasExists = File(getFilePath(context, aliasFile)).exists()

            if (!ivExists || !aliasExists) {
                removeAliasAndFiles(context, alias, aliasFile, aliasIV)

                if (ivExists != aliasExists)
                    throw ServiceErrorException(IV_OR_ALIAS_NO_ON_DISK, "file is present but the key is gone: $alias")
                else
                    throw ServiceErrorException(IV_OR_ALIAS_NO_ON_DISK, "!ivExists && !aliasExists: $alias")
            }

            val iv = FileUtil.readBytesFromFile(getFilePath(context, aliasIV)) ?: throw NullPointerException("iv is missing for $alias")

            val outCipher = Cipher.getInstance(CIPHER_ALGORITHM)
            outCipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            val cipherInputStream = CipherInputStream(FileInputStream(encryptedDataFilePath), outCipher)
            return FileUtil.readBytesFromStream(cipherInputStream)
        } catch (e: InvalidKeyException) {
            if (e is UserNotAuthenticatedException) {
                throw ServiceErrorException(USER_NOT_AUTHENTICATED)
            } else {
                throw ServiceErrorException(INVALID_KEY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw ServiceErrorException(KEY_STORE_ERROR)
        }
    }

    private fun removeAliasAndFiles(context: Context, alias: String, dataFileName: String, ivFileName: String) {
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            keyStore.deleteEntry(alias)
            File(getFilePath(context, dataFileName)).delete()
            File(getFilePath(context, ivFileName)).delete()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}