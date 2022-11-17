package com.kismi.photodog.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.kismi.photodog.data.DogRepository

class DogGalleryViewModel @ViewModelInject constructor(
    private val repository: DogRepository,
    @Assisted state: SavedStateHandle //pada bagian ini memungkinkan untuk menyimpan potongan data melalui proses mati dan dapat dipulihkan setelahya.

) : ViewModel() {
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)


    val photos = currentQuery.switchMap { queryString ->

        //bagian ini digunakan untuk mencegah himpitan saat memutar perangkat
    repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "dogs"
    }

}