package com.anaserastudio.bancheesemobile

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.*
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.anaserastudio.bancheesemobile.adapter.ListOrderDetail
import com.anaserastudio.bancheesemobile.model.Menu
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.model.Transaksi
import com.anaserastudio.bancheesemobile.model.TransaksiHeader
import com.anaserastudio.bancheesemobile.request.RequestTransaksi
import com.anaserastudio.bancheesemobile.request.RequestTransaksiDetail
import com.anaserastudio.bancheesemobile.tool.PrinterControl
import com.anaserastudio.bancheesemobile.tool.StringControl
import kotlinx.android.synthetic.main.activity_detail_nota.*
import org.json.JSONObject

class DetailNotaActivity : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListOrderDetail? = null
    private var listTransaksi = ArrayList<Transaksi>()
    private lateinit var transaksiHeader : TransaksiHeader
    private var listMenu = ArrayList<Menu>()
    private lateinit var idTransaksi : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_nota)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Detail Nota"
        idTransaksi = intent.extras.getString("idTransaksi")
        init()
    }
    private fun init(){
        listTransaksi = ArrayList()
        initOrder()
        getDataTransaksi()


    }

    private fun getDataTransaksi(){
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        object : RequestTransaksiDetail(this@DetailNotaActivity, idTransaksi){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = gson.fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val arr = resp.DATA.asJsonArray
                    var bayar = 0
                    var status = "PENDING"
                    for(tr in arr){
                        val head = gson.fromJson(tr, Transaksi::class.java)
                        listTransaksi.add(head)
                        bayar = head.BAYAR
                        status = head.STATUS

                    }

                    mAdapter!!.notifyDataSetChanged()

                    val sc = StringControl()
                    text_nota_total.text = sc.getNumberFormat(getTotal())
                    if(bayar > 0){
                        text_nota_bayar.text = sc.getNumberFormat(bayar )
                    }else{
                        bayar  = 0
                        text_nota_bayar.text = sc.getNumberFormat(0)
                    }
                    text_nota_sisa.text = sc.getNumberFormat(bayar - getTotal())
                    text_status.text = status

                    if(status== "PENDING"){
                        btn_detail_ubah.setOnClickListener {
                            val intent = Intent(this@DetailNotaActivity, TransaksiActivity::class.java)
                            intent.putExtra("transaksiHeader",convertToJSON())
                            startActivity(intent)
                        }
                        btn_detail_hapus.text = "HAPUS"

                        btn_detail_hapus.setOnClickListener {
                            AlertDialog.Builder(this@DetailNotaActivity)
                                .setTitle("Hapus")
                                .setMessage("Apakah anda yakin untuk menghapus transaksi ini?")
                                .setPositiveButton(android.R.string.yes,
                                    DialogInterface.OnClickListener { dialog, whichButton ->
                                        object : RequestTransaksi(this@DetailNotaActivity){
                                            override fun onResponse(response: JSONObject) {
                                                super.onResponse(response)
                                                val intent = Intent()
                                                setResult(1002,intent)
                                                finish()
                                            }

                                            override fun onError(anError: ANError) {
                                                setToast("Terjadi kesalahan saat menghubungi server")
                                            }

                                        }.deleteTransaksi(idTransaksi)

                                    })
                                .setNegativeButton(android.R.string.no, null).show()
                        }

                    }else if(status == "SUKSES") {
                        btn_detail_ubah.text = "PRINT"
                        btn_detail_ubah.setOnClickListener {
                            PrinterControl(this@DetailNotaActivity).print(bayar, null, listTransaksi)
                        }

                        btn_detail_hapus.text = "VOID"
                        btn_detail_hapus.setOnClickListener {
                            AlertDialog.Builder(this@DetailNotaActivity)
                                .setTitle("Void")
                                .setMessage("Apakah anda yakin untuk mengubah status menjadi void?")
                                .setPositiveButton(android.R.string.yes,
                                    DialogInterface.OnClickListener { dialog, whichButton ->
                                        object : RequestTransaksi(this@DetailNotaActivity){
                                            override fun onResponse(response: JSONObject) {
                                                super.onResponse(response)
                                                setToast("Transaksi telah berstatus VOID")
                                                init()

                                            }

                                            override fun onError(anError: ANError) {
                                                setToast("Terjadi kesalahan saat menghubungi server")
                                            }

                                        }.setVoid(idTransaksi)

                                    })
                                .setNegativeButton(android.R.string.no, null).show()

                        }

                    }else{
                        btn_detail_ubah.visibility = Button.GONE
                        btn_detail_hapus.visibility = Button.GONE

                    }


                }else{

                }

                loading_nota.visibility = ProgressBar.GONE
            }

            override fun onError(anError: ANError) {
                loading_nota.visibility = ProgressBar.GONE

            }
        }.execute()
    }

    private fun initOrder(){
        recyclerView = findViewById(R.id.recyclerView)
        val inflater = layoutInflater
        mAdapter = ListOrderDetail(listTransaksi,this@DetailNotaActivity,inflater,null)
        val mLayoutManager = LinearLayoutManager(this@DetailNotaActivity)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.isNestedScrollingEnabled = false
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this@DetailNotaActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        mAdapter!!.notifyDataSetChanged()
    }

    private fun getTotal() : Int{
        var total = 0
        for (trx in listTransaksi)
        {
            total += StringControl().roundedNumber(trx.HARGA.toString()) * trx.QTY
        }
        return total
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    fun convertToJSON() : String{

        val listMenu = ArrayList<Menu>()
        var status = ""
        var bayar = 0
        for (a in listTransaksi){
            val trans = Transaksi(a.ID_TRANSAKSI_DETAIL,a.ID_TRANSAKSI,a.ID_MENU_DETAIL,a.HARGA,a.NAMA_MENU,a.QTY,a.DISKON)
            val menu = Menu(a.ID_MENU_DETAIL,a.NAMA_MENU,StringControl().roundedNumber(a.HARGA.toString()),"",trans)
            listMenu.add(menu)
            status = a.STATUS
            bayar = a.BAYAR

        }
        val  pref = getSharedPreferences(getString(R.string.pref_string),0)
        val thead = TransaksiHeader(idTransaksi,pref.getString(getString(R.string.key_id_login),null),
            pref.getString(getString(R.string.key_cabang_login),null),status,bayar,listMenu)

        return Gson().toJson(thead)


    }


}
