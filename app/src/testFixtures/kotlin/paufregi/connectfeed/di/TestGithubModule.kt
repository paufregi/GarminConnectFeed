package paufregi.connectfeed.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import paufregi.connectfeed.data.api.github.Github
import paufregi.connectfeed.githubPort
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GithubModule::class]
)
object TestGithubModule {

    @Provides
    @Singleton
    @Named("GithubUrl")
    fun provideGithubUrl(): String = "https://localhost:${githubPort}/"

    @Provides
    @Singleton
    fun provideGithub(
        @Named("GithubUrl") url: String,
    ): Github = Github.client(url)
}