package com.anaserastudio.bancheesemobile

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.anaserastudio.bancheesemobile.model.Cabang
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestCabang
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registrasi_device.*
import org.json.JSONObject

class RegistrasiDevice : BaseActivity() {
    var idCabang = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi_device)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Daftar Outlet"
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1002){
            init()
        }
    }

    private fun init(){
        getDevice()
        btn_device_pilih.setOnClickListener {
            val intent = Intent(this@RegistrasiDevice, DaftarCabang::class.java)
            startActivityForResult(intent,1001)
        }
        btn_ubah_outlet.setOnClickListener {
            val intent = Intent(this@RegistrasiDevice, DaftarCabang::class.java)
            startActivityForResult(intent,1001)
        }
        btn_hapus_akses.setOnClickListener {
            AlertDialog.Builder(this@RegistrasiDevice)
                .setTitle("Hapus Akses")
                .setMessage("Anda yakin untuk menghapus akses dari outlet ini?")
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        hapusAkses()
                    })
                .setNegativeButton(android.R.string.no, null).show()
        }
    }

    private fun hapusAkses(){
        object : RequestCabang(this@RegistrasiDevice){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    init()
                    setToast("Hak akses telah dicabut")
                }else{
                    setToast("Gagal mencabut hak akses, Periksa kembali masukan anda")

                }
            }

            override fun onError(anError: ANError) {
                setToast("Gagal mencabut hak akses, Terjadi kesalahan saat menghubungi server")
            }

        }.hapusAkses(idCabang)
    }

    private fun getDevice(){
        progressBar2.visibility = ProgressBar.VISIBLE
        object : RequestCabang(this@RegistrasiDevice){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    layout_select_outlet.visibility = LinearLayout.GONE
                    layout_device_outlet.visibility = ConstraintLayout.VISIBLE

                    val arr = resp.DATA.asJsonArray
                    for(tr in arr){
                        val head= Gson().fromJson(tr, Cabang::class.java)
                        text_alamat_outlet.text = ": ${head.ALAMAT}"
                        text_nama_outlet.text = ": ${head.NAMA_CABANG}"
                        text_nama_device.text = Build.MODEL
                        idCabang = head.ID_CABANG
                    }
                    progressBar2.visibility = ProgressBar.GONE
                }else{
                    layout_device_outlet.visibility = ConstraintLayout.GONE
                    layout_select_outlet.visibility = LinearLayout.VISIBLE
                    progressBar2.visibility = ProgressBar.GONE

                }
            }

            override fun onError(anError: ANError) {
                progressBar2.visibility = ProgressBar.GONE
                setToast("Terjadi kesalahan saat menghubungi server")
            }

        }.getMyCabang()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
