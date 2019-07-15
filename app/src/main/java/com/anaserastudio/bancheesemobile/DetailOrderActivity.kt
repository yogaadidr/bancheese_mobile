package com.anaserastudio.bancheesemobile

import android.os.Bundle
import com.google.gson.Gson
import com.anaserastudio.bancheesemobile.model.Menu
import android.view.View
import android.widget.Button
import com.anaserastudio.bancheesemobile.adapter.ListOrder
import com.anaserastudio.bancheesemobile.tool.StringControl
import kotlinx.android.synthetic.main.activity_detail_order.*
import org.json.JSONArray
import java.lang.NumberFormatException
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.widget.*
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class DetailOrderActivity : BaseActivity() {

    private var listTransaksi = ArrayList<Menu>()
    private var nominal = ""
    private var nominalNumber = 0
    private var recyclerView: RecyclerView? = null
    private var total = 0
    private var mAdapter: ListOrder? = null
    private var uang = arrayOf(500,1000,1500,2000,2500,3000,4000,5000,7000,10000,11000,12000,15000,20000,22000,25000,30000,35000,40000,50000,60000,70000,100000,110000,120000,150000)
    private var selectedUang = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Detail Order"
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_close_black_18dp)
        recyclerView = findViewById(R.id.recyclerView)

        init()
    }

    private fun init(){
        getDataIntent()
        initOrder()

        nominalNumber = total

        order_btn_bayar.setOnClickListener {
            if((nominalNumber-total) < 0){
                setToast("Periksa kembali inputan anda")
            }else{
                val returnIntent = Intent()
                returnIntent.putExtra("bayar",nominalNumber)
                setResult(RESPONSE_ORDER, returnIntent)
                finish()
            }
        }

        getNominalBayar()
    }

    private fun getNominalBayar(){
        for (i in 0 until 3){
            var isAdded = false
            for (u in uang){
                if(u > total && !selectedUang.contains(u)){
                    selectedUang.add(u)
                    isAdded = true
                    break
                }
            }
            if(!isAdded){
                selectedUang.add(total + ((i+1) * 10000))
            }

        }

        val sc = StringControl()
        text_nominal_2.text = sc.getNumberFormat(selectedUang[0])
        text_nominal_3.text = sc.getNumberFormat(selectedUang[1])
        text_nominal_4.text = sc.getNumberFormat(selectedUang[2])

        text_nominal_1.background = resources.getDrawable(R.color.colorPrimary)
        text_bayar.text = StringControl().getNumberFormat(total)
5

        text_nominal_1.setOnClickListener {
            text_bayar.text = StringControl().getNumberFormat(total)
            nominalNumber = total
            text_nominal_1.background = resources.getDrawable(R.color.colorPrimary)
            text_nominal_2.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_3.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_4.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_lain.setText("")
        }

        text_nominal_2.setOnClickListener {
            text_bayar.text = StringControl().getNumberFormat(selectedUang[0])
            nominalNumber = selectedUang[0]
            text_nominal_2.background = resources.getDrawable(R.color.colorPrimary)
            text_nominal_3.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_4.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_1.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_lain.setText("")
        }
        text_nominal_3.setOnClickListener {
            text_bayar.text = StringControl().getNumberFormat(selectedUang[1])
            nominalNumber = selectedUang[1]

            text_nominal_3.background = resources.getDrawable(R.color.colorPrimary)
            text_nominal_2.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_4.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_1.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_lain.setText("")
        }
        text_nominal_4.setOnClickListener {
            text_bayar.text = StringControl().getNumberFormat(selectedUang[2])
            nominalNumber = selectedUang[2]

            text_nominal_4.background = resources.getDrawable(R.color.colorPrimary)
            text_nominal_3.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_2.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_1.background = resources.getDrawable(R.color.colorBlack)
            text_nominal_lain.setText("")

        }
        text_nominal_lain.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
//                text_nominal_1.background = resources.getDrawable(R.color.colorBlack)
//                text_nominal_2.background = resources.getDrawable(R.color.colorBlack)
//                text_nominal_3.background = resources.getDrawable(R.color.colorBlack)
//                text_nominal_4.background = resources.getDrawable(R.color.colorBlack)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {

                    text_bayar.text = StringControl().getNumberFormat(Integer.parseInt(p0.toString()))
                    nominalNumber = p0.toString().toInt()
                }catch (e : Exception){
                    println(e.message)
                }

            }

        })
//        text_nominal_lain.setOnKeyListener(object : View.OnKeyListener {
//            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    text_nominal_1.background = resources.getDrawable(R.color.colorBlack)
//                    text_nominal_2.background = resources.getDrawable(R.color.colorBlack)
//                    text_nominal_3.background = resources.getDrawable(R.color.colorBlack)
//                    text_nominal_4.background = resources.getDrawable(R.color.colorBlack)
//                    text_bayar.text = StringControl().getNumberFormat(Integer.parseInt(text_nominal_lain.text.toString()))
//
//                    return true
//                }
//                return false
//            }
//        })

    }

    private fun getDataIntent(){
        val data = intent.extras.getString("order")
        val json = JSONArray(data.toString())
        total = 0
        for (i in 0 until json.length()){
            val menu = Gson().fromJson(json[i].toString(),Menu::class.java)
            listTransaksi.add(menu)
            total+= StringControl().roundedNumber(menu.transaksi.HARGA.toString())
        }

        text_total.text = StringControl().getNumberFormat(total)
    }

    fun addNumber(view : View){
        val b = view as Button
        val buttonText = b.text.toString()
        nominal+= buttonText
        try{
            nominalNumber = nominal.toInt()
        }catch (nf :NumberFormatException){
            setToast("Jumlah angka sudah maksimal")
        }
        text_bayar.text = StringControl().getNumberFormat(nominalNumber)
        text_nominal.text = StringControl().getNumberFormatNoCurr(nominalNumber)
    }

    fun clear(view : View){
        nominal = ""
        nominalNumber = 0
        text_bayar.text = StringControl().getNumberFormat(nominalNumber)
        text_nominal.text = ""
//        text_sisa.text = StringControl().getNumberFormat((nominalNumber-total))

    }

    private fun initOrder(){
        val inflater = layoutInflater
        mAdapter = ListOrder(listTransaksi,this@DetailOrderActivity,inflater,null)
        val mLayoutManager = LinearLayoutManager(this@DetailOrderActivity)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this@DetailOrderActivity,
                LinearLayoutManager.VERTICAL
            )
        )
        mAdapter!!.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        setResult(NO_RESPONSE, returnIntent)
        finish()
    }

}
