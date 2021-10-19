package com.example.newspaper

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var pageNumber = 1
    private var list = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            list = mutableListOf()
            sendRequest()
        }

        btn_load.setOnClickListener {

            pageNumber += 1
            sendRequest()
        }
    }

    private fun getUrl(): String {

        val word = search_edit_text.text
        val apiKey = "9856e7ba-762f-4bb8-9985-3727ced99ae9"
        val pageSize = 10
        return "https://content.guardianapis.com/$word?page=$pageNumber&page-size=$pageSize&api-key=$apiKey"

    }

    private fun extractJSON(response: String) {

        val jsonObject = JSONObject(response)
        val jsonResponseBody = jsonObject.getJSONObject("response")
        val result = jsonResponseBody.getJSONArray("results")

        for (i in 0..9) {
            val item = result.getJSONObject(i)
            val webTitle = item.getString("webTitle")
            val webUrl = item.getString("webUrl")
            val data = Data(webTitle, webUrl)
            list.add(data)
        }

        val adapter = NewsAdapter(list)
        list_view.adapter = adapter
    }

    private fun sendRequest() {

        val url = getUrl()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    extractJSON(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()

            })
        queue.add(stringRequest)

    }
}