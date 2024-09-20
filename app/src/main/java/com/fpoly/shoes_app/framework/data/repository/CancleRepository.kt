package com.fpoly.shoes_app.framework.data.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CancleInterface
import com.fpoly.shoes_app.framework.domain.model.CancelReason
import javax.inject.Inject

class CancleRepository @Inject constructor(
    private val cancleInterface: CancleInterface
) {
    suspend fun cancleRepon(id:String,cancelReason:CancelReason) = cancleInterface.cancelOrder(id,cancelReason)
}