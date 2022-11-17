package com.kismi.photodog.api

import com.kismi.photodog.data.DogImages

data class SearchResponse (
    val results: List<DogImages>
)