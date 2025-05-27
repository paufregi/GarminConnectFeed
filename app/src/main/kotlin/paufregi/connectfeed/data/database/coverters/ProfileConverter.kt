package paufregi.connectfeed.data.database.coverters

import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.data.database.entities.ProfileEntity

fun Profile.toEntity(userId: Long) = ProfileEntity(
    id = id,
    userId = userId,
    name = name,
    activityType = activityType,
    eventType = eventType,
    course = course,
    water = water,
    rename = rename,
    customWater = customWater,
    feelAndEffort = feelAndEffort,
    trainingEffect = trainingEffect,
)

fun ProfileEntity.toCore() = Profile(
    id = id,
    name = name,
    activityType = activityType,
    eventType = eventType,
    course = course,
    water = water,
    rename = rename,
    customWater = customWater,
    feelAndEffort = feelAndEffort,
    trainingEffect = trainingEffect,
)