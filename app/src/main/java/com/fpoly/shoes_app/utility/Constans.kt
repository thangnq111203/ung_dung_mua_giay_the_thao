package com.fpoly.shoes_app.utility

const val BASE_URL = "http://192.168.88.152:3000/api/"
const val SET_TIME_OUT_API = 0L

const val ITEM_MORE = "Thêm"
const val GET_ALL_POPULAR_SHOES = "Phổ biến nhất"
const val GET_POPULAR_SHOES_ALL = "Tất cả"
const val SPAN_COUNT_CATEGORIES = 4

const val Active = 0
const val InActive = 1
const val Sold = 2

const val ADDRESS_DEFAULT = "0"
const val ADDRESS_NORMAL = "1"

const val HA_NOI = "hanoi"

const val IS_PRICE_NULL = "-"

const val IS_100 = 100L

const val REDUCE = 0
const val PLUS = 1

object RequestKey {
    const val SHIPPING_CHECKOUT_REQUEST_KEY = "shipping_checkout_request_key"
    const val DISCOUNT_CHECKOUT_REQUEST_KEY = "discount_checkout_request_key"
    const val ADDRESS_CHECKOUT_REQUEST_KEY = "address_checkout_request_key"
    const val RELOAD_ADDRESS_CHECKOUT_REQUEST_KEY = "reload_address_checkout_request_key"
    const val SHOW_ALERT_DIALOG_CHECKOUT_REQUEST_KEY = "show_alert_dialog_checkout_request_key"
    const val KEY_FILTER_REQUEST_KEY = "key_filter_request_key"
    const val RELOAD_CART_REQUEST_KEY = "reload_cart_request_key"
}

object ResultKey {
    const val SHIPPING_CHECKOUT_RESULT_KEY = "shipping_checkout_result_key"
    const val DISCOUNT_CHECKOUT_RESULT_KEY = "discount_checkout_result_key"
    const val ADDRESS_CHECKOUT_RESULT_KEY = "address_checkout_result_key"
    const val RELOAD_ADDRESS_CHECKOUT_RESULT_KEY = "reload_address_checkout_result_key"
    const val SHOW_ALERT_DIALOG_CHECKOUT_RESULT_KEY = "show_alert_dialog_checkout_result_key"
    const val KEY_FILTER_RESULT_KEY = "key_filter_result_key"
    const val RELOAD_CART_RESULT_KEY = "reload_cart_filter_result_key"
}

object ZaloPay {
    const val APP_ID: Int = 2553
    const val MAC_KEY: String = "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL"
    const val BASE_URL_ZALO_PAY = "https://sandbox.zalopay.com.vn/v001/tpe/createorder"
}

object SortText {
    const val MOST_RECENT = 0
    const val POPULAR = 1
    const val PRICE_HIGH = 2
    const val PRICE_LOW = 3
    const val RATING = 4
}

object RatingText {
    const val RATING_5 = 5
    const val RATING_4 = 4
    const val RATING_3 = 3
    const val RATING_2 = 2
    const val RATING_1 = 1
    const val RATING_ALL = 0
}