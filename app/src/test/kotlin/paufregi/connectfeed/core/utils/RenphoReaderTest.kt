package paufregi.connectfeed.core.utils

import com.google.common.truth.Truth.assertThat
import org.apache.commons.io.IOUtils
import org.junit.Test
import paufregi.connectfeed.core.models.Weight
import java.time.Instant
import java.util.Date
import java.util.Locale


class RenphoReaderTest {

    @Test
    fun `Convert Renpho CSV to list of weights`() {
        Locale.setDefault(Locale.ENGLISH)
        val csvText = """
            Date,Time,Weight(kg),BMI,Body Fat(%),Skeletal Muscle(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal Weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2025.05.10,01:02:03,75.00,23.4,22.6,49.9,58.05,20.4,7.0,55.9,55.13,2.92,17.6,1631,34,--,--,--,--,--,--
        """.trimIndent()

        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")

        val expected = Weight(
            timestamp = Date.from(Instant.parse("2025-05-09T13:02:03Z")),
            weight = 75.00f,
            bmi = 23.4f,
            fat = 22.6f,
            visceralFat = 7,
            water = 55.9f,
            muscle = 55.13f,
            bone = 2.92f,
            basalMet = 1631f,
            metabolicAge = 34,
        )

        val result = RenphoReader.read(stubInputStream)

        assertThat(result).containsExactly(expected)
    }
}