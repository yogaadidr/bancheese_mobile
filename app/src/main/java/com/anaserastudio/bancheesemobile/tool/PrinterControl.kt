package com.anaserastudio.bancheesemobile.tool

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.adapter.BluetoothPrinter
import com.anaserastudio.bancheesemobile.model.Menu
import com.anaserastudio.bancheesemobile.model.Transaksi
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.BitmapFactory
import android.graphics.Bitmap



class PrinterControl(val context : Context) : Runnable {


    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var mBluetoothSocket: BluetoothSocket
    private lateinit var mBluetoothDevice: BluetoothDevice
    internal lateinit var mPrinter: BluetoothPrinter
    var pref : SharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_string),0)
    val TAG = "PrinterControl"


    private fun setToast(text : String){
        Toast.makeText(context, text,Toast.LENGTH_SHORT).show()
    }

    fun print(bayar : Int,listMenu : ArrayList<Menu>?, listTransaksi : ArrayList<Transaksi>?){
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        var mBtDevice: BluetoothDevice? = null   // Get first paired device
        try {

            val mPairedDevices = btAdapter.bondedDevices
            if (mPairedDevices.size > 0) {
                val idPrinter = pref.getString(context.getString(R.string.id_printer),null)
                for (mDevice in mPairedDevices) {
                    if (mDevice.address == idPrinter) {
                        mBtDevice = mDevice
                    }
                }
            }
        } catch (e: Exception) {

        }

        if (mBtDevice != null) {
            mPrinter = BluetoothPrinter(mBtDevice)
            mPrinter.connectPrinter(object : BluetoothPrinter.PrinterConnectListener {

                override fun onConnected() {

                    mPrinter.addNewLines(2)

                    mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER)
                    mPrinter.setBold(true)
                    mPrinter.printText("Bancheese")
                    mPrinter.addNewLine()
                    mPrinter.setBold(false)

                    mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER)
                    mPrinter.printText(pref.getString(context.getString(R.string.nama_cabang_login),null))
                    mPrinter.addNewLine()

                    mPrinter.printLine()
                    mPrinter.addNewLine()

                    mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                    mPrinter.printText("Kasir : ${pref.getString(context.getString(R.string.key_nama_login),null)}")
                    mPrinter.addNewLine()

                    mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                    mPrinter.printText(StringControl().dateNow())
                    mPrinter.addNewLine()
                    mPrinter.printLine()
                    mPrinter.addNewLine()

                    var total = 0
                    val pajak = 0.0

                    if (listMenu != null){
                        total = getFromMenu(total,listMenu)
                    }else if (listTransaksi != null){
                        total = getFromTransaksi(total, listTransaksi)
                    }


                    val bayar = bayar


                    mPrinter.printLine()
                    mPrinter.addNewLine()

                    if (pajak > 0) {
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                        mPrinter.printText(String.format("%1$-15s %2$15s", "Pajak $pajak%", "Rp. 100"))
                        mPrinter.addNewLine()
                    }

                    mPrinter.setBold(true)
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                    mPrinter.printText(
                        String.format(
                            "%1$-15s %2$15s",
                            "Total",
                            StringControl().getNumberFormatNoCurr(total)
                        )
                    )
                    mPrinter.setBold(false)
                    mPrinter.addNewLine()
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                    mPrinter.printText(String.format("%1$-15s %2$15s", "Bayar", StringControl().getNumberFormatNoCurr(bayar)))
                    mPrinter.addNewLine()
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
                    mPrinter.printText(
                        String.format(
                            "%1$-15s %2$15s",
                            "Kembalian",
                            StringControl().getNumberFormatNoCurr(bayar - total)
                        )
                    )
                    mPrinter.addNewLines(3)
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER)
                    mPrinter.printText("@bancheesesalatiga")
                    mPrinter.addNewLines(2)
                    mPrinter.printText("** TERIMA KASIH **")
                    mPrinter.addNewLines(4)

                    mPrinter.finish()


                }

                override fun onFailed() {
                    Log.d("BluetoothPrinter", "Conection failed")
                }

            })
        } else {
            setToast("Tidak dapat terhubung ke printer")
        }

    }

    fun testPrint(){
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        var mBtDevice: BluetoothDevice? = null   // Get first paired device
        try {

            val mPairedDevices = btAdapter.bondedDevices
            if (mPairedDevices.size > 0) {
                val idPrinter = pref.getString(context.getString(R.string.id_printer),null)
                for (mDevice in mPairedDevices) {
                    if (mDevice.address == idPrinter) {
                        mBtDevice = mDevice
                    }
                }
            }


        } catch (e: Exception) {

        }

        if (mBtDevice != null) {
            mPrinter = BluetoothPrinter(mBtDevice)
            mPrinter.connectPrinter(object : BluetoothPrinter.PrinterConnectListener {

                override fun onConnected() {

                    mPrinter.addNewLines(2)
                    mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER)
                    mPrinter.printText("** TESTING PRINTER **")
                    mPrinter.addNewLines(2)
                    mPrinter.finish()
                }

                override fun onFailed() {
                    Log.d("BluetoothPrinter", "Conection failed")
                }

            })
        } else {
            setToast("Tidak dapat terhubung ke printer")
        }

    }

    private fun getFromMenu(t : Int, listMenu: ArrayList<Menu>?) : Int{
        var total = t
        for (pen in listMenu!!) {
            var namaData : String
            val subtotal = StringControl().getNumberFormatNoCurr(pen.transaksi.HARGA.toString().replace(".00","").toInt())
            val detail = "${pen.transaksi.QTY} x ${StringControl().getNumberFormatNoCurr(pen.HARGA.toString().replace(".00","").toInt())}"
            total += pen.transaksi.HARGA.toString().replace(".00","").toInt()
            namaData = pen.NAMA_MENU

            if(pen.NAMA_MENU.length >= 17){
                namaData = namaData.substring(0,16)
            }

            mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
            mPrinter.setBold(true)
            mPrinter.printText(String.format("%1$-15s %2$15s", namaData, subtotal))
            mPrinter.addNewLine()
            mPrinter.setBold(false)
            mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
            mPrinter.printText(detail)
            mPrinter.addNewLine()
        }
        return total
    }

    private fun getFromTransaksi(t : Int, listTransaksi: ArrayList<Transaksi>?) : Int{
        var total = t
        for (pen in listTransaksi!!) {
            var namaData : String
            val subtotal = StringControl().getNumberFormatNoCurr(pen.HARGA.toString().replace(".00","").toInt())
            val detail = "${pen.QTY} x ${StringControl().getNumberFormatNoCurr(pen.HARGA.toString().replace(".00","").toInt())}"
            total += pen.HARGA.toString().replace(".00","").toInt()
            namaData = pen.NAMA_MENU

            if(pen.NAMA_MENU.length >= 17){
                namaData = namaData.substring(0,16)
            }

            mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
            mPrinter.setBold(true)
            mPrinter.printText(String.format("%1$-15s %2$15s", namaData, subtotal))
            mPrinter.addNewLine()
            mPrinter.setBold(false)
            mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT)
            mPrinter.printText(detail)
            mPrinter.addNewLine()
        }
        return total
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
}