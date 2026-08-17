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
            No.,Date,Time,Weight(kg),BMI,Body Fat Percentage(%),Body Fat Mass(kg),Muscle Percentage(%),Muscle Mass(kg),Skeletal Muscle Percentage(%),Skeletal Muscle Mass(kg),Bone Percentage(%),Bone Mass(kg),Protein Percentage(%),Protein Mass(kg),Body Water Percentage(%),Body Water Mass(kg),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,BMR(kcal),Metabolic Age,WHR (Waist-to-Hip Ratio),Optimal Weight(kg),Weight Level,Body Type,Target to optimal weight(kg),Target to optimal muscle mass(kg),Target to optimal fat mass(kg),Remarks
            1,2026.08.16,07:59:46,74.15,23.1,14.1,10.46,81.6,60.51,55.5,41.15,4.3,3.18,19.6,14.53,62.0,45.97,63.69,12.3,6,1743,36,--,--,--,--,--,--,--,,
        """.trimIndent()

        val stubInputStream = IOUtils.toInputStream(csvText, "UTF-8")

        val formatter = Formatter.dateTimeForImport(Locale.getDefault())
        val expected = Weight(
            timestamp = formatter.parse("2026.08.16 07:59:46")!!,
            weight = 74.15f,
            bmi = 23.1f,
            fat = 14.1f,
            visceralFat = 6,
            water = 62.0f,
            muscle = 60.51f,
            bone = 3.18f,
            basalMet = 1743f,
            metabolicAge = 36,
        )

        val result = RenphoReader.read(stubInputStream)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).containsExactly(expected)
    }
}