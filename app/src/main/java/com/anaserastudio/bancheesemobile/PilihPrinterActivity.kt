package com.anaserastudio.bancheesemobile

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.widget.LinearLayout
import com.anaserastudio.bancheesemobile.adapter.BluetoothPrinter
import com.anaserastudio.bancheesemobile.tool.PrinterControl
import kotlinx.android.synthetic.main.activity_pilih_printer.*
import java.io.IOException
import java.util.*

class PilihPrinterActivity : BaseActivity(), Runnable {

    private val TAG = "PilihPrinterActivity"
    private var namaPrinter : String? = null
    private var idPrinter : String? = null

    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var mBluetoothSocket: BluetoothSocket
    private lateinit var mBluetoothDevice: BluetoothDevice
    internal lateinit var mPrinter: BluetoothPrinter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_printer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Printer"
        init()
    }

    private fun init(){
        checkStatusPrinter()

        btn_pilih_printer.setOnClickListener {
            intentSelectPrinter()
        }

        btn_test_print.setOnClickListener {
            PrinterControl(this@PilihPrinterActivity).testPrint()
        }
    }

    private fun intentSelectPrinter(){
        val intent = Intent(this@PilihPrinterActivity, DaftarPrinterActivity::class.java)
        startActivityForResult(intent,1001)
    }

    private fun checkStatusPrinter(){
        namaPrinter = pref.getString(getString(R.string.nama_printer),null)
        idPrinter = pref.getString(getString(R.string.id_printer),null)
        if (namaPrinter == null){
            layout_no_printer.visibility = LinearLayout.VISIBLE
            layout_printer.visibility = ConstraintLayout.GONE
        }else{
            layout_no_printer.visibility = LinearLayout.GONE
            layout_printer.visibility = ConstraintLayout.VISIBLE

            text_nama_printer.text = pref.getString(getString(R.string.nama_printer),null)
            text_id_printer.text = pref.getString(getString(R.string.id_printer),null)
            btn_ubah_printer.setOnClickListener {
                intentSelectPrinter()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1002){
            setToast("Printer diubah")
            checkStatusPrinter()
        }
    }

    private fun closeSocket(nOpenSocket: BluetoothSocket) {
        try {
            nOpenSocket.close()
            Log.d(TAG, "SocketClosed")
        } catch (ex: IOException) {
            Log.d(TAG, "CouldNotCloseSocket")
        }
    }

    override fun run() {
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID)
            mBluetoothAdapter.cancelDiscovery()
            mBluetoothSocket.connect()
        } catch (eConnectException: IOException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException)
            closeSocket(mBluetoothSocket)
            return
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}
