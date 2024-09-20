package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ConfirmTakeInterface
import javax.inject.Inject

class ConfirmTakeRepository @Inject constructor(
    private val confirmTakeInterface: ConfirmTakeInterface
) {
    suspend fun confirmTakrRepon(id:String) = confirmTakeInterface.confirmTake(id)
}