package me.ibrahimsn.wallet.ui.send.send

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_send.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.ui.send.SendActivity
import me.ibrahimsn.wallet.ui.send.SendViewModel
import me.ibrahimsn.wallet.util.BalanceUtil
import me.ibrahimsn.wallet.util.Constants
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.regex.Pattern
import javax.inject.Inject

class SendFragment : BaseFragment<SendActivity>(), SeekBar.OnSeekBarChangeListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SendViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_send
    }

    private var currentWallet: Wallet? = null

    private val gasLimitMin = Constants.GAS_LIMIT_MIN
    private val gasLimitMax = Constants.GAS_LIMIT_MAX
    private val networkFeeMax = BigInteger.valueOf(Constants.NETWORK_FEE_MAX)
    private val gasPriceMinGwei = BalanceUtil.weiToGweiBI(BigInteger.valueOf(Constants.GAS_PRICE_MIN)).toInt()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(SendViewModel::class.java)
        activity.setTitle("Send Ethereum")

        sbGasPrice.setOnSeekBarChangeListener(this)
        sbGasLimit.setOnSeekBarChangeListener(this)

        renderScreen()

        sbGasPrice.max = BalanceUtil
                .weiToGweiBI(networkFeeMax.divide(gasLimitMax.toBigInteger()))
                .subtract(BigDecimal.valueOf(gasPriceMinGwei.toLong()))
                .toInt()

        sbGasPrice.progress = BalanceUtil
                .weiToGweiBI(viewModel.gasSettings.gasPrice)
                .subtract(BigDecimal.valueOf(gasPriceMinGwei.toLong()))
                .toInt()

        sbGasLimit.max = (gasLimitMax - gasLimitMin).toInt()
        sbGasLimit.progress = (viewModel.gasSettings.gasLimit - gasLimitMin).toInt()

        btSend.setOnClickListener {
            if (currentWallet != null && currentWallet!!.isWallet) {
                val receiver = etReceiverAddress.text.toString().trim()
                val amount = etAmount.text.toString().trim()

                if (validateForm(receiver, amount)) {
                    viewModel.createTransaction(currentWallet!!, receiver, BalanceUtil.baseToSubunit(amount))
                    activity.navController.navigate(R.id.action_sendFragment_to_confirmFragment)
                }
            } else
                Toast.makeText(activity, "You can't create transaction with this wallet.", Toast.LENGTH_SHORT).show()
        }

        viewModel.currentWallet.observe(this, Observer {
            currentWallet = it
        })
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar!!.id == R.id.sbGasPrice)
            viewModel.gasSettings.gasPrice = BalanceUtil.gweiToWei(BigDecimal.valueOf((progress + gasPriceMinGwei).toLong()))
        else if (seekBar.id == R.id.sbGasLimit)
            viewModel.gasSettings.gasLimit = BigInteger.valueOf(progress.toLong() + gasLimitMin).toLong()

        renderScreen()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    private fun validateForm(address: String, amount: String): Boolean {
        if (!Pattern.matches("^0x[a-fA-F0-9]{40}\$", address)) {
            Toast.makeText(activity, "Please enter a valid ethereum address.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Pattern.matches("^\\d+(\\.\\d+)?\$", amount) || amount.toDouble() <= 0) {
            Toast.makeText(activity, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun renderScreen() {
        tvGasPrice.text = StringBuilder()
                .append(BalanceUtil.weiToGwei(viewModel.gasSettings.gasPrice))
                .append(" Gwei")
                .toString()

        tvGasLimit.text = StringBuilder()
                .append(viewModel.gasSettings.gasLimit)
                .toString()

        tvNetworkFee.text = StringBuilder()
                .append(BalanceUtil
                .weiToEth(viewModel.gasSettings.gasPrice
                        .multiply(viewModel.gasSettings.gasLimit.toBigInteger())))
                .append(" ETH")
                .toString()
    }
}