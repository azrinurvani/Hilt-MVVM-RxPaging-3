package com.mobile.azri.hiltmvvmrxpaging.util

import androidx.room.TypeConverter
import com.mobile.azri.hiltmvvmrxpaging.model.Image
import java.util.*

//Next, we’re going to create Room Converters because we have custom data type in our model: Image and Date
class Converters {

    @TypeConverter
    fun fromTimestamp(value:Long?) : Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date:Date?) : Long?{
        return date?.time
    }

    @TypeConverter
    fun fromImage(image: Image?): String?{
        return image?.url
    }

    @TypeConverter
    fun toImage(url:String?): Image?{
        return url?.let { Image(it) }
    }
}