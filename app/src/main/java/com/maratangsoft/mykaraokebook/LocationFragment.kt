package com.maratangsoft.mykaraokebook

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maratangsoft.mykaraokebook.databinding.FragmentLocationBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class LocationFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var naverMap: NaverMap
    lateinit var locationSource: FusedLocationSource

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
        super.onHiddenChanged(hidden)
        if (hidden){
            naverMap.locationTrackingMode = LocationTrackingMode.None
        }else{
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
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
        //퍼미션 체크 TODO: Fragment에서 Deprecated된 방법. 맵을 액티비티로 옮기는 게 좋을듯
        if(requireActivity().checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED){
            requestPermissions(permissions, LOCATION_PERMISSION_CODE)
        }
        this.naverMap = p0
        naverMap.locationSource = locationSource

        naverMap.addOnLocationChangeListener {
            Log.d("aaa", "lat = ${it.latitude}, lng = ${it.longitude}")
        }

        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            Log.d("aaa_LocationFrag", locationSource.isActivated.toString())
            if (locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }else{
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}