package com.kismi.photodog.data

import androidx.paging.PagingSource
import com.kismi.photodog.api.DogApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class DogPagingSource(
    private val dogApi: DogApi,
    private val query: String

// bagian ini digunakan untuk mengubah data menjadi halaman

) : PagingSource<Int, DogImages>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DogImages> {

        //bagian ini digunakan untuk menetapkan halaman mana yang akan dimuat
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = dogApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }


    }


}