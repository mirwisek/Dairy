package com.app.dairy

data class DairyItem (
        var cowNo: String? = null,
        var serviceDate: String? = null,
        var method: String? = null,
        var calvingDate: String? = null,
        var gender: String? = null,
        var weight: String? = null,
        var length: String? = null,
        var production: String? = null,
        var locationDate: String? = null,
        var daysDry: Int = 0,
        var peakYield: String? = null,
        var datePeakYield: String? = null,
        var servicePeriod: String? = null,
        var calvingDays: Int = 0
)

data class DairyItems(
        var items: MutableList<DairyItem> = mutableListOf()
)