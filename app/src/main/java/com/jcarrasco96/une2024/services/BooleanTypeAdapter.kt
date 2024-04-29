package com.jcarrasco96.une2024.services

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class BooleanTypeAdapter : JsonDeserializer<Boolean> {

    override fun deserialize(
        json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?
    ): Boolean {
        if (json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
            return json.asInt == 1
        }
        throw JsonParseException("Expected a boolean but was ${json.javaClass.simpleName}")
    }

}