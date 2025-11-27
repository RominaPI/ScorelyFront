package com.example.scorly.Models

import com.google.gson.annotations.SerializedName

data class LigasResponse(
    val success: Boolean,
    val data: List<LigaData>,
    val total: Int
)

data class LigaData(
    @SerializedName("liga_id") val id: Int,
    val nombre: String,
    @SerializedName("logo_url") val logoUrl: String?,
    val pais: String
)