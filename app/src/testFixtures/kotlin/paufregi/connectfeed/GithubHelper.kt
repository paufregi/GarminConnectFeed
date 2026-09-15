package paufregi.connectfeed

import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import paufregi.connectfeed.data.api.github.models.Asset
import paufregi.connectfeed.data.api.github.models.Release

val githubRelease = Release(
    tagName = "v2.2.2",
    assets = listOf(
        Asset(
            contentType="application/vnd.android.package-archive",
            downloadUrl = "https://github.com/paufregi/GarminConnectFeed/releases/download/v2.2.2/ConnectFeed-v2.2.2.apk"
        )
    )
)

val githubLatestReleaseJson = """
    {
      "url": "https://api.github.com/repos/paufregi/GarminConnectFeed/releases/218432575",
      "assets_url": "https://api.github.com/repos/paufregi/GarminConnectFeed/releases/218432575/assets",
      "upload_url": "https://uploads.github.com/repos/paufregi/GarminConnectFeed/releases/218432575/assets{?name,label}",
      "html_url": "https://github.com/paufregi/GarminConnectFeed/releases/tag/v2.2.2",
      "id": 218432575,
      "author": {
        "login": "paufregi",
        "id": 17803128,
        "node_id": "MDQ6VXNlcjE3ODAzMTI4",
        "avatar_url": "https://avatars.githubusercontent.com/u/17803128?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/paufregi",
        "html_url": "https://github.com/paufregi",
        "followers_url": "https://api.github.com/users/paufregi/followers",
        "following_url": "https://api.github.com/users/paufregi/following{/other_user}",
        "gists_url": "https://api.github.com/users/paufregi/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/paufregi/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/paufregi/subscriptions",
        "organizations_url": "https://api.github.com/users/paufregi/orgs",
        "repos_url": "https://api.github.com/users/paufregi/repos",
        "events_url": "https://api.github.com/users/paufregi/events{/privacy}",
        "received_events_url": "https://api.github.com/users/paufregi/received_events",
        "type": "User",
        "user_view_type": "public",
        "site_admin": false
      },
      "node_id": "RE_kwDOK_u9UM4NBQQ_",
      "tag_name": "v2.2.2",
      "target_commitish": "main",
      "name": "v2.2.2",
      "draft": false,
      "immutable": false,
      "prerelease": false,
      "created_at": "2025-05-14T01:45:14Z",
      "updated_at": "2025-05-14T01:50:25Z",
      "published_at": "2025-05-14T01:50:25Z",
      "assets": [
        {
          "url": "https://api.github.com/repos/paufregi/GarminConnectFeed/releases/assets/254438203",
          "id": 254438203,
          "node_id": "RA_kwDOK_u9UM4PKms7",
          "name": "ConnectFeed-v2.2.2.apk",
          "label": null,
          "uploader": {
            "login": "paufregi",
            "id": 17803128,
            "node_id": "MDQ6VXNlcjE3ODAzMTI4",
            "avatar_url": "https://avatars.githubusercontent.com/u/17803128?v=4",
            "gravatar_id": "",
            "url": "https://api.github.com/users/paufregi",
            "html_url": "https://github.com/paufregi",
            "followers_url": "https://api.github.com/users/paufregi/followers",
            "following_url": "https://api.github.com/users/paufregi/following{/other_user}",
            "gists_url": "https://api.github.com/users/paufregi/gists{/gist_id}",
            "starred_url": "https://api.github.com/users/paufregi/starred{/owner}{/repo}",
            "subscriptions_url": "https://api.github.com/users/paufregi/subscriptions",
            "organizations_url": "https://api.github.com/users/paufregi/orgs",
            "repos_url": "https://api.github.com/users/paufregi/repos",
            "events_url": "https://api.github.com/users/paufregi/events{/privacy}",
            "received_events_url": "https://api.github.com/users/paufregi/received_events",
            "type": "User",
            "user_view_type": "public",
            "site_admin": false
          },
          "content_type": "application/vnd.android.package-archive",
          "state": "uploaded",
          "size": 7446538,
          "digest": null,
          "download_count": 3,
          "created_at": "2025-05-14T01:49:05Z",
          "updated_at": "2025-05-14T01:49:09Z",
          "browser_download_url": "https://github.com/paufregi/GarminConnectFeed/releases/download/v2.2.2/ConnectFeed-v2.2.2.apk"
        }
      ],
      "tarball_url": "https://api.github.com/repos/paufregi/GarminConnectFeed/tarball/v2.2.2",
      "zipball_url": "https://api.github.com/repos/paufregi/GarminConnectFeed/zipball/v2.2.2",
      "body": "### v2.2.2\r\n\r\nThis release enhances the Renpho reader by implementing robust error handling for incompatible file formats. \r\nUsers will now receive an informative error message instead of experiencing an unexpected crash."
    }
""".trimIndent()

const val githubPort = 8085

val githubDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.url.encodedPath
        return when {
            path == "/repos/paufregi/GarminConnectFeed/releases/latest" && request.method == "GET" ->
                MockResponse(code = 200, body = githubLatestReleaseJson)

            else -> MockResponse(404)
        }
    }
}