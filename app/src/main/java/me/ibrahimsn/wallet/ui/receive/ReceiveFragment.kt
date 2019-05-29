package me.ibrahimsn.wallet.ui.receive

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_receive.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity

class ReceiveFragment : BaseFragment<HomeActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_receive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivBarcode.setImageBitmap(createQRImage("0x4353453"))
    }

    private fun createQRImage(address: String): Bitmap? {
        val size = Point()
        activity.windowManager.defaultDisplay.getSize(size)
        val imageSize = (size.x * 0.9f).toInt()
        try {
            val bitMatrix = MultiFormatWriter().encode(
                    address,
                    BarcodeFormat.QR_CODE,
                    imageSize,
                    imageSize, null)
            val barcodeEncoder = BarcodeEncoder()
            return barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: Exception) {
            Toast.makeText(activity, "Fail barcode generate!", Toast.LENGTH_SHORT).show()
        }

        return null
    }
}