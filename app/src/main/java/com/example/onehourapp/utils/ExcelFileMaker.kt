package com.example.onehourapp.utils

import android.text.Layout.Alignment
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.toColorInt
import com.example.onehourapp.data.models.dto.ExcelRecord
import org.apache.commons.codec.binary.Hex
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFPalette
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat


object ExcelFileMaker {
    fun writeXLSFile(file: File, records:List<ExcelRecord>, headers:List<String>, sheets:List<String> = arrayListOf("Data")){
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
        val fos = FileOutputStream(file)
        workBook.write(fos)
        fos.close()
        workBook.close()
    }
}