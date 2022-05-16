package com.example.examenfintecimal.ui.fragments.home

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.examenfintecimal.R
import com.example.examenfintecimal.core.model.ModelApiItem
import com.example.examenfintecimal.core.model.ModelApiList
import com.example.examenfintecimal.ui.StatusRequest
import com.example.examenfintecimal.utils.filterEmoji

fun FragmentHome.initElement() = with(mBinding) {

    viewModel.requestPlaces(requireContext())
    edTxtSearch.doAfterTextChanged { txt ->
        if(txt.toString().isNotEmpty()){
            searchPlace(edTxtSearch.text.toString())
        }
    }
    edTxtSearch.filterEmoji()
}

fun FragmentHome.initObservers() {
    viewModel.placesData.observe(viewLifecycleOwner) {
        mBinding.apply {
            when (it.first) {
                StatusRequest.LOADING -> {
                    shimmer.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                StatusRequest.SUCCESS -> {
                    if (it.second != null && it.second?.isNotEmpty() == true) {
                        initRecycler(it.second ?: ModelApiList())
                        shimmer.visibility = View.GONE
                    } else {
                        txtListEmpty.visibility = View.VISIBLE
                    }
                }
                StatusRequest.FAILURE -> {
                    txtListEmpty.visibility = View.VISIBLE
                }
            }
        }
    }
}

fun FragmentHome.initRecycler(list: ArrayList<ModelApiItem>) = with(mBinding) {
    recyclerView.adapter = myAdapter
    myAdapter.submitList(list)
    val placesVisited = list.count { !it.visited }
    txtPlaceToVisit.text = getString(R.string.txt_visit, placesVisited.toString())
    if (placesVisited == 0) {
        mBinding.alert.visibility = View.VISIBLE
        mBinding.recyclerView.visibility = View.GONE
    }else{
        mBinding.recyclerView.visibility = View.VISIBLE
    }
    myAdapter.onItemClickListener = { model ->
        viewModel.setSelectedPlace(model)
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentMap)
    }
}
