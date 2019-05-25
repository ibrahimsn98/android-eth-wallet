package me.ibrahimsn.wallet.entity

data class Transaction (val timeStamp: String,
                        val hash: String,
                        val nonce: String,
                        val from: String,
                        val to: String,
                        val value: String,
                        val gas: String,
                        val gasPrice: String,
                        val isError: String,
                        val gasUsed: String,
                        val confirmations: String)