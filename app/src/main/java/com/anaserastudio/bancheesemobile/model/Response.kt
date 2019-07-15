package com.anaserastudio.bancheesemobile.model
import com.google.gson.JsonElement
import java.io.Serializable

class Response(var STATUS: String, var MESSAGE: String, var CODE: Int, var DATA: JsonElement) : Serializable
