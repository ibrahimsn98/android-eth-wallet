package me.ibrahimsn.wallet.entity

data class NetworkInfo (val name: String,
                        val symbol: String,
                        val rpcServerUrl: String,
                        val backendUrl: String,
                        val etherScanUrl: String,
                        val chainId: Int,
                        val isMainNetwork: Boolean)