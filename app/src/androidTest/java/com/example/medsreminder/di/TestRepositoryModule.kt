package com.example.medsreminder.di

import com.example.medsreminder.data.repository.AuthRepository
import com.example.medsreminder.data.repository.fake.FakeAuthRepository
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

}