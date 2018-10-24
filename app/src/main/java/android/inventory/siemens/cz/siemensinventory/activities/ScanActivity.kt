package android.inventory.siemens.cz.siemensinventory.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.api.entity.Device
import android.inventory.siemens.cz.siemensinventory.device.DeviceServiceApi
import android.inventory.siemens.cz.siemensinventory.entity.enums.ScanIntent
import android.inventory.siemens.cz.siemensinventory.tools.SnackBarNotifier
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.Gson
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private var deviceApi : DeviceServiceApi = DeviceServiceApi.Factory.create(this)
    private val MY_CAMERA_REQUEST_CODE = 100
    private var parameterName: String? = null
    private var scanIntent = ScanIntent.Device
    private var snackBarNotifier: SnackBarNotifier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        }

        parameterName = getParameterName()
        scanIntent = getScanIntent()
        if (parameterName.isNullOrEmpty()) {
            throw IllegalArgumentException("Variable 'parameterName' is not specified.")
        }

        initScanner()
    }

    private fun initScanner() {
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
        val scanView = mScannerView
        scanView?.setResultHandler(this)
        scanView?.startCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScanner()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun handleResult(rawResult: Result) {
        val deviceBarcodeId = rawResult.text
        if(scanIntent == ScanIntent.Barcode) {
            finishWithValue(deviceBarcodeId)
        }

        if(deviceBarcodeId != null && deviceBarcodeId.isNotEmpty()) {
            val queue = deviceApi.getDeviceByBarcodeId(deviceBarcodeId)
            queue.enqueue(object : Callback<Device> {
                override fun onResponse(call: Call<Device>?, response: Response<Device>?) {
                    if(response?.isSuccessful == true) {
                        intent.putExtra(parameterName, Gson().toJson(response.body()))
                    } else {
                        //todo finish how to handle other responses like 404 Not found
                        intent.putExtra(parameterName, "")
                    }
                    intent.putExtra("barcode", deviceBarcodeId)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                override fun onFailure(call: Call<Device>?, t: Throwable?) {
                    finishWithValue("")
                }
            })
        } else {
            snackBarNotifier?.show(getString(R.string.unable_to_scan))
        }
    }

    private fun finishWithValue(value: String) {
        intent.putExtra(parameterName, value)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun getParameterName() : String {
        val parameterName = intent.getStringExtra("parameterName")
        if (parameterName.isNullOrEmpty()) {
            throw IllegalArgumentException("Variable 'parameterName' is not specified.")
        }
        return parameterName
    }

    private fun getScanIntent(): ScanIntent {
        val intentString = intent.getStringExtra("intent")
        if (intentString == null) {
            return ScanIntent.Device
        }
        return ScanIntent.valueOf(intentString) ?: ScanIntent.Device
    }
}
