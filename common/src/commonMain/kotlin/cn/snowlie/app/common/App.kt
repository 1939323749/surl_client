package cn.snowlie.app.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking

@Composable
fun App() {
    var text by remember { mutableStateOf("") }
    var shortUrl by remember { mutableStateOf("") }
    var clickState by remember { mutableStateOf(false) }

    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { newtext ->
                text = newtext
            },
            label = { Text("Input URL") }
        )
        Button(onClick = {
            clickState = !clickState
        }) {
            Text("shortUrl")
        }
        if (shortUrl.isNotEmpty()) {
            TextField(
                value = shortUrl,
                onValueChange = { newtext ->
                    shortUrl = newtext
                },
                label = { Text("shortUrl") }
            )
        }
    }
    LaunchedEffect(clickState) {
        if (clickState) {
            shortUrl = getShortUrl(text)
        }
    }
}

data class Url(val url: String)

@OptIn(InternalAPI::class)
private suspend fun getShortUrl(text: String): String = runBlocking {
    try {
        val url = Url("https://snowlie.cn")
        val responsePost: HttpResponse = HttpClient().use { client ->
            client.post(url.url) {
                url {
                    path("create")
                }
                contentType(ContentType.Application.Json)
                body = MultiPartFormDataContent(formData {
                    append("url", text)
                })
            }
        }
        val responseString = responsePost.bodyAsText()

        url.url + "/" + responseString
            .replace("\"", "")
            .replace("{", "")
            .replace("}", "")
            .replace("shortUrl:", "")
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
