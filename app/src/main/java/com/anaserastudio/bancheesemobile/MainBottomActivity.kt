package com.anaserastudio.bancheesemobile

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.PopupMenu
import android.widget.GridView
import com.anaserastudio.bancheesemobile.adapter.GridMenuDashboard
import com.anaserastudio.bancheesemobile.model.MenuDashboard
import kotlinx.android.synthetic.main.activity_main_bottom.*
import android.content.DialogInterface
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.anaserastudio.bancheesemobile.request.RequestCabang
import com.androidnetworking.error.ANError
import org.json.JSONObject

class MainBottomActivity : BaseActivity() {
    private var listMenu = ArrayList<MenuDashboard>()
    private var adapter: GridMenuDashboard? = null
    private lateinit var gridView : GridView
    var isExit = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bottom)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        text_dashboard_nama.text = pref.getString(getString(R.string.key_nama_login),"-")
        text_dashboard_cabang.text = pref.getString(getString(R.string.nama_cabang_login),"-")
        text_dashboard_role.text = pref.getString(getString(R.string.key_role_login),"-")

        init()
    }

    private fun init(){
        cekLogin()
        getDataMenu()
        adapter = GridMenuDashboard(this, listMenu)
        gridView = findViewById(R.id.grid_dashboard_menu)
        gridView.adapter = adapter

        button_dashboard_more.setOnClickListener {
            val popup = PopupMenu(this@MainBottomActivity, button_dashboard_more)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.menu_popup_dashboard, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener { item ->
                if(item.title == "Logout"){

                    AlertDialog.Builder(this)
                        .setTitle("Keluar")
                        .setMessage("Apakah anda yakin untuk keluar?")
                        .setPositiveButton(android.R.string.yes,
                            DialogInterface.OnClickListener { dialog, whichButton ->
                                val edit = pref.edit()
                                edit.putBoolean(getString(R.string.isLogin),false)
                                edit.putString(getString(R.string.key_username_login),null)
                                edit.putString(getString(R.string.key_role_login),null)
                                edit.putString(getString(R.string.key_nama_login),null)
                                edit.putString(getString(R.string.key_cabang_login),null)
                                edit.putString(getString(R.string.key_id_login),null)
                                edit.putString(getString(R.string.nama_cabang_login),null)
                                edit.putString(getString(R.string.alamat_cabang_login),null)

                                edit.apply()
                                edit.commit()
                                finishAct()

                            })
                        .setNegativeButton(android.R.string.no, null).show()



                }else if(item.itemId == R.id.dashboard_popup_password){
                    val intent = Intent(this@MainBottomActivity,UbahPasswordActivity::class.java)
                    startActivity(intent)
                }
                true
            }

            popup.show()
        }

    }

    private fun getVersion(){
        object : RequestCabang(this@MainBottomActivity){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)

            }

            override fun onError(anError: ANError) {
            }
        }.getLatestVersion()
    }

    private fun getDataMenu(){
        val role = pref.getString(getString(R.string.key_role_login),null)
        if(role == null){
            val intent = Intent(this@MainBottomActivity,LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }else{
            if (role.toLowerCase() == "admin"){
                listMenu.add(MenuDashboard("Registrasi Device",resources.getDrawable(R.drawable.qr_code)))
                listMenu.add(MenuDashboard("Bahan Baku",resources.getDrawable(R.drawable.shopping_bag)))
                listMenu.add(MenuDashboard("Printer",resources.getDrawable(R.drawable.print)))
            }else{
                listMenu.add(MenuDashboard("Transaksi",resources.getDrawable(R.drawable.shopping_cart)))
                listMenu.add(MenuDashboard("Nota",resources.getDrawable(R.drawable.invoice)))
                listMenu.add(MenuDashboard("Printer",resources.getDrawable(R.drawable.print)))
                listMenu.add(MenuDashboard("Bahan Baku",resources.getDrawable(R.drawable.shopping_bag)))
            }
        }

    }

    private fun finishAct(){
        val intent = Intent(this@MainBottomActivity,LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun cekLogin(){
        if(!pref.getBoolean(getString(R.string.isLogin),false)){
            val intent = Intent(this,LoginActivity::class.java)
            //startActivity(intent)
            this.finish()
        }
    }

    override fun onBackPressed() {
        if (isExit){
            this.finish()
        }
        setToast("Tekan sekali lagi untuk keluar aplikasi")
        isExit = true
        Handler().postDelayed(Runnable { isExit = false }, 3000)
    }

}
