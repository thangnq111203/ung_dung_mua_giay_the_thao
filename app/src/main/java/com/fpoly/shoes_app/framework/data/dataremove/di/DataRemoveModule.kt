package com.fpoly.shoes_app.framework.data.dataremove.di

import com.fpoly.shoes_app.framework.data.dataremove.api.BannerApi
import com.fpoly.shoes_app.framework.data.dataremove.api.CartApi
import com.fpoly.shoes_app.framework.data.dataremove.api.CategoriesApi
import com.fpoly.shoes_app.framework.data.dataremove.api.DiscountApi
import com.fpoly.shoes_app.framework.data.dataremove.api.FavoriteApi
import com.fpoly.shoes_app.framework.data.dataremove.api.OrderApi
import com.fpoly.shoes_app.framework.data.dataremove.api.ReviewApi
import com.fpoly.shoes_app.framework.data.dataremove.api.ShoesApi
import com.fpoly.shoes_app.framework.data.dataremove.api.deleteInterface.DeleteaddressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.AlladdressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.NotificationsInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.OrderApiService
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.OrderRepository
import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.ProfileInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.AddAddressInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CancleInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ConfirmTakeInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.CreateNewPassInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.ForgotMailInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.LoginInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.OTPConfirmInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.RateInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SetUpInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.SignUpInterface
import com.fpoly.shoes_app.framework.data.dataremove.api.postInterface.UpdateAddressInterface
import com.fpoly.shoes_app.utility.BASE_URL
import com.fpoly.shoes_app.utility.SET_TIME_OUT_API
import com.fpoly.shoes_app.framework.repository.OrderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataRemoveModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .writeTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .readTimeout(SET_TIME_OUT_API, TimeUnit.SECONDS)
                    .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
                    .addInterceptor(httpLoggingInterceptor)
                    .build()
            )
            .build()

    @Provides
    @Singleton
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi =
        retrofit.create(CategoriesApi::class.java)

    @Provides
    @Singleton
    fun provideShoesApi(retrofit: Retrofit): ShoesApi =
        retrofit.create(ShoesApi::class.java)

    @Provides
    @Singleton
    fun provideBannerApi(retrofit: Retrofit): BannerApi =
        retrofit.create(BannerApi::class.java)

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginInterface =
        retrofit.create(LoginInterface::class.java)

    @Provides
    @Singleton
    fun provideForgotmailApi(retrofit: Retrofit): ForgotMailInterface =
        retrofit.create(ForgotMailInterface::class.java)

    @Provides
    @Singleton
    fun provideOTPConfirmApi(retrofit: Retrofit): OTPConfirmInterface =
        retrofit.create(OTPConfirmInterface::class.java)

    @Provides
    @Singleton
    fun provideSignUpmApi(retrofit: Retrofit): SignUpInterface =
        retrofit.create(SignUpInterface::class.java)

    @Provides
    @Singleton
    fun provideSetUpmApi(retrofit: Retrofit): SetUpInterface =
        retrofit.create(SetUpInterface::class.java)

    @Provides
    @Singleton
    fun provideNewPassWordApi(retrofit: Retrofit): CreateNewPassInterface =
        retrofit.create(CreateNewPassInterface::class.java)

    @Provides
    @Singleton
    fun provideFavoriteApi(retrofit: Retrofit): FavoriteApi =
        retrofit.create(FavoriteApi::class.java)

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi =
        retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideDiscountApi(retrofit: Retrofit): DiscountApi =
        retrofit.create(DiscountApi::class.java)

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
        retrofit.create(OrderApi::class.java)

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi =
        retrofit.create(ReviewApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileInterface =
        retrofit.create(ProfileInterface::class.java)

    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit): AlladdressInterface =
        retrofit.create(AlladdressInterface::class.java)

    @Provides
    @Singleton
    fun proviDedeleteAddressApi(retrofit: Retrofit): DeleteaddressInterface =
        retrofit.create(DeleteaddressInterface::class.java)

    @Provides
    @Singleton
    fun provideAddAddressApi(retrofit: Retrofit): AddAddressInterface =
        retrofit.create(AddAddressInterface::class.java)

    @Provides
    @Singleton
    fun provideUpdateAddressApi(retrofit: Retrofit): UpdateAddressInterface =
        retrofit.create(UpdateAddressInterface::class.java)
    @Provides
    @Singleton
    fun provideActiveApi(retrofit: Retrofit): OrderApiService =
        retrofit.create(OrderApiService::class.java)


    @Provides
    fun provideActiveRepository(orderApiService: OrderApiService): OrderRepository =
        OrderRepositoryImpl(orderApiService)

    @Provides
    @Singleton
    fun provideRateApi(retrofit: Retrofit): RateInterface =
        retrofit.create(RateInterface::class.java)
    @Provides
    @Singleton
    fun provideConfirmTakeApi(retrofit: Retrofit): ConfirmTakeInterface =
        retrofit.create(ConfirmTakeInterface::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationsInterface =
        retrofit.create(NotificationsInterface::class.java)

 @Provides
    @Singleton
    fun provideCancleApi(retrofit: Retrofit): CancleInterface =
        retrofit.create(CancleInterface::class.java)


}