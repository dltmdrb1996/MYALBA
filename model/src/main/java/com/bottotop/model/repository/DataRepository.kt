package com.bottotop.model.repository

import com.bottotop.model.Sample
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getUser(id : String , test : String) : Sample
}