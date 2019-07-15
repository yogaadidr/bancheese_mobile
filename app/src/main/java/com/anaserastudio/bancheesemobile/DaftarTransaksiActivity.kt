package com.anaserastudio.bancheesemobile

import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import androidx.appcompat.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.anaserastudio.bancheesemobile.adapter.ListNotaAdapter
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.model.TransaksiHeader
import com.anaserastudio.bancheesemobile.request.RequestTransaksiList

import kotlinx.android.synthetic.main.activity_daftar_transaksi.*
import kotlinx.android.synthetic.main.fragment_daftar_transaksi.*
import org.json.JSONObject
import java.lang.Exception
import java.lang.IllegalStateException

class DaftarTransaksiActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_transaksi)

        setSupportActionBar(toolbar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Daftar Transaksi"
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


    }

    override fun onResume() {
        super.onResume()
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }



    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        private var recyclerView: RecyclerView? = null
        private var mAdapter: ListNotaAdapter? = null
        private var listTransaksiHeader = ArrayList<TransaksiHeader>()

        private lateinit var vw : View
        private lateinit var loading : ProgressBar
        private lateinit var swipeRefreshLayout: SwipeRefreshLayout

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            vw = inflater.inflate(R.layout.fragment_daftar_transaksi, container, false)
            loading = vw.findViewById(R.id.loading_transaksi)

            init()

            return vw
        }

        private fun init(){
            initAdapter()
            getTransaksi()
            swipeRefreshLayout = vw.findViewById(R.id.swipe_refresh_transaksi)
            swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                getTransaksi()
            })
        }

        private fun initAdapter(){
            listTransaksiHeader = ArrayList()

            recyclerView = vw.findViewById(R.id.recyclerView)
            mAdapter = ListNotaAdapter(listTransaksiHeader,context!!)
            val mLayoutManager = LinearLayoutManager(context)
            recyclerView!!.layoutManager = mLayoutManager
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            recyclerView!!.adapter = mAdapter
            recyclerView!!.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
            mAdapter!!.notifyDataSetChanged()
        }

        private fun getTransaksi(){
            try {

                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

                val pref = context!!.getSharedPreferences(getString(R.string.pref_string), 0)
                object : RequestTransaksiList(
                    context!!,
                    pref.getString(getString(R.string.key_id_login), null)!!,
                    pref.getString(getString(R.string.key_cabang_login), null)!!
                ) {
                    override fun onResponse(response: JSONObject) {
                        super.onResponse(response)
                        val resp = Gson().fromJson(response.toString(), Response::class.java)
                        val data = arguments?.getInt(ARG_SECTION_NUMBER)
                        if (resp.CODE == 200) {
                            val arr = resp.DATA.asJsonArray
                            for (tr in arr) {
                                val head = gson.fromJson(tr, TransaksiHeader::class.java)
                                if (data == 1) {
                                    if (head.STATUS == "SUKSES") {
                                        listTransaksiHeader.add(head)
                                    }
                                } else if (data == 2) {
                                    if (head.STATUS == "PENDING") {
                                        listTransaksiHeader.add(head)
                                    }
                                } else if (data == 3) {
                                    if (head.STATUS == "VOID") {
                                        listTransaksiHeader.add(head)
                                    }
                                }
                            }
                            if (listTransaksiHeader.size > 0) {
                                text_not_found.visibility = TextView.GONE
                            } else {
                                text_not_found.visibility = TextView.VISIBLE
                            }
                            mAdapter!!.notifyDataSetChanged()

                        } else {
                            text_not_found.visibility = TextView.VISIBLE

                        }
                        loading.visibility = ProgressBar.GONE
                        swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onError(anError: ANError) {
                        text_not_found.visibility = TextView.VISIBLE
                        loading.visibility = ProgressBar.GONE
                        swipeRefreshLayout.isRefreshing = false

                    }

                }.execute()


//            listTransaksiHeader.clear()
//            val pref = context!!.getSharedPreferences(getString(R.string.pref_string),0)
//            val trans = pref.getString(getString(R.string.transaksi_valid),"")
//            if(trans != ""){
//                val json = JSONArray(trans)
//                val data = arguments?.getInt(ARG_SECTION_NUMBER).toString()
//                for (i in 0 until json.length()){
//                    val trHead = Gson().fromJson(json[i].toString(), TransaksiHeader::class.java)
//                    if(data == "SUKSES"){
//                        if(trHead.STATUS == "SUKSES"){
//                            listTransaksiHeader.add(trHead)
//                        }
//                    }else{
//                        if(trHead.STATUS == "PENDING"){
//                            listTransaksiHeader.add(trHead)
//                        }
//                    }
//                }
//            }
//
//            mAdapter!!.notifyDataSetChanged()
            }catch (ex : IllegalStateException){
                swipeRefreshLayout.isRefreshing = false

                Log.d("DaftarTransaski",ex.message)
            }

        }



        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
