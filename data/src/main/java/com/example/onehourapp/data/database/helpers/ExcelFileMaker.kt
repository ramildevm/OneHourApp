package com.example.onehourapp.data.database.helpers

import android.content.Context
import android.os.Environment
import com.example.onehourapp.common.utils.CalendarUtil
import com.example.onehourapp.theme.R
import com.example.onehourapp.data.database.dao.ActivityRecordDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ExcelFileMaker @Inject constructor(
    @ApplicationContext private val context:Context,
    private val activityRecordDao: ActivityRecordDao
) {
    suspend fun writeXLSFile(){
        val directory = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS).toString()
        val filePath = "$directory/One hour data backup.xls"
        val file = File(filePath)
        val headers = listOf(context.resources.getString(R.string.color), context.resources.getString(R.string.category), context.resources.getString(R.string.activities), context.resources.getString(R.string.date_and_time))
        val records = activityRecordDao.getActivityRecordsForExcel().first()

        val workBook = HSSFWorkbook()

        val sheet = workBook.createSheet("Data")
        val header = sheet.createRow(0)
//
//        val colors = records.map{ it.color}
//        val palette: HSSFPalette = workBook.customPalette
//        for(i in colors.indices){
//            val colorRed = colors[i].toColorInt().red
//            val colorGreen = colors[i].toColorInt().green
//            val colorBlue = colors[i].toColorInt().blue
//            palette.setColorAtIndex((i+1).toShort(), colorRed.toByte(), colorGreen.toByte(), colorBlue.toByte())
//        }

        // Creating cell and setting the cell value

        for(label in headers.indices){
            header.createCell(label).setCellValue(headers[label])
        }
//        header.createCell(0).setCellValue("Color")
//        header.createCell(1).setCellValue("Category")
//        header.createCell(2).setCellValue("Activity")
//        header.createCell(3).setCellValue("Start time")

        // Creating the 1st row to insert employee record
        for(i in records.indices) {
            val row = sheet.createRow(i + 1)
            val record = records[i]
            val date = CalendarUtil.getFullDateTimeString(record.timestamp)

            row.createCell(0).setCellValue(record.color)
            row.createCell(1).setCellValue(record.category)
            row.createCell(2).setCellValue(record.activity)
            row.createCell(3).setCellValue(date)
        }
        withContext(Dispatchers.IO){
            val fos = FileOutputStream(file)
            workBook.write(fos)
            fos.close()
            workBook.close()
        }
    }
}