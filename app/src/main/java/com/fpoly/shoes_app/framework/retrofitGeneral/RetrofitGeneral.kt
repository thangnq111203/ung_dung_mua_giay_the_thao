package com.fpoly.shoes_app.framework.retrofitGeneral

import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitGeneral {
        private const val BASE_URL = "http://192.168.88.124:8000/api/"
        val retrofitInstance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

object PolyUtil {
    /**
     * Decodes an encoded path string into a list of LatLng points.
     *
     * @param encodedPath The encoded path string.
     * @return A list of LatLng points representing the decoded path.
     */
    fun decode(encodedPath: String): List<LatLng> {
        val path = ArrayList<LatLng>()
        var index = 0
        val length = encodedPath.length
        var lat = 0
        var lng = 0

        while (index < length) {
            var result = 1
            var shift = 0
            var b: Int
            do {
                b = encodedPath[index++].code - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            result = 1
            shift = 0
            do {
                b = encodedPath[index++].code - 63 - 1
                result += b shl shift
                shift += 5
            } while (b >= 0x1f)
            lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            path.add(LatLng(lat * 1e-5, lng * 1e-5))
        }

        return path
    }
}