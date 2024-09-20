package com.fpoly.shoes_app.framework.data.datalocal.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.service.ServiceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataLocalModule {

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE)

    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

    @Provides
    fun provideSharedPreferencesManager(
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): SharedPreferencesManager =
        SharedPreferencesManager.apply {
            this.sharedPreferences = sharedPreferences
            this.editor = editor
        }

    @Provides
    fun provideServiceUtil(): ServiceUtil {
        return ServiceUtil
    }

}