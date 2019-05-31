package me.ibrahimsn.wallet.util

class ServiceErrorException @JvmOverloads constructor(val code: Int,
                                                      message: String? = null,
                                                      throwable: Throwable? = null) : Exception(message, throwable) {
    companion object {
        val UNKNOWN_ERROR = -1
        val INVALID_DATA = 1
        val KEY_STORE_ERROR = 1001
        val FAIL_TO_SAVE_IV_FILE = 1002
        val KEY_STORE_SECRET = 1003
        val USER_NOT_AUTHENTICATED = 1004
        val KEY_IS_GONE = 1005
        val IV_OR_ALIAS_NO_ON_DISK = 1006
        val INVALID_KEY = 1007
    }
}