package com.quoinsight.tv

@kotlinx.serialization.Serializable // ?? must add this to be compatible with kotlinx.serialization.json.Json.decodeFromString<>()
data class TestDataClass(val s: String, val i: Int)
