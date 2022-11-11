package com.maratangsoft.mykaraokebook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.maratangsoft.mykaraokebook.databinding.FragmentLocationBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationSource
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource

class LocationFragment : Fragment() {
    private lateinit var binding: FragmentLocationBinding

    val providerClient by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 5000
    ).build()
    lateinit var locationCallback: LocationCallback

    lateinit var naverMap: NaverMap
    lateinit var locationSource: FusedLocationSource

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSetting.setOnClickListener { startActivity(Intent(requireActivity(), SettingActivity::class.java)) }
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PER)
        getLocation()
        loadNaverMap()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        when (hidden){
            true -> providerClient.removeLocationUpdates(locationCallback)
            false -> getLocation()
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////

    private fun loadNaverMap(){
        val fm = requireActivity().supportFragmentManager
        val map = fm.findFragmentById(R.id.frag_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.frag_map, it).commit()
            }
        map.getMapAsync {
            this.naverMap = it
        }
    }

    fun getLocation(){
        //FusedLocationProvider로 좌표 받기
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.d("TAG", location.toString())
            }
        }
        if (ActivityCompat.checkSelfPermission(requireActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }


    }
}