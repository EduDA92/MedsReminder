package com.example.medsreminder.di

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.data.repository.DefaultAuthRepository
import com.example.medsreminder.data.repository.DefaultMedicineTakingRepository
import com.example.medsreminder.data.repository.MedicineTakingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsAuthRepository(
        authRepository: DefaultAuthRepository
    ): AuthRepository

    @Binds
    fun bindsMedicineTakingRepository(
        medicineTakingRepository: DefaultMedicineTakingRepository
    ): MedicineTakingRepository

}