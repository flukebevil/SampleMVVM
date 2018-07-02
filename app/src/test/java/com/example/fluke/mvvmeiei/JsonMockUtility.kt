package de.riderr.app

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class JsonMockUtility {

    fun <T> getJsonToMock(fileName: String, className: Class<T>): T =
            GsonBuilder().create().fromJson<T>(getJsonFromResources(fileName), className)

    fun <T> getJsonToMock(fileName: String, type: Type): T =
            GsonBuilder().create().fromJson<T>(getJsonFromResources(fileName), type)

    fun getJsonFromResources(fileName: String): String =
            this.javaClass.classLoader.getResourceAsStream(fileName).bufferedReader().use { it.readText() }
}