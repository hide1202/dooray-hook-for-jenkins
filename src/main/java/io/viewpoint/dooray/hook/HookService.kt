package io.viewpoint.dooray.hook

import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import hudson.model.TaskListener

class HookService(val listener: TaskListener?) {
    fun hook(url: String, hook: Hook) {
        val response = url.httpPost()
                .header(mapOf("Content-Type" to "application/json"))
                .body(Gson().toJson(hook))
                .response()

        val logger = listener?.logger
        if(logger != null) {
            logger.println("Hook Request info")
            logger.println("\tURL : $url")
            logger.println("\tHook : $hook")
            logger.println("Hook Response : HTTP ${response.second.statusCode}")
        }
    }
}