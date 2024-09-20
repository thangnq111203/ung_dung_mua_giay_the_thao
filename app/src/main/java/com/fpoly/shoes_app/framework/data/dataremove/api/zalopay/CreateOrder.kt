package com.example.zalopaykotlin.Api

import HttpProvider
import com.fpoly.shoes_app.utility.Helper.Helpers
import com.fpoly.shoes_app.utility.ZaloPay
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.Date

class CreateOrder {
    private class CreateOrderData constructor(amount: String) {
        var AppId: String
        var AppUser: String
        var AppTime: String
        var Amount: String
        var AppTransId: String
        var EmbedData: String
        var Items: String
        var BankCode: String
        var Description: String
        var Mac: String

        init {
            val appTime = Date().time
            AppId = java.lang.String.valueOf(ZaloPay.APP_ID)
            AppUser = "Android_Demo"
            AppTime = appTime.toString()
            Amount = amount
            AppTransId = Helpers.getAppTransId()
            EmbedData = "{}"
            Items = "[]"
            BankCode = "zalopayapp"
            Description = "Thanh toán đơn hàng cho Shoe Bee"
            val inputHMac = String.format(
                "%s|%s|%s|%s|%s|%s|%s",
                AppId,
                AppTransId,
                AppUser,
                Amount,
                AppTime,
                EmbedData,
                Items
            )
            Mac = Helpers.getMac(ZaloPay.MAC_KEY, inputHMac)
        }
    }

    @Throws(Exception::class)
    suspend fun createOrder(amount: String): JSONObject? {
        val input = CreateOrderData(amount)
        val formBody: RequestBody = FormBody.Builder()
            .add("appid", input.AppId)
            .add("appuser", input.AppUser)
            .add("apptime", input.AppTime)
            .add("amount", input.Amount)
            .add("apptransid", input.AppTransId)
            .add("embeddata", input.EmbedData)
            .add("item", input.Items)
            .add("bankcode", input.BankCode)
            .add("description", input.Description)
            .add("mac", input.Mac)
            .build()
        return HttpProvider.sendPost(ZaloPay.BASE_URL_ZALO_PAY, formBody)
    }
}