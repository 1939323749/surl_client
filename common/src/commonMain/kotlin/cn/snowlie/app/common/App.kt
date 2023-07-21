package cn.snowlie.app.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class UrlResponse(
    val ShortUrl: String,
    val LongUrl: String,
    val ClickCount: Int
)
@Composable
fun App() {
    var text by remember { mutableStateOf("") }
    var shortUrl by remember { mutableStateOf("") }
    var clickState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(3f)
                .align(Alignment.CenterHorizontally)
        )
        {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    value = text,
                    onValueChange = { newtext ->
                        text = newtext
                    },
                    label = { Text("Input URL") },
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            text = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Clear",
                            tint = Color.Gray
                        )
                    },
                )
                Button(
                    onClick = {
                        clickState = !clickState
                    },
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text("shortUrl")
                }

                if (shortUrl.isNotEmpty()) {
                    Column {
                        Text(
                            text = "Short Url",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                        SelectionContainer {
                            Text(
                                text = shortUrl,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }

                }
            }
            LaunchedEffect(clickState) {
                if (clickState&&text.isNotEmpty()) {
                    shortUrl = getShortUrl(text)
                }
            }
            }
        Spacer(modifier = Modifier.weight(1f))
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
        "https://snowlie.cn/"+Json.decodeFromString<UrlResponse>(responseString).ShortUrl
    } catch (e: Exception) {
        e.printStackTrace()
        "error occurred"
    }
}
