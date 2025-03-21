package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrainingEffectConverterTest {

    @Test
    fun `Covert speed`() {
        val trainingEffect = TrainingEffectConverter.convert("SPEED")
        assertThat(trainingEffect).isEqualTo("sprint")
    }

    @Test
    fun `Covert anaerobic`() {
        val trainingEffect = TrainingEffectConverter.convert("ANAEROBIC_CAPACITY")
        assertThat(trainingEffect).isEqualTo("anaerobic capacity")
    }

    @Test
    fun `Covert VO2Max`() {
        val trainingEffect = TrainingEffectConverter.convert("VO2MAX")
        assertThat(trainingEffect).isEqualTo("VO₂Max")
    }

    @Test
    fun `Covert threshold`() {
        val trainingEffect = TrainingEffectConverter.convert("LACTATE_THRESHOLD")
        assertThat(trainingEffect).isEqualTo("threshold")
    }

    @Test
    fun `Covert tempo`() {
        val trainingEffect = TrainingEffectConverter.convert("TEMPO")
        assertThat(trainingEffect).isEqualTo("tempo")
    }

    @Test
    fun `Covert base`() {
        val trainingEffect = TrainingEffectConverter.convert("AEROBIC_BASE")
        assertThat(trainingEffect).isEqualTo("base")
    }

    @Test
    fun `Covert recovery`() {
        val trainingEffect = TrainingEffectConverter.convert("RECOVERY")
        assertThat(trainingEffect).isEqualTo("recovery")
    }

    @Test
    fun `Covert unknown`() {
        val trainingEffect = TrainingEffectConverter.convert("UNKNOWN")
        assertThat(trainingEffect).isNull()
    }

    @Test
    fun `Covert null`() {
        val trainingEffect = TrainingEffectConverter.convert(null)
        assertThat(trainingEffect).isNull()
    }

}