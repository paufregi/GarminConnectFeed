package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.githubRelease
import retrofit2.Response
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GithubModule::class]
)
object TestGithubModule {

    @Provides
    @Singleton
    fun provideGithub(): Github = object : Github {
        override suspend fun getLatestRelease() = Response.success(githubRelease)
    }
}