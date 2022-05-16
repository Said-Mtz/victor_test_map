package com.example.examenfintecimal.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.examenfintecimal.core.api.ApiPlaceService
import com.example.examenfintecimal.core.api.RetrofitService
import com.example.examenfintecimal.core.model.ModelApiItem
import com.example.examenfintecimal.core.model.ModelApiList
import com.example.examenfintecimal.core.model.convertToString
import com.example.examenfintecimal.room.AppDataBase
import com.example.examenfintecimal.room.PlaceEntity
import com.example.examenfintecimal.room.placesEntityToModelApiList
import com.example.examenfintecimal.ui.StatusRequest
import com.example.examenfintecimal.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val retrofit = RetrofitService.instance()
    private val apiService: ApiPlaceService = retrofit.create(
        ApiPlaceService::
        class.java
    )

    lateinit var database: AppDataBase

    val placesData: LiveData<Pair<StatusRequest, ModelApiList?>>
        get() = _placesData
    private val _placesData = MutableLiveData<Pair<StatusRequest, ModelApiList?>>()

    private lateinit var modelItemSelected: ModelApiItem

    fun setSelectedPlace(modelItem: ModelApiItem) {
        modelItemSelected = modelItem
    }

    fun getSelectedPlace() = modelItemSelected

    fun getPlaces() = _placesData.value?.second ?: listOf()

    fun updatePlace(modelItem: ModelApiItem = getSelectedPlace()) {
        viewModelScope.launch(Dispatchers.IO) {
            database.placeDao.update(
                PlaceEntity(
                    id = modelItem.id,
                    location = modelItem.location.convertToString(),
                    streetName = modelItem.streetName,
                    suburb = modelItem.suburb,
                    visited = true
                )
            )
        }
    }

    fun requestPlaces(context: Context, finished: () -> Unit = {}) {
        database = Room.databaseBuilder(
            context,
            AppDataBase::class.java, AppDataBase.DATABASE_NAME
        ).build()
        _placesData.value = Pair(StatusRequest.LOADING, null)

        viewModelScope.launch(Dispatchers.IO) {
            with(database.placeDao.getAllPlace()) {
                if (isNotEmpty()) {
                    _placesData.postValue(
                        Pair(
                            StatusRequest.SUCCESS,
                            placesEntityToModelApiList()
                        )
                    )
                    finished.invoke()
                    "Se obtuvo de manera local".log("REQUEST")
                    return@launch
                }
            }
            "Se obtuvo de manera remota".log("REQUEST")
            apiService.obtenerPlace().enqueue(object : Callback<ModelApiList> {
                override fun onResponse(
                    call: Call<ModelApiList>,
                    response: Response<ModelApiList>
                ) {
                    if (response.code() in 200..204) {
                        val remoteList = response.body() ?: ModelApiList()
                        remoteList.onEachIndexed { index, modelApiItem ->
                            remoteList[index] = modelApiItem.copy(id = index + 1)
                        }
                        _placesData.postValue(
                            Pair(StatusRequest.SUCCESS, remoteList)
                        )
                        savePlaces(response.body() ?: ModelApiList())
                    } else {
                        _placesData.postValue(
                            Pair(StatusRequest.FAILURE, null)
                        )
                    }
                }

                override fun onFailure(call: Call<ModelApiList>, t: Throwable) {
                    _placesData.postValue(
                        Pair(StatusRequest.FAILURE, null)
                    )
                    Log.e("Error", "Retrofit")
                }

            })
        }
    }

    private fun savePlaces(modelApiList: ModelApiList) {

        viewModelScope.launch(Dispatchers.IO) {
            modelApiList.forEachIndexed { index, modelApiItem ->
                database.placeDao.insert(
                    PlaceEntity(
                        id = index + 1,
                        location = modelApiItem.location.convertToString(),
                        streetName = modelApiItem.streetName,
                        suburb = modelApiItem.suburb,
                        visited = false
                    )
                )
            }

        }

    }
}