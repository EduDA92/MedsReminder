package com.example.medsreminder.di

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.data.repository.fake.FakeAuthRepository
import com.example.medsreminder.data.repository.fake.FakeMedicineTakingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.testing.TestInstallIn
import dagger.hilt.components.SingletonComponent


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
interface TestRepositoryModule {

    @Binds
    fun bindsFakeAuthRepository(
        authRepository: FakeAuthRepository
    ): AuthRepository

    @Binds
    fun bindsMedicineTakingRepository(
        medicineTakingRepository: FakeMedicineTakingRepository
    ): MedicineTakingRepository

}