package paufregi.connectfeed

import com.appstractive.jwt.jwt
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.RecordedRequest
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.api.garmin.GarminAuth
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import kotlin.time.Instant

fun createAuthToken(expiresAt: Instant, refreshToken: String = "REFRESH_TOKEN") = AuthToken(
    accessToken = jwt {
        claims {
            expires(expiresAt)
            claim("client_id", "connect")
        }
    }.toString(),
    refreshToken = refreshToken,
)

val user = User(1, "Paul", "https://profile.image.com/medium.jpg")
val authToken = createAuthToken(today)
val refreshedToken = createAuthToken(tomorrow, "NEW_REFRESH_TOKEN")

val validLogin = """
    {
        "serviceURL": "https://mobile.integration.garmin.com/gcm/android", 
        "serviceTicketId": "ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso", 
        "responseStatus": {
            "type": "SUCCESSFUL", 
            "message": ""
        }, 
        "responseReason": "", 
        "customerMfaInfo": "", 
        "consentTypeList": "", 
        "captchaAlreadyPassed": false, 
        "samlResponse": "", 
        "authType": "CAS"
    }
""".trimIndent()

val invalidLogin = """
    {
        "serviceURL": "", 
        "serviceTicketId": "", 
        "responseStatus": {
            "type": "INVALID_USERNAME_PASSWORD", 
            "message": "generalLoginInvalidUsernameOrPassword"
        }, 
        "responseReason": "", 
        "customerMfaInfo": "", 
        "consentTypeList": "", 
        "captchaAlreadyPassed": "", 
        "samlResponse": "", 
        "authType": "CAS"
    }
""".trimIndent()

val authTokenJson = """
    {
        "access_token": "${authToken.accessToken}",
        "refresh_token": "${authToken.refreshToken}"
    }
    """.trimIndent()

val refreshedAuthTokenJson = """
    {
        "access_token": "${refreshedToken.accessToken}",
        "refresh_token": "${refreshedToken.refreshToken}"
    }
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

val activitiesJson = """
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
            "workoutId": 1,
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
            "workoutId": 2,
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

val workoutJson = """
{
        "workoutId": 1,
        "ownerId": 1,
        "workoutName": "Power - Zone 6",
        "description": null,
        "updatedDate": "2026-06-15T01:53:48.0",
        "createdDate": "2026-05-24T19:04:09.0",
        "sportType": {
            "sportTypeId": 2,
            "sportTypeKey": "cycling",
            "displayOrder": 2
        },
        "subSportType": "GENERIC",
        "trainingPlanId": null,
        "author": {
            "userProfilePk": 75364678,
            "displayName": "49117e9d-b3c1-45b0-a1db-732556349653",
            "fullName": "Paul",
            "profileImgNameLarge": null,
            "profileImgNameMedium": "12a03481-60dd-4756-9ab8-f14bb44518d2-75364678.jpg",
            "profileImgNameSmall": "eedf99a7-3de6-4c2e-9b17-740efd891672-75364678.jpg",
            "userPro": false,
            "vivokidUser": false
        },
        "sharedWithUsers": null,
        "estimatedDurationInSecs": 0,
        "estimatedDistanceInMeters": 0.0,
        "workoutSegments": [
            {
                "segmentOrder": 1,
                "sportType": {
                    "sportTypeId": 2,
                    "sportTypeKey": "cycling",
                    "displayOrder": 2
                },
                "poolLengthUnit": null,
                "poolLength": null,
                "avgTrainingSpeed": null,
                "estimatedDurationInSecs": null,
                "estimatedDistanceInMeters": null,
                "estimatedDistanceUnit": null,
                "estimateType": null,
                "description": null,
                "workoutSteps": [
                    {
                        "type": "ExecutableStepDTO",
                        "stepId": 13679892150,
                        "stepOrder": 1,
                        "stepType": {
                            "stepTypeId": 1,
                            "stepTypeKey": "warmup",
                            "displayOrder": 1
                        },
                        "childStepId": null,
                        "description": null,
                        "endCondition": {
                            "conditionTypeId": 1,
                            "conditionTypeKey": "lap.button",
                            "displayOrder": 1,
                            "displayable": true
                        },
                        "endConditionValue": 1200.0,
                        "preferredEndConditionUnit": null,
                        "endConditionCompare": null,
                        "targetType": {
                            "workoutTargetTypeId": 1,
                            "workoutTargetTypeKey": "no.target",
                            "displayOrder": 1
                        },
                        "targetValueOne": null,
                        "targetValueTwo": null,
                        "targetValueUnit": null,
                        "zoneNumber": null,
                        "secondaryTargetType": null,
                        "secondaryTargetValueOne": null,
                        "secondaryTargetValueTwo": null,
                        "secondaryTargetValueUnit": null,
                        "secondaryZoneNumber": null,
                        "endConditionZone": null,
                        "strokeType": {
                            "strokeTypeId": 0,
                            "strokeTypeKey": null,
                            "displayOrder": 0
                        },
                        "equipmentType": {
                            "equipmentTypeId": 0,
                            "equipmentTypeKey": null,
                            "displayOrder": 0
                        },
                        "category": null,
                        "exerciseName": null,
                        "workoutProvider": null,
                        "providerExerciseSourceId": null,
                        "weightValue": null,
                        "weightUnit": {
                            "unitId": 8,
                            "unitKey": "kilogram",
                            "factor": 1000.0
                        }
                    },
                    {
                        "type": "ExecutableStepDTO",
                        "stepId": 13679892151,
                        "stepOrder": 2,
                        "stepType": {
                            "stepTypeId": 3,
                            "stepTypeKey": "interval",
                            "displayOrder": 3
                        },
                        "childStepId": null,
                        "description": null,
                        "endCondition": {
                            "conditionTypeId": 2,
                            "conditionTypeKey": "time",
                            "displayOrder": 2,
                            "displayable": true
                        },
                        "endConditionValue": 60.0,
                        "preferredEndConditionUnit": null,
                        "endConditionCompare": null,
                        "targetType": {
                            "workoutTargetTypeId": 2,
                            "workoutTargetTypeKey": "power.zone",
                            "displayOrder": 2
                        },
                        "targetValueOne": null,
                        "targetValueTwo": null,
                        "targetValueUnit": null,
                        "zoneNumber": 6,
                        "secondaryTargetType": {
                            "workoutTargetTypeId": 1,
                            "workoutTargetTypeKey": "no.target",
                            "displayOrder": 1
                        },
                        "secondaryTargetValueOne": null,
                        "secondaryTargetValueTwo": null,
                        "secondaryTargetValueUnit": null,
                        "secondaryZoneNumber": null,
                        "endConditionZone": null,
                        "strokeType": {
                            "strokeTypeId": 0,
                            "strokeTypeKey": null,
                            "displayOrder": 0
                        },
                        "equipmentType": {
                            "equipmentTypeId": 0,
                            "equipmentTypeKey": null,
                            "displayOrder": 0
                        },
                        "category": null,
                        "exerciseName": null,
                        "workoutProvider": null,
                        "providerExerciseSourceId": null,
                        "weightValue": null,
                        "weightUnit": {
                            "unitId": 8,
                            "unitKey": "kilogram",
                            "factor": 1000.0
                        }
                    },
                    {
                        "type": "ExecutableStepDTO",
                        "stepId": 13679892164,
                        "stepOrder": 15,
                        "stepType": {
                            "stepTypeId": 2,
                            "stepTypeKey": "cooldown",
                            "displayOrder": 2
                        },
                        "childStepId": null,
                        "description": null,
                        "endCondition": {
                            "conditionTypeId": 1,
                            "conditionTypeKey": "lap.button",
                            "displayOrder": 1,
                            "displayable": true
                        },
                        "endConditionValue": 1200.0,
                        "preferredEndConditionUnit": null,
                        "endConditionCompare": null,
                        "targetType": {
                            "workoutTargetTypeId": 1,
                            "workoutTargetTypeKey": "no.target",
                            "displayOrder": 1
                        },
                        "targetValueOne": null,
                        "targetValueTwo": null,
                        "targetValueUnit": null,
                        "zoneNumber": null,
                        "secondaryTargetType": null,
                        "secondaryTargetValueOne": null,
                        "secondaryTargetValueTwo": null,
                        "secondaryTargetValueUnit": null,
                        "secondaryZoneNumber": null,
                        "endConditionZone": null,
                        "strokeType": {
                            "strokeTypeId": 0,
                            "strokeTypeKey": null,
                            "displayOrder": 0
                        },
                        "equipmentType": {
                            "equipmentTypeId": 0,
                            "equipmentTypeKey": null,
                            "displayOrder": 0
                        },
                        "category": null,
                        "exerciseName": null,
                        "workoutProvider": null,
                        "providerExerciseSourceId": null,
                        "weightValue": null,
                        "weightUnit": {
                            "unitId": 8,
                            "unitKey": "kilogram",
                            "factor": 1000.0
                        }
                    }
                ]
            }
        ],
        "poolLength": null,
        "poolLengthUnit": null,
        "locale": null,
        "workoutProvider": "null",
        "workoutSourceId": "null",
        "uploadTimestamp": null,
        "atpPlanId": null,
        "consumer": null,
        "consumerName": null,
        "consumerImageURL": null,
        "consumerWebsiteURL": null,
        "workoutNameI18nKey": null,
        "descriptionI18nKey": null,
        "avgTrainingSpeed": 6.163858664942544,
        "estimateType": null,
        "estimatedDistanceUnit": {
            "unitId": null,
            "unitKey": null,
            "factor": null
        },
        "workoutThumbnailUrl": "https://connect.garmin.com/chart-service/workout?data=f,f,-n,1,pz=6-f,f,-rp*3(n,1,pz=6-f,0.5,)-f,f,-n,1,pz=6-f,f,-rp*2(n,1,pz=6-f,0.5,)-f,f,-n,f,pz=6-f,f,",
        "isSessionTransitionEnabled": null,
        "shared": false
    }
""".trimIndent()

val gearsJson = """
    [
        {
            "uuid": "522f1e21-0822-451d-88a3-b0b661802c2f",
            "userProfilePk": 1,
            "gearType": "SHOES",
            "status": "ACTIVE",
            "brand": "Mizuno",
            "model": "Neo Vista",
            "usageType": "DISTANCE",
            "maxUsageDistanceMeters": 1000000.0,
            "maxUsageDurationSeconds": 0,
            "firstUseDate": "2026-03-18",
            "numActivitiesLinked": 2,
            "durationUsedSeconds": 17086,
            "distanceUsedMeters": 51955.4501953125,
            "daysUsed": 2,
            "processing": false,
            "createdDate": "2026-03-23T01:15:28.0"
        },
        {
            "uuid": "789dccf8-b669-4903-bf46-7d8d9369124e",
            "userProfilePk": 1,
            "gearType": "BIKE",
            "status": "ACTIVE",
            "name": "Nova",
            "brand": "Giant",
            "model": "Contend AR",
            "usageType": "DISTANCE",
            "maxUsageDurationSeconds": 0,
            "firstUseDate": "2021-08-16",
            "numActivitiesLinked": 1009,
            "durationUsedSeconds": 2808622,
            "distanceUsedMeters": 17226955.28363037,
            "daysUsed": 753,
            "processing": false,
            "createdDate": "2021-09-15T22:46:54.0"
        }
    ]
""".trimIndent()

const val connectPort = 8081
const val garminSSOPort = 8082
const val garminAuthPort = 8083

val connectDispatcher: Dispatcher = object : Dispatcher(){
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.url.encodedPath
        return when {
            path == "/upload-service/upload" && request.method == "POST" ->
                MockResponse(200)

            path == "/userprofile-service/socialProfile" && request.method == "GET" ->
                MockResponse(code = 200, body = userProfileJson)

            path == "/course-service/course" && request.method == "GET" ->
                MockResponse(code = 200, body = coursesJson)

            path == "/gear-service/gear/v2/list" && request.method == "GET" ->
                MockResponse(code = 200, body = gearsJson)

            path.startsWith("/gear-service/activity/v2") && path.endsWith("/associated-gear") && request.method == "PUT" ->
                MockResponse(200)

            path.startsWith("/activitylist-service/activities/search/activities") && request.method == "GET" ->
                MockResponse(code = 200, body = activitiesJson)

            path.startsWith("/activity-service/activity") && request.method == "PUT" ->
                MockResponse(200)

            path.startsWith("/workout-service/workout") && request.method == "GET" ->
                MockResponse(code = 200, body = workoutJson)

            else -> MockResponse(404)
        }
    }
}

val garminSSODispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.url.encodedPath
        return when {
            path.startsWith("/mobile/api/login") && request.method == "POST" ->
                MockResponse(code = 200, body = validLogin)

            else -> MockResponse(404)
        }
    }
}

val garminAuthDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.url.encodedPath
        val fields = request.getFields()
        return when (path) {
            "/di-oauth2-service/oauth/token" if request.method == "POST" && fields["grant_type"] == GarminAuth.GRANT_TYPE_EXCHANGE ->
                MockResponse(code = 200, body = authTokenJson)

            "/di-oauth2-service/oauth/token" if request.method == "POST" && fields["grant_type"] == GarminAuth.GRANT_TYPE_REFRESH ->
                MockResponse(code = 200, body = refreshedAuthTokenJson)

            else -> MockResponse(404)
        }
    }
}