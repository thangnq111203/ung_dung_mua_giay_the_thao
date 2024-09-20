package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentNotificationHomeBinding
import com.fpoly.shoes_app.framework.adapter.notification.NotificationsHomeAdapter
import com.fpoly.shoes_app.framework.adapter.notification.NotificationsItem
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class NotificationHomeFragment : BaseFragment<FragmentNotificationHomeBinding, NotificationHomeViewModel>(
    FragmentNotificationHomeBinding::inflate,
    NotificationHomeViewModel::class.java
) {
    private lateinit var idUser: String
    private lateinit var notificationsAdapter: NotificationsHomeAdapter
    private var isFetching = false

    override fun setupPreViews() {
        idUser = sharedPreferences.getIdUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupViews() {
        notificationsAdapter = NotificationsHomeAdapter()
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                notificationsAdapter.submitList(emptyList()) // Clear the list before loading new data
                viewModel.fetchNotifications(idUser)
            }
            headerLayout.tvTitle.text = getString(R.string.general_notification)
            recycViewNotifications.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationsAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                        if (!binding.swipeRefreshLayout.isRefreshing && !isFetching) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                                isFetching = true
                                viewModel.fetchNotifications(idUser) // Trigger load more
                            }
                        }
                    }
                })
            }
        }
        viewModel.fetchNotifications(idUser)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.notificationsState.collect { state ->
                binding.swipeRefreshLayout.isRefreshing = state.isLoading
                isFetching = state.isLoading

                if (!state.isLoading) {
                    val groupedNotifications = groupNotificationsByDate(state.notifications)
                    notificationsAdapter.appendData(groupedNotifications)
                }

                state.errorMessage?.let {
                    showError(it)
                }
            }
        }
    }

    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupNotificationsByDate(notifications: List<NotificationsHome>): List<NotificationsItem> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
        val currentDate = LocalDate.now()
        val groupedNotifications = mutableListOf<NotificationsItem>()
        val grouped = notifications.groupBy { notification ->
            try {
                val notificationDate = LocalDateTime.parse(notification.time, formatter).toLocalDate()
                when {
                    notificationDate.isEqual(currentDate) -> "Today"
                    notificationDate.isEqual(currentDate.minusDays(1)) -> "Yesterday"
                    else -> notificationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
            } catch (e: Exception) {
                "Unknown Date"
            }
        }.toSortedMap(compareByDescending { header ->
            when (header) {
                "Today" -> currentDate
                "Yesterday" -> currentDate.minusDays(1)
                "Unknown Date" -> LocalDate.MIN // Place unknown dates at the end
                else -> {
                    try {
                        LocalDate.parse(header, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } catch (e: Exception) {
                        LocalDate.MIN // Handle invalid date format
                    }
                }
            }
        })

        // Add headers and notifications to the list
        grouped.forEach { (header, notificationList) ->
            groupedNotifications.add(NotificationsItem.Header(header)) // Add header (date)
            groupedNotifications.addAll(notificationList.map { NotificationsItem.NotificationItem(it) }) // Add notifications under the header
        }

        return groupedNotifications
    }

    private fun showError(message: String) {
        showProgressbar(false)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
