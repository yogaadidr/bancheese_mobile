package com.anaserastudio.bancheesemobile

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anaserastudio.bancheesemobile.adapter.ListPrinter
import com.anaserastudio.bancheesemobile.model.Printer
import kotlinx.android.synthetic.main.activity_daftar_printer.*
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class DaftarPrinterActivity : BaseActivity() {
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var socket: BluetoothSocket
    lateinit var bluetoothDevice: BluetoothDevice
    lateinit var outputStream: OutputStream
    lateinit var inputStream: InputStream

    lateinit var adapter: ListPrinter
    lateinit var lv: ListView

    var namaPrinter = ArrayList<String>()
    var idPrinter = ArrayList<String>()
    var nomorPrinter = ArrayList<Int>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_printer)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_close_black_18dp)
        supportActionBar!!.title = "Daftar Perangkat"

        lv = findViewById(R.id.listDevice)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_printer)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            initPrinter()
        })
        initPrinter()

    }

    private fun initPrinter() {
        namaPrinter = ArrayList()
        idPrinter = ArrayList()
        nomorPrinter = ArrayList()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        text_printer_not_found.visibility = TextView.GONE
        progressBar.visibility = ProgressBar.VISIBLE

        try {
            if (!bluetoothAdapter.isEnabled) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(enableBluetooth)
            }

            val pairedDevices = bluetoothAdapter.bondedDevices

            if (pairedDevices.size > 0) {
                var no = 1
                for (device in pairedDevices) {
                    namaPrinter.add(device.name)
                    idPrinter.add(device.address)
                    nomorPrinter.add(no)
                    no++
                }

                progressBar.visibility = ProgressBar.GONE
                adapter = ListPrinter(this@DaftarPrinterActivity, namaPrinter,idPrinter,nomorPrinter)
                lv.adapter = adapter
                lv.setOnItemClickListener { adapterView, view, i, l ->
                    val edit = pref.edit()
                    edit.putString(getString(R.string.nama_printer), namaPrinter[i])
                    edit.putString(getString(R.string.id_printer), idPrinter[i])
                    edit.apply()
                    edit.commit()
                    val returnIntent = Intent()
                    setResult(1002, returnIntent)
                    finish()
                }

                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") //Standard SerialPortService ID
                val m = bluetoothDevice.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType!!)
                socket = m.invoke(bluetoothDevice, 1) as BluetoothSocket
                bluetoothAdapter.cancelDiscovery()
                socket.connect()
                outputStream = socket.outputStream
                inputStream = socket.inputStream
                //beginListenForData();
                text_printer_not_found.visibility = TextView.GONE
                progressBar.visibility = ProgressBar.GONE
                swipeRefreshLayout.isRefreshing = false

            } else {
                text_printer_not_found.visibility = TextView.VISIBLE
                progressBar.visibility = ProgressBar.GONE
                swipeRefreshLayout.isRefreshing = false

                return
            }

        } catch (ex: Exception) {
//            text_printer_not_found.visibility = TextView.VISIBLE
            progressBar.visibility = ProgressBar.GONE
            swipeRefreshLayout.isRefreshing = false

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val returnIntent = Intent()
        setResult(103, returnIntent)
        finish()
        return true
    }
}
