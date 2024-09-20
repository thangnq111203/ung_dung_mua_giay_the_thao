package com.fpoly.shoes_app.framework.presentation.ui.profile.general_setting

import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentGeneralSettingBinding
import com.fpoly.shoes_app.framework.adapter.notification.NotificationAdapter
import com.fpoly.shoes_app.framework.domain.model.profile.notification.Notification
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.S)
@AndroidEntryPoint
class GeneralSettingFragment : BaseFragment<FragmentGeneralSettingBinding, GeneralSettingViewModel>(
    FragmentGeneralSettingBinding::inflate, GeneralSettingViewModel::class.java
) {
    private lateinit var audioManager: AudioManager
    private val notifications by lazy {
        listOf(
            Notification(
                getString(R.string.general_notification),
                sharedPreferences.getNotificationModeState()
            ),
            Notification(getString(R.string.sound), sharedPreferences.getSoundModeState()),
            Notification(getString(R.string.vibrate), sharedPreferences.getVibrateModeState()),
            Notification(getString(R.string.app_updates), false),
            Notification(getString(R.string.new_service_available), false),
            Notification(getString(R.string.new_tips_available), false)
        )
    }
    private val adapter by lazy {
        NotificationAdapter(notifications) { item, isChecked ->
            when (item.title) {
                getString(R.string.general_notification) -> {
                    sharedPreferences.saveNotificationModeState(isChecked)
                    if (isChecked) service.playNotificationSound(
                        requireContext(),
                        "Thông Báo Được Bật",
                        ""
                    )
                }

                getString(R.string.sound) -> {
                    sharedPreferences.saveSoundModeState(isChecked)
                    if (isChecked) service.playCustomSound(requireContext())
                }

                getString(R.string.vibrate) -> {
                    sharedPreferences.saveVibrateModeState(isChecked)
                    if (isChecked) service.triggerVibration(requireContext())
                }
            }
        }
    }

    override fun setupPreViews() {
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.general_notification)
            recyclerViewNotifications.adapter = adapter
            recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun bindViewModel() {
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }
    }
}