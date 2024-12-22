package paufregi.connectfeed

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import paufregi.connectfeed.core.models.Credential
import paufregi.connectfeed.data.api.models.OAuth1
import paufregi.connectfeed.data.api.models.OAuth2
import paufregi.connectfeed.data.api.models.OAuthConsumer
import paufregi.connectfeed.test.R
import java.util.Date


fun createOAuth2(expiresAt: Date) = OAuth2(
    scope = "SCOPE",
    jti = "JTI",
    accessToken = JWT.create().withExpiresAt(expiresAt).sign(Algorithm.none()),
    tokenType = "TOKEN_TYPE",
    refreshToken = "REFRESH_TOKEN",
    expiresIn = 0,
    refreshTokenExpiresIn = 0
)

//1 Day  : 1000 * 60 * 60 * 24 milliseconds
val tomorrow = Date(Date().time + (1000 * 60 * 60 * 24))

val cred = Credential(username = "user", password = "pass")
val consumer = OAuthConsumer("CONSUMER_KEY", "CONSUMER_SECRET")
val consumerBody = """{"consumer_key":"${consumer.key}","consumer_secret":"${consumer.secret}"}"""
val oauth1 = OAuth1("OAUTH_TOKEN", "OAUTH_SECRET")
val oauth1Body = "oauth_token=${oauth1.token}&oauth_token_secret=${oauth1.secret}"
val oauth2 = createOAuth2(tomorrow)
val oauth2Body = """{"scope": "${oauth2.scope}","jti": "${oauth2.jti}","access_token": "${oauth2.accessToken}","token_type": "${oauth2.tokenType}","refresh_token": "${oauth2.refreshToken}","expires_in": ${oauth2.expiresIn},"refresh_token_expires_in": ${oauth2.refreshTokenExpiresIn}}"""

val htmlForCSRF = """
        <!DOCTYPE html>
        <html lang="en" class="no-js">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                <meta name="viewport" content="width=device-width" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge;" />
                <title>GARMIN Authentication Application</title>

                <link rel="stylesheet" href=""/>
            </head>
            <body>

                <!-- begin GAuth component -->
                <div id="GAuth-component">
                    <!-- begin login component-->
                    <div id="login-component" class="blueForm-basic">
                       <input type="hidden" id="contextPath" value="/sso" />
                        <!-- begin login form -->
                        <div id="login-state-default">
                            <h2>Sign In</h2>

                            <form method="post" id="login-form">

                                <div class="form-alert">
                                    <div id="username-error" style="display:none;"></div>
                                    <div id="password-error" style="display:none;"></div>
                                </div>
                                <div class="textfield">
                                    <label for="username">Email</label>
                                        <!-- If the lockToEmailAddress parameter is specified then we want to mark the field as readonly,
                                        preload the email address, and disable the other input so that null isn't sent to the server. We'll
                                        also style the field to have a darker grey background and disable the mouse pointer
                                         -->

                                        <!-- If the lockToEmailAddress parameter is NOT specified then keep the existing functionality and disable the readonly input field
                                         -->
                                        <input class="login_email" name="username" id="username" value="" type="email" spellcheck="false" autocorrect="off" autocapitalize="off"/>

                                </div>

                                <div class="textfield">
                                    <label for="password">Password</label>
                                    <a id="loginforgotpassword" class="login-forgot-password" style="cursor:pointer">(Forgot?)</a>
                                    <input type="password" name="password" id="password" spellcheck="false" autocorrect="off" autocapitalize="off" />
                                     <strong id="capslock-warning" class="information" title="Caps lock is on." style="display: none;">Caps lock is on.</strong>
                                </div>
                                <input type="hidden" name="embed" value="true"/>
                                <input type="hidden" name="_csrf" value="TEST_CSRF_VALUE" />
                                <button type="submit" id="login-btn-signin" class="btn1" accesskey="l">Sign In</button>
                                <!-- The existence of the "rememberme" parameter at all will remember the user! -->
                            </form>
                        </div>
                        <!-- end login form -->

                        <!-- begin Create Account message -->
                        <div id="login-create-account">

                        </div>
                        <!-- end Create Account message -->

                        <!-- begin Social Sign In component -->
                        <div id="SSI-component">

                        </div>
                        <!-- end Social Sign In component -->
                        <div class="clearfix"></div> <!-- Ensure that GAuth-component div's height is computed correctly. -->
                    </div>
                    <!-- end login component-->

                </div>
                <!-- end GAuth component -->
        </html>
    """.trimIndent()

val htmlForTicket = """
        <!DOCTYPE html>
        <html class="no-js">
            <head>
                <title>Success</title>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge;" />
                <meta name="description" content="">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="cleartype" content="on">
                <script type="text/javascript">
                    var redirectAfterAccountLoginUrl 	  = "https:\/\/sso.garmin.com\/sso\/embed";
                    var redirectAfterAccountCreationUrl = "https:\/\/sso.garmin.com\/sso\/embed";
                    var consumeServiceTicket         	  = "true";
                    var service_url                  	  = "https:\/\/sso.garmin.com\/sso\/embed";
                    var parent_url                   	  = "https:\/\/sso.garmin.com\/sso\/embed";
                    var response_url                 	  = "https:\/\/sso.garmin.com\/sso\/embed?ticket=TEST_TICKET_VALUE";
                    var logintoken                   	  = "";
                    var socialLogin                   	  = "";
                    var performMFACheck                 = "";
                </script>
            </head>
            <body>
                <div id="GAuth-component">
                    <img src='/sso/images/ajax-loader.gif' class="loaderImage"/>
                </div>
            </body>m
        </html>
    """.trimIndent()

val userProfileJson = """
    {
    "id": 1,
    "profileId": 1,
    "garminGUID": "621e187e-259e-474d-b502-6538c56c29c3",
    "displayName": "c27c9419-a89c-4529-83d0-a977794836c3",
    "fullName": "Paul",
    "userName": "email@email.com",
    "profileImageType": "UPLOADED_PHOTO",
    "profileImageUrlLarge": "https://profile.image.com/large.jpg",
    "profileImageUrlMedium": "https://profile.image.com/medium.jpg",
    "profileImageUrlSmall": "https://profile.image.com/small.jpg",
    "location": "Auckland",
    "facebookUrl": null,
    "twitterUrl": null,
    "personalWebsite": null,
    "motivation": 2,
    "bio": null,
    "primaryActivity": "running",
    "favoriteActivityTypes": [
        "running",
        "cycling",
        "weight_training"
    ],
    "runningTrainingSpeed": 0.0,
    "cyclingTrainingSpeed": 0.0,
    "favoriteCyclingActivityTypes": [
        "road",
        "commuting"
    ],
    "cyclingClassification": null,
    "cyclingMaxAvgPower": 0.0,
    "swimmingTrainingSpeed": 0.0,
    "profileVisibility": "private",
    "activityStartVisibility": "public",
    "activityMapVisibility": "public",
    "courseVisibility": "public",
    "activityHeartRateVisibility": "public",
    "activityPowerVisibility": "public",
    "badgeVisibility": "private",
    "showAge": true,
    "showWeight": false,
    "showHeight": false,
    "showWeightClass": false,
    "showAgeRange": false,
    "showGender": true,
    "showActivityClass": false,
    "showVO2Max": false,
    "showPersonalRecords": true,
    "showLast12Months": true,
    "showLifetimeTotals": true,
    "showUpcomingEvents": true,
    "showRecentFavorites": true,
    "showRecentDevice": true,
    "showRecentGear": false,
    "showBadges": true,
    "otherActivity": null,
    "otherPrimaryActivity": null,
    "otherMotivation": null,
    "userRoles": [
        "SCOPE_ATP_READ",
        "SCOPE_ATP_WRITE",
        "SCOPE_COMMUNITY_COURSE_READ",
        "SCOPE_COMMUNITY_COURSE_WRITE",
        "SCOPE_CONNECT_READ",
        "SCOPE_CONNECT_WRITE",
        "SCOPE_DT_CLIENT_ANALYTICS_WRITE",
        "SCOPE_GARMINPAY_READ",
        "SCOPE_GARMINPAY_WRITE",
        "SCOPE_GCOFFER_READ",
        "SCOPE_GCOFFER_WRITE",
        "SCOPE_GHS_SAMD",
        "SCOPE_GHS_UPLOAD",
        "SCOPE_GOLF_API_READ",
        "SCOPE_GOLF_API_WRITE",
        "SCOPE_INSIGHTS_READ",
        "SCOPE_INSIGHTS_WRITE",
        "SCOPE_OMT_CAMPAIGN_READ",
        "SCOPE_OMT_SUBSCRIPTION_READ",
        "SCOPE_PRODUCT_SEARCH_READ",
        "ROLE_CONNECTUSER",
        "ROLE_FITNESS_USER",
        "ROLE_WELLNESS_USER",
        "ROLE_MARINE_USER"
    ],
    "nameApproved": true,
    "userProfileFullName": "Paul",
    "makeGolfScorecardsPrivate": true,
    "allowGolfLiveScoring": false,
    "allowGolfScoringByConnections": true,
    "userLevel": 6,
    "userPoint": 741,
    "levelUpdateDate": "2024-01-01T20:48:42.0",
    "levelIsViewed": false,
    "levelPointThreshold": 1260,
    "userPointOffset": 0,
    "userPro": false
}
""".trimIndent()

val latestActivitiesJson = """
    [
        {
            "activityId": 1,
            "activityName": "Activity 1",
            "startTimeLocal": "2024-10-24 20:15:00",
            "startTimeGMT": "2024-10-24 07:15:00",
            "activityType": {
                "typeId": 10,
                "typeKey": "road_biking",
                "parentTypeId": 2,
                "isHidden": false,
                "restricted": false,
                "trimmable": true
            },
            "eventType": {
                "typeId": 5,
                "typeKey": "transportation",
                "sortOrder": 8
            },
            "distance": 17803.69921875,
            "duration": 2718.678955078125,
            "elapsedDuration": 2718.678955078125,
            "movingDuration": 2577.4530029296875,
            "elevationGain": 142.0,
            "elevationLoss": 136.0,
            "averageSpeed": 6.548999786376953,
            "maxSpeed": 14.508999824523926,
            "startLatitude": -36.84929880313575,
            "startLongitude": 174.7583261411637,
            "hasPolyline": true,
            "hasImages": false,
            "ownerId": 75364678,
            "ownerDisplayName": "49117e9d-b3c1-45b0-a1db-732556749653",
            "ownerFullName": "Paul",
            "ownerProfileImageUrlSmall": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/eedf99a7-3de6-4c8e-9b17-740efd891672-75364678.jpg",
            "ownerProfileImageUrlMedium": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/12a03481-60dd-4766-9ab8-f14bb44518d2-75364678.jpg",
            "ownerProfileImageUrlLarge": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/baddb995-3078-49c1-9930-472c93943370-75364678.jpg",
            "calories": 429.0,
            "bmrCalories": 62.0,
            "averageHR": 136.0,
            "maxHR": 171.0,
            "averageBikingCadenceInRevPerMinute": 67.0,
            "maxBikingCadenceInRevPerMinute": 110.0,
            "userRoles": [
                "SCOPE_GOLF_API_READ",
                "SCOPE_ATP_READ",
                "SCOPE_DIVE_API_WRITE",
                "SCOPE_COMMUNITY_COURSE_ADMIN_READ",
                "SCOPE_DIVE_API_READ",
                "SCOPE_DI_OAUTH_2_CLIENT_READ",
                "SCOPE_CONNECT_WRITE",
                "SCOPE_COMMUNITY_COURSE_WRITE",
                "SCOPE_MESSAGE_GENERATION_READ",
                "SCOPE_DI_OAUTH_2_CLIENT_REVOCATION_ADMIN",
                "SCOPE_CONNECT_WEB_TEMPLATE_RENDER",
                "SCOPE_OMT_SUBSCRIPTION_ADMIN_READ",
                "SCOPE_CONNECT_NON_SOCIAL_SHARED_READ",
                "SCOPE_CONNECT_READ",
                "SCOPE_DI_OAUTH_2_TOKEN_ADMIN",
                "ROLE_CONNECTUSER",
                "ROLE_FITNESS_USER",
                "ROLE_WELLNESS_USER",
                "ROLE_MARINE_USER"
            ],
            "privacy": {
                "typeId": 2,
                "typeKey": "private"
            },
            "userPro": false,
            "hasVideo": false,
            "timeZoneId": 130,
            "beginTimestamp": 1729754100000,
            "sportTypeId": 2,
            "avgPower": 135.0,
            "maxPower": 949.0,
            "aerobicTrainingEffect": 2.4000000953674316,
            "anaerobicTrainingEffect": 1.100000023841858,
            "strokes": 2743.0,
            "normPower": 161.0,
            "avgLeftBalance": 47.21,
            "max20MinPower": 146.0,
            "trainingStressScore": 32.79999923706055,
            "intensityFactor": 0.6629999876022339,
            "vO2MaxValue": 55.0,
            "deviceId": 3999940010,
            "minTemperature": 17.0,
            "maxTemperature": 22.0,
            "minElevation": -8.399999618530273,
            "maxElevation": 70.4000015258789,
            "summarizedDiveInfo": {
                "summarizedDiveGases": []
            },
            "avgVerticalSpeed": 5.223124907353903,
            "maxVerticalSpeed": 27.600000381469727,
            "manufacturer": "GARMIN",
            "locationName": "Auckland",
            "lapCount": 4,
            "endLatitude": -36.87170471996069,
            "endLongitude": 174.6240296959877,
            "caloriesConsumed": 0.0,
            "waterEstimated": 324.0,
            "waterConsumed": 500.0,
            "maxAvgPower_1": 949,
            "maxAvgPower_2": 873,
            "maxAvgPower_5": 650,
            "maxAvgPower_10": 491,
            "maxAvgPower_20": 364,
            "maxAvgPower_30": 345,
            "maxAvgPower_60": 263,
            "maxAvgPower_120": 196,
            "maxAvgPower_300": 180,
            "maxAvgPower_600": 165,
            "maxAvgPower_1200": 146,
            "maxAvgPower_1800": 145,
            "excludeFromPowerCurveReports": false,
            "minRespirationRate": 22.940000534057617,
            "maxRespirationRate": 35.27000045776367,
            "avgRespirationRate": 28.65999984741211,
            "trainingEffectLabel": "RECOVERY",
            "activityTrainingLoad": 66.91346740722656,
            "minActivityLapDuration": 475.56298828125,
            "aerobicTrainingEffectMessage": "MINOR_AEROBIC_BENEFIT_0",
            "anaerobicTrainingEffectMessage": "MINOR_ANAEROBIC_BENEFIT_15",
            "splitSummaries": [],
            "hasSplits": false,
            "moderateIntensityMinutes": 20,
            "vigorousIntensityMinutes": 21,
            "purposeful": false,
            "pr": false,
            "manualActivity": false,
            "autoCalcCalories": false,
            "elevationCorrected": false,
            "atpActivity": false,
            "favorite": false,
            "decoDive": false,
            "parent": false
        },
        {
            "activityId": 2,
            "activityName": "Activity 2",
            "startTimeLocal": "2024-10-24 06:52:48",
            "startTimeGMT": "2024-10-23 17:52:48",
            "activityType": {
                "typeId": 10,
                "typeKey": "road_biking",
                "parentTypeId": 2,
                "isHidden": false,
                "restricted": false,
                "trimmable": true
            },
            "eventType": {
                "typeId": 5,
                "typeKey": "transportation",
                "sortOrder": 8
            },
            "distance": 17759.779296875,
            "duration": 2721.677001953125,
            "elapsedDuration": 2721.677001953125,
            "movingDuration": 2642.0,
            "elevationGain": 155.0,
            "elevationLoss": 150.0,
            "averageSpeed": 6.5250000953674325,
            "maxSpeed": 12.51200008392334,
            "startLatitude": -36.87174679711461,
            "startLongitude": 174.62399776093662,
            "hasPolyline": true,
            "hasImages": false,
            "ownerId": 75364678,
            "ownerDisplayName": "49117e9d-b3c1-45b0-a1db-732556749653",
            "ownerFullName": "Paul",
            "ownerProfileImageUrlSmall": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/eedf99a7-3de6-4c8e-9b17-740efd891672-75364678.jpg",
            "ownerProfileImageUrlMedium": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/12a03481-60dd-4766-9ab8-f14bb44518d2-75364678.jpg",
            "ownerProfileImageUrlLarge": "https://s3.amazonaws.com/garmin-connect-prod/profile_images/baddb995-3078-49c1-9930-472c93943370-75364678.jpg",
            "calories": 389.0,
            "bmrCalories": 61.0,
            "averageHR": 127.0,
            "maxHR": 163.0,
            "averageBikingCadenceInRevPerMinute": 65.0,
            "maxBikingCadenceInRevPerMinute": 96.0,
            "userRoles": [
                "SCOPE_GOLF_API_READ",
                "SCOPE_ATP_READ",
                "SCOPE_DIVE_API_WRITE",
                "SCOPE_COMMUNITY_COURSE_ADMIN_READ",
                "SCOPE_DIVE_API_READ",
                "SCOPE_DI_OAUTH_2_CLIENT_READ",
                "SCOPE_CONNECT_WRITE",
                "SCOPE_COMMUNITY_COURSE_WRITE",
                "SCOPE_MESSAGE_GENERATION_READ",
                "SCOPE_DI_OAUTH_2_CLIENT_REVOCATION_ADMIN",
                "SCOPE_CONNECT_WEB_TEMPLATE_RENDER",
                "SCOPE_OMT_SUBSCRIPTION_ADMIN_READ",
                "SCOPE_CONNECT_NON_SOCIAL_SHARED_READ",
                "SCOPE_CONNECT_READ",
                "SCOPE_DI_OAUTH_2_TOKEN_ADMIN",
                "ROLE_CONNECTUSER",
                "ROLE_FITNESS_USER",
                "ROLE_WELLNESS_USER",
                "ROLE_MARINE_USER"
            ],
            "privacy": {
                "typeId": 2,
                "typeKey": "private"
            },
            "userPro": false,
            "hasVideo": false,
            "timeZoneId": 130,
            "beginTimestamp": 1729705968000,
            "sportTypeId": 2,
            "avgPower": 120.0,
            "maxPower": 524.0,
            "aerobicTrainingEffect": 2.0999999046325684,
            "anaerobicTrainingEffect": 0.4000000059604645,
            "strokes": 2576.0,
            "normPower": 149.0,
            "avgLeftBalance": 47.3,
            "max20MinPower": 142.38833333333332,
            "trainingStressScore": 28.0,
            "intensityFactor": 0.6119999885559082,
            "vO2MaxValue": 55.0,
            "deviceId": 3999940010,
            "minTemperature": 10.0,
            "maxTemperature": 20.0,
            "minElevation": 17.200000762939453,
            "maxElevation": 84.4000015258789,
            "summarizedDiveInfo": {
                "summarizedDiveGases": []
            },
            "maxVerticalSpeed": 5.799999237060547,
            "manufacturer": "GARMIN",
            "locationName": "Auckland",
            "lapCount": 4,
            "endLatitude": -36.849369797855616,
            "endLongitude": 174.75845329463482,
            "caloriesConsumed": 0.0,
            "waterEstimated": 243.0,
            "waterConsumed": 500.0,
            "maxAvgPower_1": 524,
            "maxAvgPower_2": 497,
            "maxAvgPower_5": 431,
            "maxAvgPower_10": 411,
            "maxAvgPower_20": 339,
            "maxAvgPower_30": 292,
            "maxAvgPower_60": 244,
            "maxAvgPower_120": 199,
            "maxAvgPower_300": 184,
            "maxAvgPower_600": 161,
            "maxAvgPower_1200": 142,
            "maxAvgPower_1800": 140,
            "excludeFromPowerCurveReports": false,
            "minRespirationRate": 15.079999923706055,
            "maxRespirationRate": 36.439998626708984,
            "avgRespirationRate": 24.920000076293945,
            "trainingEffectLabel": "RECOVERY",
            "activityTrainingLoad": 42.87953186035156,
            "minActivityLapDuration": 463.7909851074219,
            "aerobicTrainingEffectMessage": "MINOR_AEROBIC_BENEFIT_0",
            "anaerobicTrainingEffectMessage": "NO_ANAEROBIC_BENEFIT_0",
            "splitSummaries": [],
            "hasSplits": false,
            "moderateIntensityMinutes": 13,
            "vigorousIntensityMinutes": 23,
            "purposeful": false,
            "pr": false,
            "manualActivity": false,
            "autoCalcCalories": false,
            "elevationCorrected": false,
            "atpActivity": false,
            "favorite": false,
            "decoDive": false,
            "parent": false
        }
    ]
""".trimIndent()

val coursesJson = """
    [
        {
            "courseId": 1,
            "userProfileId": 1,
            "displayName": "display name",
            "userGroupId": null,
            "geoRoutePk": null,
            "activityType": {
                "typeId": 1,
                "typeKey": "running",
                "parentTypeId": 17,
                "isHidden": false,
                "restricted": false,
                "trimmable": false
            },
            "courseName": "Course 1",
            "courseDescription": null,
            "createdDate": 1690847946000,
            "updatedDate": 1690847946000,
            "privacyRule": {
                "typeId": 2,
                "typeKey": "private"
            },
            "distanceInMeters": 10234.81,
            "elevationGainInMeters": 256.29,
            "elevationLossInMeters": 255.1,
            "startLatitude": -36.84921,
            "startLongitude": 174.75862,
            "speedInMetersPerSecond": 0.0,
            "sourceTypeId": 3,
            "sourcePk": null,
            "elapsedSeconds": null,
            "coordinateSystem": "WGS84",
            "originalCoordinateSystem": "WGS84",
            "consumer": null,
            "elevationSource": 3,
            "hasShareableEvent": false,
            "hasPaceBand": false,
            "hasPowerGuide": false,
            "favorite": false,
            "hasTurnDetectionDisabled": false,
            "curatedCourseId": null,
            "startNote": null,
            "finishNote": null,
            "cutoffDuration": null,
            "activityTypeId": {
                "typeId": 1,
                "typeKey": "running",
                "parentTypeId": 17,
                "isHidden": false,
                "restricted": false,
                "trimmable": false
            },
            "public": false,
            "createdDateFormatted": "2023-07-31 23:59:06.0 GMT",
            "updatedDateFormatted": "2023-07-31 23:59:06.0 GMT"
        },
        {
            "courseId": 2,
            "userProfileId": 1,
            "displayName": "display name",
            "userGroupId": null,
            "geoRoutePk": null,
            "activityType": {
                "typeId": 10,
                "typeKey": "road_biking",
                "parentTypeId": 2,
                "isHidden": false,
                "restricted": false,
                "trimmable": false
            },
            "courseName": "Course 2",
            "courseDescription": null,
            "createdDate": 1693709364000,
            "updatedDate": 1693709485000,
            "privacyRule": {
                "typeId": 2,
                "typeKey": "private"
            },
            "distanceInMeters": 15007.59,
            "elevationGainInMeters": 139.0,
            "elevationLossInMeters": 131.0,
            "startLatitude": -36.87140095978975,
            "startLongitude": 174.6240344736725,
            "speedInMetersPerSecond": 3.031836363636364,
            "sourceTypeId": 1,
            "sourcePk": 11867676869,
            "elapsedSeconds": 4950.0,
            "coordinateSystem": "WGS84",
            "originalCoordinateSystem": "WGS84",
            "consumer": null,
            "elevationSource": 2,
            "hasShareableEvent": false,
            "hasPaceBand": false,
            "hasPowerGuide": false,
            "favorite": false,
            "hasTurnDetectionDisabled": false,
            "curatedCourseId": null,
            "startNote": null,
            "finishNote": null,
            "cutoffDuration": null,
            "activityTypeId": {
                "typeId": 1,
                "typeKey": "running",
                "parentTypeId": 17,
                "isHidden": false,
                "restricted": false,
                "trimmable": false
            },
            "public": false,
            "createdDateFormatted": "2023-09-03 02:49:24.0 GMT",
            "updatedDateFormatted": "2023-09-03 02:51:25.0 GMT"
        }
    ]
""".trimIndent()

val eventTypesJson = """
    [
        {
            "typeId": 1,
            "typeKey": "race",
            "sortOrder": 5
        },
        {
            "typeId": 2,
            "typeKey": "training",
            "sortOrder": 4
        }
    ]
""".trimIndent()

const val connectPort = 8081
const val garminSSOPort = 8082
const val garthPort = 8083

fun loadRes(res: Int): String =
    getInstrumentation().context.resources.openRawResource(res).bufferedReader().use { it.readText() }

var sslSocketFactory = HandshakeCertificates.Builder()
    .heldCertificate(HeldCertificate.decode(loadRes(R.raw.server)))
    .build().sslSocketFactory()

val connectDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path ?: ""
        return when {
            path.startsWith("/oauth-service/oauth/preauthorized") && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(oauth1Body)
            path == "/oauth-service/oauth/exchange/user/2.0" && request.method == "POST" ->
                MockResponse().setResponseCode(200).setBody(oauth2Body)
            path == "/upload-service/upload" && request.method == "POST" ->
                MockResponse().setResponseCode(200)
            path == "/userprofile-service/socialProfile" && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(userProfileJson)
            path == "/course-service/course" && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(coursesJson)
            path == "/activity-service/activity/eventTypes" && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(eventTypesJson)
            (path.startsWith("/activitylist-service/activities/search/activities") && request.method == "GET") ->
                MockResponse().setResponseCode(200).setBody(latestActivitiesJson)
            (path.startsWith("/activity-service/activity") && request.method == "PUT") ->
                MockResponse().setResponseCode(200)
            else -> MockResponse().setResponseCode(404)
        }
    }
}

val garthDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path ?: ""
        return when {
            path == "/oauth_consumer.json" && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(consumerBody)
            else -> MockResponse().setResponseCode(404)
        }
    }
}

val garminSSODispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path ?: ""
        return when {
            path.startsWith("/sso/signin") && request.method == "GET" ->
                MockResponse().setResponseCode(200).setBody(htmlForCSRF)
            path.startsWith("/sso/signin") && request.method == "POST" ->
                MockResponse().setResponseCode(200).setBody(htmlForTicket)
            else -> MockResponse().setResponseCode(404)
        }
    }
}



