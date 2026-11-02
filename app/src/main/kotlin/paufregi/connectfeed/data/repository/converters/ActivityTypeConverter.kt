package paufregi.connectfeed.data.repository.converters

import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto

fun ActivityTypeDto.toCore(): ActivityType = when (this.id) {
    // Running
    1L -> ActivityType.Running
    8L -> ActivityType.TrackRunning
    6L -> ActivityType.TrailRunning
    18L -> ActivityType.TreadmillRunning
    181L -> ActivityType.UltraRun

    // Cycling
    2L -> ActivityType.Cycling
    20L -> ActivityType.DownhillBiking
    176L -> ActivityType.EBiking
    175L -> ActivityType.EBikingMountain
    143L -> ActivityType.GravelCycling
    5L -> ActivityType.MountainBiking
    10L -> ActivityType.RoadBiking
    25L -> ActivityType.IndoorRide
    152L -> ActivityType.VirtualRide

    // Fitness
    180L -> ActivityType.HIIT
    164L -> ActivityType.Breathwork
    11L -> ActivityType.Cardio
    254L -> ActivityType.JumpRope
    13L -> ActivityType.StrengthTraining
    163L -> ActivityType.Yoga

    // Swimming
    27L -> ActivityType.PoolSwimming
    28L -> ActivityType.OpenWaterSwimming
    26L -> ActivityType.Swimming

    // Multisport
    89L -> ActivityType.Multisport

    // Other
    9L -> ActivityType.Walking
    3L -> ActivityType.Hiking
    252L -> ActivityType.Snowboarding
    231L -> ActivityType.Kayaking
    239L -> ActivityType.StandUpPaddling
    240L -> ActivityType.Surfing
    242L -> ActivityType.Windsurf

    else -> ActivityType.Unknown
}