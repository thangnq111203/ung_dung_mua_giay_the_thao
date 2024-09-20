import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.TlsVersion
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class HttpProvider {

    companion object {
        suspend fun sendPost(url: String, formBody: RequestBody): JSONObject? {
            return withContext(Dispatchers.IO) {
                try {
                    val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                        )
                        .build()

                    val client: OkHttpClient = OkHttpClient.Builder()
                        .connectionSpecs(listOf(spec))
                        .callTimeout(5000, TimeUnit.MILLISECONDS)
                        .build()

                    val request: Request = Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(formBody)
                        .build()

                    val response: Response = client.newCall(request).execute()
                    val responseBodyString = response.body?.string()

                    if (!response.isSuccessful) {
                        Log.e("HttpProvider", "Request failed: ${response.message}")
                        null
                    } else {
                        responseBodyString?.let {
                            try {
                                JSONObject(it)
                            } catch (e: JSONException) {
                                Log.e("HttpProvider", "Failed to parse JSON: ${e.message}", e)
                                null
                            }
                        }
                    }
                } catch (e: IOException) {
                    Log.e("HttpProvider", "IOException: ${e.message}", e)
                    null
                }
            }
        }
    }
}
