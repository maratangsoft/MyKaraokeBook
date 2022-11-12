package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.FragmentLocationBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LocationFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var naverMap: NaverMap
    lateinit var locationSource: FusedLocationSource
    private var lastPosition: Location? = null
    private val markers = Array(5) {Marker()}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_CODE)

        loadNaverMap()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) naverMap.locationTrackingMode = LocationTrackingMode.None
    }
//////////////////////////////////////////////////////////////////////////////////////////////////

    private fun loadNaverMap(){
        val fm = requireActivity().supportFragmentManager
        val map = fm.findFragmentById(R.id.frag_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.frag_map, it).commit()
            }
        map.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0
        naverMap.locationSource = locationSource

        //퍼미션 체크 TODO: Fragment에서 Deprecated된 방법. 맵을 액티비티로 옮기는 게 좋을듯
        if(requireActivity().checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, LOCATION_PERMISSION_CODE)
        }
        //맵 UI 설정
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true

        naverMap.addOnLocationChangeListener {
            val query = makeQuery()
            Log.d("CustomTag-query", query)
            if (query != "") searchKaraoke(query)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            Log.d("CustomTag-LocationFrag", locationSource.isActivated.toString())
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    private fun makeQuery(): String {
        locationSource.lastLocation?.let {
            val presentPosition = it
            //현재 좌표가 lastPosition과 50미터 이상 차이가 나지 않으면 작업 수행 안함
            if (lastPosition != null && presentPosition.distanceTo(lastPosition!!) <= 50) {
                return ""
            }
            lastPosition = presentPosition
            Log.d("CustomTag-address", presentPosition.toString())

            //현재위치 좌표 -> 주소 변환 (API 33부터 방식 바뀜)
            val lat = presentPosition.latitude
            val lng = presentPosition.longitude
            val geocoder = Geocoder(requireActivity(), Locale.KOREAN)

            var addresses: List<Address> = listOf()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(lat, lng, 5) {
                    addresses = it
                }
            } else {
                geocoder.getFromLocation(lat, lng, 5)?.let { addresses = it }
            }

            Log.d("CustomTag-address", addresses.toString())

            //주소 가공해서 검색어 만들기
            val region = addresses[2].getAddressLine(0).replace("대한민국 ", "")
            return "$region 노래방"
        }
        return ""
    }

    private fun searchKaraoke(query:String){
        //맵에 표시중인 마커 지우기
        markers.forEach { it.map = null }

        val clientId = "Bbw7aySf2URwfsoXB86N"
        val clientSecret = "J0EqYSjZ5E"

        RetrofitHelper.getInstance("https://openapi.naver.com/").create(RetrofitService::class.java)
            .locateKaraoke(query, 5, clientId, clientSecret).enqueue(object : Callback<NaverResult> {

                override fun onResponse(call: Call<NaverResult>, response: Response<NaverResult>) {
                    response.body()?.let {
                        val result = it
                        Log.d("aaa-NaverResult", result.items.toString())

                        //받아온 정보 마커로 만들어서 맵에 올리기
                        result.items.forEachIndexed { i, item ->
                            markers[i].position = Tm128(item.mapx.toDouble(), item.mapy.toDouble()).toLatLng()
                            markers[i].captionText = item.title.replace("<b>", "").replace("</b>", "")
                            markers[i].map = naverMap
                        }
                    }
                }
                override fun onFailure(call: Call<NaverResult>, t: Throwable) {
                    AlertDialog.Builder(requireActivity()).setMessage(t.message).show()
                }
            })
    }
} //TODO 마커 클릭하면 간단한 바텀시트 띄우기, 최대 줌 거리 제한하기