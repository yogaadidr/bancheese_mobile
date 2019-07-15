package com.anaserastudio.bancheesemobile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.anaserastudio.bancheesemobile.tool.StringControl

open class BaseActivity : AppCompatActivity() {

    var stringControl = StringControl()
    val REQUEST_ORDER = 200
    val RESPONSE_ORDER = 202
    val NO_RESPONSE = 100
    val RESPONSE_ORDER_ERROR = 402
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(getString(R.string.pref_string),0)

    }


    fun setToast(kata : String){
        Toast.makeText(this, kata, Toast.LENGTH_SHORT).show()
    }



}
