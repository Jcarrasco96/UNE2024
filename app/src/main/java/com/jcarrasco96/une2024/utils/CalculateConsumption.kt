package com.jcarrasco96.une2024.utils

object CalculateConsumption {

    private val STEPS = intArrayOf(
        0,
        100,
        150,
        200,
        250,
        300,
        350,
        400,
        450,
        500,
        600,
        700,
        1000,
        1800,
        2600,
        3400,
        4200,
        5000
    )
    val AMOUNT = doubleArrayOf(
        0.33,
        1.07,
        1.43,
        2.46,
        3.00,
        4.00,
        5.00,
        6.00,
        7.00,
        11.50,
        11.81,
        12.31,
        13.50,
        14.75,
        16.13,
        17.44,
        18.75,
        25.00
    )
    private val COST_END_STEP = doubleArrayOf(
        0.00,
        33.00,
        86.50,
        158.00,
        281.00,
        431.00,
        631.00,
        881.00,
        1181.00,
        1531.00,
        2681.00,
        3862.00,
        7555.00,
        18355.00,
        30155.00,
        43059.00,
        57011.00,
        72011.00
    )

    fun kWhXSteps(consumption: Long): LongArray {
        val consumptionBySteps = LongArray(STEPS.size) { 0 }
        var consumptionAux = consumption

        for (i in consumptionBySteps.size - 1 downTo 0) {
            if (consumptionAux > STEPS[i]) {
                consumptionBySteps[i] = consumptionAux - STEPS[i]
                consumptionAux -= consumptionBySteps[i]
            }
        }

        return consumptionBySteps
    }

    fun calculateAmount(kWhXSteps: LongArray): Double =
        kWhXSteps.foldRightIndexed(0.0) { index, value, acc -> acc + value * AMOUNT[index] }

    fun amount(consumption: Long): Double = calculateAmount(kWhXSteps(consumption))

    fun canPay(money: Double): Int {
        val index = COST_END_STEP.indexOfFirst { it > money }

        return when {
            index != -1 -> {
                val (firstIndex, secondIndex) = STEPS[index - 1] to STEPS[index]
                (firstIndex..secondIndex).lastOrNull { money >= amount(it.toLong()) } ?: firstIndex
            }

            else -> -1
        }
    }

}