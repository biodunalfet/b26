package com.hf.btcpricetracker.data

class BtcSchemaMapper : IBtcSchemaMapper {

    override fun apply(data: BtcPriceSchema): BtcPriceData? {
        return BtcPriceData(data.period, data.unit, data.values)
    }

}

interface IBtcSchemaMapper {
    fun apply(data: BtcPriceSchema): BtcPriceData?
}