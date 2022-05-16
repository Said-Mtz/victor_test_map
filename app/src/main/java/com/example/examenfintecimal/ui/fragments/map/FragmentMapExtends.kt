package com.example.examenfintecimal.ui.fragments.map

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.examenfintecimal.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun FragmentMap.initElement() {
    mBinding.apply {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        createFragment()

        with(viewModel.getSelectedPlace()) {
            if (visited) {
                txtStatusPlace.text = getString(R.string.txt_item_visit)
                svgStatusCircle.setColorFilter(
                    ContextCompat.getColor(
                        root.context,
                        R.color.green_aqua
                    )
                )
            } else {
                txtStatusPlace.text = getString(R.string.txt_item_pending)
                svgStatusCircle.setColorFilter(ContextCompat.getColor(root.context, R.color.grey))
                linearLayoutTwo.visibility = View.VISIBLE
            }
            txtStreetName.text = streetName
            txtSubUrb.text = suburb

            btnNav.setOnClickListener {
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo: ${location.latitude} ${location.longitude}")
                ).also {
                    startActivity(it)
                }
            }
        }

        btnDoVisit.setOnClickListener {
            viewModel.updatePlace()
            viewModel.requestPlaces(requireContext()){
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(500)
                    findNavController().popBackStack()
                }
            }
        }
    }

}

fun FragmentMap.createFragment() {
    val mapFragment: SupportMapFragment =
        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    mapFragment.view?.isClickable = false
}

fun FragmentMap.createMarket() {
    val coordenadas = with(viewModel.getSelectedPlace()) {
        LatLng(location.latitude, location.longitude)
    }
    val bitmap = getDrawable(
        requireContext(),
        if (viewModel.getSelectedPlace().visited) R.drawable.ic_visited_marker else
            R.drawable.ic_visite_marker
    )!!.toBitmap(84, 98)

    val marker = MarkerOptions().position(coordenadas)
        .title(viewModel.getSelectedPlace().suburb)
        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
    map.addMarker(marker)

    map.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            coordenadas,
            18f
        ),//coordanadas a mostrar y cuanto zoom realizara
        2000,//Tiempo en realizar el zoom
        null
    )
}
