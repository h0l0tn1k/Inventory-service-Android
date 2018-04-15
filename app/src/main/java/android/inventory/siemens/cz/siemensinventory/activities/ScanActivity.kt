package android.inventory.siemens.cz.siemensinventory.activities

import android.Manifest
import android.app.Activity
import android.inventory.siemens.cz.siemensinventory.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.gson.Gson


class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private val MY_CAMERA_REQUEST_CODE = 100
    private var parameterName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
        }

        parameterName = intent.getStringExtra("parameterName")
        if(parameterName.isNullOrEmpty()) {
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
        intent.putExtra(parameterName, rawResult.text)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
