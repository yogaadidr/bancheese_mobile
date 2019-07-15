package com.anaserastudio.bancheesemobile

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.widget.ProgressBar
import android.widget.TextView
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestUbahPassword
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_ubah_password.*
import org.json.JSONObject

class UbahPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_password)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Ubah Password"
        init()
    }

    private fun init(){
        btn_pass_simpan.setOnClickListener {
            loading_password.visibility = ProgressBar.VISIBLE
            if(checkNewPassword()){
                changePassword()
            }
        }
        btn_pass_kembali.setOnClickListener {
            onBackPressed()
        }
    }

    private fun changePassword(){
        val idUser = pref.getString(getString(R.string.key_id_login),null)
        object : RequestUbahPassword(this,idUser!!,text_pass_lama.text.toString(),text_pass_baru.text.toString(),text_pass_ulang.text.toString()){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    text_alert.visibility = TextView.GONE
                    setToast("Password berhasil diubah")
                }else{
                    text_alert.text = resp.MESSAGE
                    text_alert.visibility = TextView.VISIBLE
                }
                loading_password.visibility = ProgressBar.GONE

            }

            override fun onError(anError: ANError) {
            }

        }.execute()
    }


    private fun checkNewPassword() : Boolean{
        return if(text_pass_baru.length() >= 6){
            if(text_pass_baru.text.toString() == text_pass_ulang.text.toString()){
                text_alert.visibility = TextView.GONE
                true
            }else{
                text_alert.text = getString(R.string.password_tidak_cocok)
                text_alert.visibility = TextView.VISIBLE
                loading_password.visibility = ProgressBar.GONE
                false
            }
        }else{
            text_alert.text = getString(R.string.min_password)
            text_alert.visibility = TextView.VISIBLE
            loading_password.visibility = ProgressBar.GONE

            false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
