package com.anaserastudio.bancheesemobile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.*
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.anaserastudio.bancheesemobile.adapter.GridMenuAdapter
import com.anaserastudio.bancheesemobile.adapter.ListOrder
import com.anaserastudio.bancheesemobile.model.Menu
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.model.Transaksi
import com.anaserastudio.bancheesemobile.model.TransaksiHeader
import com.anaserastudio.bancheesemobile.request.RequestMenu
import com.anaserastudio.bancheesemobile.request.RequestTransaksi
import com.anaserastudio.bancheesemobile.tool.PrinterControl
import com.anaserastudio.bancheesemobile.tool.StringControl
import kotlinx.android.synthetic.main.activity_transaksi.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class TransaksiActivity : BaseActivity(){

    private val TAG = "TransaksiActivity"

    private var listMenu = ArrayList<Menu>()
    private var listMenuMaster = ArrayList<Menu>()
    private var listTransaksi = ArrayList<Menu>()
    private lateinit  var btnPesan : Button
    private var adapter: GridMenuAdapter? = null
    private lateinit var gridView : GridView
    private lateinit var transaksiHeader : TransaksiHeader
    private var isEditted = false
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListOrder? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Detail Order"
        gridView = findViewById(R.id.grid_menu)
        recyclerView = findViewById(R.id.recycler_view)
        btnPesan = findViewById(R.id.btn_pesan)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESPONSE_ORDER){
            transaksiHeader.listTransaksi = listTransaksi
            transaksiHeader.STATUS = "SUKSES"
            transaksiHeader.BAYAR = data!!.extras!!.getInt("bayar")

            val total = countTotal(listTransaksi)
            text_uang_kembali.text = StringControl().getNumberFormat(transaksiHeader.BAYAR - total)

            layout_success.visibility = ConstraintLayout.VISIBLE
            btn_transaksi_kembali.setOnClickListener {
                layout_success.visibility = ConstraintLayout.GONE
                resetTransaksi()
            }
            PrinterControl(this@TransaksiActivity).print(transaksiHeader.BAYAR,listTransaksi,null)

            btn_transaksi_print.setOnClickListener {
                PrinterControl(this@TransaksiActivity).print(transaksiHeader.BAYAR,listTransaksi,null)
            }
        }
    }

    private fun init() {
        layout_success.visibility = ConstraintLayout.GONE
        getDataMenu()

        if(intent.hasExtra("transaksiHeader")){
            val header = intent!!.extras!!.getString("transaksiHeader")
            transaksiHeader = Gson().fromJson<TransaksiHeader>(header,TransaksiHeader::class.java)
            listTransaksi = transaksiHeader.listTransaksi
            isEditted = true
            countTotal(listTransaksi)
        }else{
            isEditted = false
            initTransaksiHeader()
        }

        adapter = GridMenuAdapter(this, listMenu, this@TransaksiActivity)
        gridView.adapter = adapter
        initOrder()
        btnPesan.setOnClickListener {
            if(listTransaksi.size < 1){
                setToast("Anda belum memilih menu")
            }else{
                val intent = Intent(this@TransaksiActivity,DetailOrderActivity::class.java)
                val json = Gson().toJson(listTransaksi)
                intent.putExtra("order",json)
                startActivityForResult(intent,REQUEST_ORDER)
            }

        }
        btn_simpan.setOnClickListener {
            if (listTransaksi.size < 1){
                setToast("Anda belum memilih menu")
            }else{
                AlertDialog.Builder(this@TransaksiActivity)
                    .setTitle("Simpan")
                    .setMessage("Simpan transaksi ini?")
                    .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, whichButton ->


                            transaksiHeader.listTransaksi = listTransaksi
                            transaksiHeader.STATUS = "PENDING"

                            addTransaksi(transaksiHeader)
                            listTransaksi.clear()
                            mAdapter!!.notifyDataSetChanged()
                            countTotal(listTransaksi)
                            initTransaksiHeader()
                            setToast("Transaksi disimpan")
                        })
                    .setNegativeButton(android.R.string.no, null).show()
            }
        }

        text_cari.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listMenu = ArrayList()
                if(!p0.isNullOrEmpty()){
                    for (mn in listMenuMaster){
                        if(mn.NAMA_MENU.contains(p0.toString(),true)){
                            listMenu.add(mn)
                        }
                    }
                }else{
                    listMenu = listMenuMaster
                }
                adapter = GridMenuAdapter(this@TransaksiActivity, listMenu, this@TransaksiActivity)
                gridView.adapter = adapter

            }

        })

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_menu)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getDataMenu()
        })

    }

    private fun resetTransaksi(){
        addTransaksi(transaksiHeader)
        listTransaksi.clear()
        mAdapter!!.notifyDataSetChanged()
        countTotal(listTransaksi)

        initTransaksiHeader()
    }

    private fun initTransaksiHeader(){
        transaksiHeader = TransaksiHeader(
            StringControl().getDateNow(),
            pref.getString(getString(R.string.key_id_login),null)!!,
            pref.getString(getString(R.string.key_cabang_login),null)!!,
            Calendar.getInstance().time,
            "",
            0,
            listTransaksi)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initOrder(){
        val inflater = layoutInflater
        mAdapter = ListOrder(listTransaksi,this@TransaksiActivity,inflater,this)
        val mLayoutManager = LinearLayoutManager(this@TransaksiActivity)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this@TransaksiActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        mAdapter!!.notifyDataSetChanged()
    }

    private fun getDataMenu(){
        listMenu = ArrayList()
        listMenuMaster = ArrayList()
//        load_menu.visibility = ProgressBar.VISIBLE

        object : RequestMenu(this@TransaksiActivity,pref.getString(getString(R.string.key_cabang_login),"0")!!){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                try {
                    val resp = Gson().fromJson(response.toString(), Response::class.java)
                    if(resp.CODE == 200){
                        val arr = resp.DATA.asJsonArray
                        for (i in 0 until arr.size()) {

                            val menu = arr.get(i).asJsonObject
                            val data = Gson().fromJson(menu, Menu::class.java)
                            listMenu.add(data)
                        }
                        listMenuMaster = listMenu
                        adapter!!.notifyDataSetChanged()
                    }

                }catch (err : JsonSyntaxException){
                    setToast("Menu tidak ada"+err.message)
                }

                load_menu.visibility = ProgressBar.GONE
                swipeRefreshLayout.isRefreshing = false

            }

            override fun onError(anError: ANError) {
                swipeRefreshLayout.isRefreshing = false

                load_menu.visibility = ProgressBar.GONE

            }
        }.execute()

    }

    fun addTransaction(menu : Menu){
        val transaksi : Transaksi
        var isContained = false
        var pos = 0
        if(listTransaksi.size > 0){

            for (i in 0 until listTransaksi.size){
                if(listTransaksi[i].ID_MENU_DETAIL == menu.ID_MENU_DETAIL){
                    isContained = true
                    pos = i
                }
            }
        }else{
        }

        if(isContained){
            transaksi = listTransaksi[pos].transaksi
            transaksi.QTY += 1
            transaksi.HARGA = StringControl().roundedNumber(menu.HARGA.toString()) * transaksi.QTY
            transaksi.NAMA_MENU = menu.NAMA_MENU
            listTransaksi[pos].transaksi = transaksi
        }else{
            transaksi = Transaksi(
                null,
                transaksiHeader.ID_TRANSAKSI,
                menu.ID_MENU_DETAIL,
                StringControl().roundedNumber(menu.HARGA.toString()),
                menu.NAMA_MENU,
                1,
                0.0)
            menu.transaksi = transaksi
            listTransaksi.add(menu)
        }

        mAdapter!!.notifyDataSetChanged()
        countTotal(listTransaksi)
    }

    fun countTotal(transaksi : ArrayList<Menu>) : Int{
        var total = 0
        for (i in 0 until transaksi.size) {
            total += StringControl().roundedNumber(transaksi[i].transaksi.HARGA.toString())
        }
        if (listTransaksi.size > 0){
            image_receipt.visibility = ImageView.GONE

        }else{
            image_receipt.visibility = ImageView.VISIBLE

        }

        btnPesan.text = stringControl.getNumberFormat(total)
        return total
    }


    private fun addTransaksi(list : TransaksiHeader){
        object : RequestTransaksi(this@TransaksiActivity){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                println(response)
            }

            override fun onError(anError: ANError) {
            }

        }.execute(list)

        val listTrx = ArrayList<TransaksiHeader>()
        val edit = pref.edit()
        val trans = pref.getString(getString(R.string.transaksi_valid),"")
        if(trans != ""){
            val json = JSONArray(trans)
            for (i in 0 until json.length()){
                val trHead = Gson().fromJson(json[i].toString(),TransaksiHeader::class.java)
                listTrx.add(trHead)
            }
        }

        listTrx.add(list)
        val data = Gson().toJson(listTrx)
        edit.putString(getString(R.string.transaksi_valid),data)
        edit.apply()
        edit.commit()
    }

    override fun onBackPressed() {

        if(listTransaksi.size > 0){
            AlertDialog.Builder(this@TransaksiActivity)
                .setTitle("Kembali")
                .setMessage("Batalkan transasksi ini?")
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        goBack()
                    })
                .setNegativeButton(android.R.string.no, null).show()

        }else{
            goBack()
        }


    }

    private fun goBack(){
        super.onBackPressed()
        val intent = Intent(this@TransaksiActivity,MainBottomActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
