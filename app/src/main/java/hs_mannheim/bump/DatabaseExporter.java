package hs_mannheim.bump;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;
import hs_mannheim.bump.Sample;
import jxl.*;
import jxl.write.*;

/**
 * Created by benjamin on 14/05/15.
 *
 * Exports the Database into Excel Format and saves it to Downloads Folder
 */
public class DatabaseExporter {

    private List<ExperimentData> mDataList;
    private Context mContext;
    private WritableCellFormat times;

    public DatabaseExporter(Context context) {
        this.mContext = context;
    }

    public void getAllDatabaseEntries() {
        DBHelper dbHelper = new DBHelper(mContext);
        mDataList = dbHelper.getAllData();
    }

    public void export() {
        getAllDatabaseEntries();

        try {
            createSheet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    public void createSheet() throws IOException, WriteException {
        String firstFileName = "FirstExperiment.xls";
        String firstFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + firstFileName;
        File firstFile = new File(firstFilePath);

        String secondFileName = "SecondExperiment.xls";
        String secondFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + secondFileName;
        File secondFile = new File(secondFilePath);

        WorkbookSettings wbSettings = new WorkbookSettings();
        WritableWorkbook firstWorkbook = Workbook.createWorkbook(firstFile, wbSettings);
        WritableWorkbook secondWorkbook = Workbook.createWorkbook(secondFile, wbSettings);

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getExperimentType() == ExperimentType.FIRST) {
                firstWorkbook.createSheet(mDataList.get(i).getName() + " - " + mDataList.get(i).getThreshold().name(), i);
                WritableSheet excelSheet = firstWorkbook.getSheet(firstWorkbook.getNumberOfSheets()-1);
                createSheetContent(excelSheet, mDataList.get(i).getSamples());
            } else {
                secondWorkbook.createSheet(mDataList.get(i).getName() + " - " + mDataList.get(i).getThreshold().name(), i);
                WritableSheet excelSheet = secondWorkbook.getSheet(secondWorkbook.getNumberOfSheets()-1);
                createSheetContent(excelSheet, mDataList.get(i).getSamples());
            }

        }
        if (firstWorkbook.getNumberOfSheets() != 0) {
            firstWorkbook.write();
        }
        firstWorkbook.close();
        if (secondWorkbook.getNumberOfSheets() != 0) {
        secondWorkbook.write();
        }
        secondWorkbook.close();
        MediaScannerConnection.scanFile(mContext,
                new String[]{firstFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        MediaScannerConnection.scanFile(mContext,
                new String[]{secondFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }



    private void createSheetContent(WritableSheet sheet, List<Sample> samples) throws WriteException {
        addCaption(sheet, 0, 0, "time");
        addCaption(sheet, 1, 0, "x");
        addCaption(sheet, 2, 0, "y");
        addCaption(sheet, 3, 0, "z");
        for (int i = 0; i < samples.size(); i++) {
            addNumber(sheet, 0, i+1, (double) samples.get(i).timestamp);
            addNumber(sheet, 1, i+1, samples.get(i).x);
            addNumber(sheet, 2, i+1, samples.get(i).y);
            addNumber(sheet, 3, i+1, samples.get(i).z);
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String text)
            throws  WriteException {
        Label label;
        label = new Label(column, row, text);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           Double value) throws WriteException {
        jxl.write.Number number = new jxl.write.Number(column, row, value);
        sheet.addCell(number);
    }
}