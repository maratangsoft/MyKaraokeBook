package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.maratangsoft.mykaraokebook.databinding.FragmentPopularBinding
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.concurrent.thread

class PopularFragment : Fragment() {
    private lateinit var binding: FragmentPopularBinding
    private var items: MutableList<Item> = mutableListOf()
    private var strType = "1"
    private var gb = "1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNation.setOnClickListener { openPopup() }
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        binding.recycler.adapter = SearchAdapter(requireActivity(), items)

        when (brand){
            "tj" -> loadTJData()
            "kumyoung" -> loadKumyoungData()
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private fun openPopup(){
        val popup = PopupMenu(requireActivity(), binding.btnNation)
        popup.menuInflater.inflate(R.menu.popup_popular, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.korean -> {
                    strType = "1"
                    gb = "1"
                }
                R.id.english -> {
                    strType = "2"
                    gb = "5"
                }
                R.id.japanese -> {
                    strType = "3"
                    gb = "6"
                }
            }
            binding.btnNation.text = it.title
            when (brand){
                "tj" -> loadTJData()
                "kumyoung" -> loadKumyoungData()
            }
            true
        }
        popup.show()
    }

    private fun loadTJData(){
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        thread {
            val popularUrl = "https://www.tjmedia.com/tjsong/song_monthPopular.asp?strType=${strType}"
            try {
                val doc = Jsoup.connect(popularUrl).get()
                val table = doc.select("table[class=board_type1] tr")

                for (tr in table){
                    if (tr == table[0]) continue //첫줄은 제목줄이라 넘어가기

                    val tds = tr.select("td")
                    val no:String = tds[1].text()
                    val title:String = tds[2].text()
                    val singer:String = tds[3].text()
                    items.add(Item(brand, no, title, singer, null, null))
                }
                requireActivity().runOnUiThread{
                    binding.recycler.adapter?.notifyDataSetChanged()
                }
            } catch(ioException: IOException) {
                requireActivity().runOnUiThread{
                    Toast.makeText(requireActivity(), "IOException", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadKumyoungData() {
        items.clear()
        binding.recycler.adapter?.notifyDataSetChanged()

        thread {
            val popularUrl = "https://kysing.kr/genre-polular/?gb=${gb}"
            try {
                val doc = Jsoup.connect(popularUrl).get()
                val chart = doc.select("ul[class=popular_chart_list clear]")

                for (ul in chart){
                    if (ul == chart[0]) continue //첫줄은 제목줄이라 넘어가기
                    val lis = ul.select("li")
                    val no:String = lis[1].text()
                    val title:String = lis.select("li[class=popular_chart_tit clear] span")[0].text()
                    val singer:String = lis[3].text()
                    items.add(Item(brand, no, title, singer, null, null))
                }

                requireActivity().runOnUiThread{
                    binding.recycler.adapter?.notifyDataSetChanged()
                }
            } catch(ioException: IOException) {
                requireActivity().runOnUiThread{
                    Toast.makeText(requireActivity(), "IOException", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
