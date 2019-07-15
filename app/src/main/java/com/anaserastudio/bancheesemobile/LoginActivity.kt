package com.anaserastudio.bancheesemobile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.error.ANError
import com.anaserastudio.bancheesemobile.request.RequestLogin

import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import com.androidnetworking.AndroidNetworking
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.model.User
import android.app.ProgressDialog




/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(){
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */



    lateinit var pref : SharedPreferences
    private lateinit var dialog: ProgressDialog

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AndroidNetworking.initialize(applicationContext)
        // Set up the login form.
        text_login_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        dialog = ProgressDialog(this@LoginActivity)
        pref = getSharedPreferences(getString(R.string.pref_string),0)
        email_sign_in_button.setOnClickListener { attemptLogin() }
    }


    private fun attemptLogin() {
        text_login_alert.visibility = TextView.GONE
        val mEmail = text_login_username.text.toString()
        val mPassword = text_login_password.text.toString()
        dialog.setMessage("Tunggu Sebentar...")
        dialog.show()

        object : RequestLogin(this@LoginActivity,mEmail,mPassword){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                try{
                    val resp = Gson().fromJson(response.toString(),Response::class.java)
                    if (resp.CODE == 200){
                        println(resp.DATA)
                        val data = resp.DATA as JsonObject
                        val user = Gson().fromJson(data,User::class.java)
                        val edit = pref.edit()
                        edit.putBoolean(getString(R.string.isLogin),true)
                        edit.putString(getString(R.string.key_username_login),user.username)
                        edit.putString(getString(R.string.key_role_login),user.role)
                        edit.putString(getString(R.string.key_nama_login),user.nama_user)
                        edit.putString(getString(R.string.key_cabang_login),user.id_cabang)
                        edit.putString(getString(R.string.key_id_login),user.id_user)
                        edit.putString(getString(R.string.nama_cabang_login),user.nama_cabang)
                        edit.putString(getString(R.string.alamat_cabang_login),user.alamat)

                        edit.apply()
                        edit.commit()
                        dialog.dismiss()
                        finishAct()


                    }else{
                        text_login_alert.visibility = TextView.VISIBLE
                        text_login_alert.text = resp.MESSAGE
                    }
                    dialog.dismiss()
                }catch (err : JsonSyntaxException){
                    text_login_alert.visibility = TextView.VISIBLE
                    text_login_alert.text = getString(R.string.text_user_not_found)
                    dialog.dismiss()
                }
            }

            override fun onError(anError: ANError) {
                Toast.makeText(this@LoginActivity,"Terjadi kesalahan saat menghubungi server",Toast.LENGTH_SHORT).show()
                println(anError.message.toString())
                dialog.dismiss()
            }
        }.execute()
    }

    private fun finishAct(){
        val intent = Intent(this@LoginActivity, MainBottomActivity::class.java)
        this.finish()
        startActivity(intent)
    }
}
