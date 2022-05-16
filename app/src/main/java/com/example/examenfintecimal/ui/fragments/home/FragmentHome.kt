package com.example.examenfintecimal.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.examenfintecimal.databinding.FragmentHomeBinding
import com.example.examenfintecimal.room.AppDataBase
import com.example.examenfintecimal.viewmodel.MainViewModel


class FragmentHome : Fragment() {

    private var _mBinding: FragmentHomeBinding? = null
    val mBinding get() = _mBinding!!
    val viewModel by activityViewModels<MainViewModel>()
    val myAdapter: PlaceAdapter by lazy { PlaceAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initElement()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }

    fun searchPlace(search: String) {
        val list = viewModel.getPlaces().filter {
            it.suburb.lowercase().contains(search.lowercase())
        }
        myAdapter.submitList(list)
        mBinding.recyclerView.adapter = myAdapter
    }
}